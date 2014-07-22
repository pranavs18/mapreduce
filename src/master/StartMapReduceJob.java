package master;

import generics.ChunkProperties;
import generics.MapReduceConfiguration;
import generics.MapReduceStarterInterface;
import generics.MasterToNameNodeInterface;
import generics.MasterToWorkerInterface;
import generics.TaskDetails;
import generics.WorkerMessageToMaster;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



public class StartMapReduceJob extends UnicastRemoteObject implements MapReduceStarterInterface{

	private static final long serialVersionUID = 1L;
	HashSet<String >workerIpAddresses;
	protected MapReduceConfiguration config;
	protected StartMapReduceJob() throws RemoteException {

	}


	public Boolean StartJob(MapReduceConfiguration config, String splitIp)

			throws RemoteException, MalformedURLException {

		/* Here we first call an RMI function asking name node for the map of splits and their respective locations(IP) */
		Boolean check = null;
		this.config = config;
		System.out.println("Reached here");

		try {
			MasterToNameNodeInterface fileChunkMapRequest = (MasterToNameNodeInterface)Naming.lookup("rmi://"+MasterGlobalInformation.getNameNodeIp()+":23392/split");

			try {
				Set<String> setOfworkerIpAddresses = MasterGlobalInformation.getAllWorkerMapReduceDetails().keySet();
				System.out.println("Key Set : "+setOfworkerIpAddresses);
				workerIpAddresses = new HashSet<String>();
				for(String s: setOfworkerIpAddresses){
					workerIpAddresses.add(s);

				}

				ConcurrentHashMap<String, ChunkProperties> fileChunkMap =  fileChunkMapRequest.sendChunkMap(config,workerIpAddresses, splitIp);
				MasterGlobalInformation.setMasterStaticChunkMap(fileChunkMap);

				/* Start Launching Mappers */
				int countOfComplete = 0;

				while(true){
					/* Used for checking if all map jobs have completed their work */
					Boolean MappersNotCompleted = false;


					/* we iterate through the chunkMapobtained from Name node to launch Job */
					for(ConcurrentHashMap.Entry<String, ChunkProperties> chunkDetails : MasterGlobalInformation.getMasterStaticChunkMap().entrySet()){


						System.out.println("\n HeartBeat status \n");

						for(ConcurrentHashMap.Entry<String,WorkerMessageToMaster> masterStatus : MasterGlobalInformation.getAllWorkerMapReduceDetails().entrySet()){

							for(ConcurrentHashMap.Entry<String,TaskDetails> instatus: MasterGlobalInformation.getAllWorkerMapReduceDetails().get(masterStatus.getKey()).getMapStatus().entrySet()){

								System.out.println("Key: "+instatus.getKey()+" Latest fileRun: "+instatus.getValue().getFileName()+" Current Status: "+instatus.getValue().getStatus());
							}

						}


						/* if the status of the job is complete just continue */
						if(chunkDetails.getValue().getJobStatus().equals("COMPLETE")){
							continue;
						}

						/* if status of the job is available the the job has not been scheduled yet */
						else if(chunkDetails.getValue().getJobStatus().equals("AVAILABLE")){

							/* This count is used to check if the IPs containing the file chunk are completely busy*/
							int busyCount = 0;

							/* For all ips that contain the file chunk try to launch the job in one of them*/
							for(String anIpInList : chunkDetails.getValue().getCHUNK_IP_LIST()){

								if(MasterGlobalInformation.getAllWorkerMapReduceDetails().get(anIpInList).getMapFull() == false){
									Boolean mapperFound = false;

									for(ConcurrentHashMap.Entry<String, TaskDetails> idFree : 
										MasterGlobalInformation.getAllWorkerMapReduceDetails().get(anIpInList).getMapStatus().entrySet()){
										if(idFree.getValue().getStatus() == JobStatus.AVAILABLE || idFree.getValue().getStatus() == JobStatus.COMPLETE){
											String mapperID = idFree.getKey();


											/* Make RMI Call to worker with args mapperID and fileName*/
											MasterToWorkerInterface launchJob = (MasterToWorkerInterface)Naming.lookup("rmi://"+anIpInList+":9876/job");
											Boolean isJobLaunchSuccessful = launchJob.launchMapJob(chunkDetails.getKey(), mapperID, config);
											Thread.sleep(50);

											if(isJobLaunchSuccessful==false)
												continue;
											else{
												chunkDetails.getValue().setJobMachineHolder(mapperID);
												chunkDetails.getValue().setJobStatus("RUNNING");
												mapperFound = true;
												break;
											}
										}
									}
									if(mapperFound == true){
										mapperFound = false;
										break;
									}
								}

								else{
									busyCount++;
									if(busyCount>=chunkDetails.getValue().getCHUNK_IP_LIST().size()){
										break;
									}
								}

							}

							/* if all ips are busy we go to the next chunk and launch it in one of its ips. We will come back to this 
							 * chunk in the next iteration */
							if(busyCount>=chunkDetails.getValue().getCHUNK_IP_LIST().size()){
								busyCount = 0;
							}
						}
						/* if the file chunk is in running status we check the corresponding heart beat value 
						 * and change the status of this chunks job status based on it */
						else if(chunkDetails.getValue().getJobStatus().equals("RUNNING")){

							String idToBeinvestigated = chunkDetails.getValue().getJobMachineHolder();

							String ipToBeinvestigated = idToBeinvestigated.substring(0,idToBeinvestigated.indexOf("_"));
							if(MasterGlobalInformation.getAllWorkerMapReduceDetails().get(ipToBeinvestigated).getMapStatus().get(idToBeinvestigated).getStatus()==JobStatus.COMPLETE ){

								chunkDetails.getValue().setJobStatus("COMPLETE");
								countOfComplete++;

							}


						}
						if(countOfComplete >=MasterGlobalInformation.getMasterStaticChunkMap().size()){
							MappersNotCompleted = true;
							break;
						}



					}
					if(MappersNotCompleted == true){
						MappersNotCompleted = false;
						break;
					}	
				}


				/* If code reaches here all the map jobs are complete 
				   		   Partitioner Logic to Sort the shuffled data */
				for(String workerIp:workerIpAddresses){
					MasterToWorkerInterface sortFiles = (MasterToWorkerInterface)Naming.lookup("rmi://"+workerIp+":9876/job");
					sortFiles.sortIntermediateFiles(this.config);
				}

				/* Transfer file to the corresponding reducers. 
				 * The number of reduce will always be the max of either userProvided number 
				 * or total number of nodes online, 
				 */
				int numberOfReducers = config.getReducers() > workerIpAddresses.size() ? workerIpAddresses.size() : config.getReducers();
				HashSet<String> requiredWorkerIps = new HashSet<String>();
				Queue<String> interMediateFileNameExtensionQueue = new LinkedList<String>();
				for(int i = 0; i< numberOfReducers; i++){
					String tempString = "_mapper_"+i+".txt";
					interMediateFileNameExtensionQueue.add(tempString);
				}
				
				requiredWorkerIps.add(config.getSplitIP());
				for(String str : workerIpAddresses){
					requiredWorkerIps.add(str);
					if(numberOfReducers == requiredWorkerIps.size()){
						break;
					}
				}
				ConcurrentHashMap<String, HashSet<String>> trasferInfo = new ConcurrentHashMap<String, HashSet<String>>();
				while(!interMediateFileNameExtensionQueue.isEmpty()){
					for(String str : requiredWorkerIps){

						if(interMediateFileNameExtensionQueue.isEmpty()){
							break;
						}
						if(!trasferInfo.containsKey(str)){
							HashSet<String> newobj = new HashSet<String>();
							newobj.add(interMediateFileNameExtensionQueue.remove());
							trasferInfo.put(str, newobj);
						}
						else if(trasferInfo.containsKey(str)){

							trasferInfo.get(str).add(interMediateFileNameExtensionQueue.remove());	
						}
					}	
				}
				System.out.println("Transfer Info"+trasferInfo);
			
				if(!trasferInfo.containsKey(this.config.getSplitIP())){
					
					for(ConcurrentHashMap.Entry<String, HashSet<String>> str : trasferInfo.entrySet()){
						trasferInfo.put(this.config.getSplitIP(),trasferInfo.get(str.getKey()));
						trasferInfo.remove(str.getKey());
						break;						
					}
				}

				ArrayList<Thread> threads = new ArrayList<Thread>(); 
				for(String str: requiredWorkerIps){
					TransferRMIRequestLauncher trasfer = new TransferRMIRequestLauncher(str, trasferInfo, config);
					Thread transferThread = new Thread(trasfer);
					transferThread.start();
					threads.add(transferThread);
				}
				for(Thread t : threads){
					t.join();
				}
				
				if(MasterGlobalInformation.getIncreasedTransferSuccessCount()>requiredWorkerIps.size()){
					MasterGlobalInformation.resetTransferCount();
				}
				else{
					return false;
				}
				
				
				/* Start Reducers here */	
				ArrayList<Thread> redthreads = new ArrayList<Thread>(); 
				for(String str: requiredWorkerIps){
					ReduceLauncher reduce = new ReduceLauncher(config, str);
					Thread transferThread = new Thread(reduce);
					transferThread.start();
					redthreads.add(transferThread);
				}
				for(Thread t : redthreads){
					t.join();
				}

				
				if(MasterGlobalInformation.getIncreasedReducerSuccessCount()>requiredWorkerIps.size()){
					MasterGlobalInformation.resetReducerCount();
				}
				else{
					return false;
				}
				
				
				check = true;

			} catch (Exception e) {

				e.printStackTrace();
			}
		} catch (NotBoundException | IOException e) {

			System.out.println("URL not found or not bound");
			check = false;
		}
		
		MasterGlobalInformation.resetReducerCount();
		MasterGlobalInformation.resetTransferCount();
		MasterGlobalInformation.getMasterStaticChunkMap().clear();
		return check;
	}

}
