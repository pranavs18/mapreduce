package master;

import generics.ChunkProperties;
import generics.MapReduceConfiguration;
import generics.MapReduceStarterInterface;
import generics.MasterToNameNodeInterface;
import generics.MasterToWorkerInterface;
import generics.TaskDetails;
import generics.fakeDistributedFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class StartMapReduceJob extends UnicastRemoteObject implements MapReduceStarterInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected StartMapReduceJob() throws RemoteException {

	}


	public Boolean StartJob(MapReduceConfiguration config, String splitIp)

			throws RemoteException, MalformedURLException {

		/* Here we first call an rmi function asking name node for the map of splits and their respective locations(IP) */
		Boolean check = null;
		System.out.println("Reached here");
		try {
			MasterToNameNodeInterface fileChunkMapRequest = (MasterToNameNodeInterface)Naming.lookup("rmi://"+MasterGlobalInformation.getNameNodeIp()+":23392/split");
			
			try {
				Set<String> setOfworkerIpAddresses = MasterGlobalInformation.getAllWorkerMapReduceDetails().keySet();
				System.out.println("Key Set : "+setOfworkerIpAddresses);
				HashSet<String >workerIpAddresses = new HashSet<String>();
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
						
						System.out.println(chunkDetails.getKey()+ " zval: "+chunkDetails.getValue().getCHUNK_IP_LIST()+" job: "+chunkDetails.getValue().getJobStatus()+" jobh: "+chunkDetails.getValue().getJobMachineHolder());
						
						/* if the status of the job is complete just continue */
						if(chunkDetails.getValue().getJobStatus().equals("COMPLETE")){
							continue;
						}

						/* if status of the job is available the the job has not been scheduled yet */
						else if(chunkDetails.getValue().getJobStatus().equals("AVAILABLE")){
							
							/* This count is used to check if the IPs containing the file chunk are completely busy*/
							int busyCount = 0;
							
							/* For all ips that contain the file chunk try to launch the job in one */
							for(String anIpInList : chunkDetails.getValue().getCHUNK_IP_LIST()){
								
								if(MasterGlobalInformation.getAllWorkerMapReduceDetails().get(anIpInList).getMapFull() == false){
									Boolean mapperFound = false;
									
									for(ConcurrentHashMap.Entry<String, TaskDetails> idFree : 
										MasterGlobalInformation.getAllWorkerMapReduceDetails().get(anIpInList).getMapStatus().entrySet()){
										if(idFree.getValue().getStatus() == JobStatus.AVAILABLE || idFree.getValue().getStatus() == JobStatus.COMPLETE){
											String mapperID = idFree.getKey();
											System.out.println("Mapper id: "+mapperID);

											/****/
											/* Make RMI Call to worker with args mapperID and fileName*/
											MasterToWorkerInterface launchJob = (MasterToWorkerInterface)Naming.lookup("rmi://"+anIpInList+":9876/job");
											Boolean isJobLaunchSuccessful = launchJob.launchMapJob(chunkDetails.getKey(), mapperID, config);
											System.out.println("Result of launch: "+isJobLaunchSuccessful);
											/****/
											chunkDetails.getValue().setJobMachineHolder(mapperID);
											chunkDetails.getValue().setJobStatus("RUNNING");
											mapperFound = true;
											break;
										}
									}
									if(mapperFound == true){
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
							if(busyCount>=chunkDetails.getValue().getCHUNK_IP_LIST().size()){
								
								MasterRequestForTransfer transferRequest = new MasterRequestForTransfer(fileChunkMapRequest, chunkDetails.getKey(), config.getInputPath(), chunkDetails.getValue().getCHUNK_IP_LIST(), workerIpAddresses);
								new Thread(transferRequest).start();
								busyCount = 0;
							}
						}

						else if(chunkDetails.getValue().getJobStatus().equals("RUNNING")){

							String idToBeinvestigated = chunkDetails.getValue().getJobMachineHolder();
							System.out.println("Id investigated "+idToBeinvestigated);
							
							String ipToBeinvestigated = idToBeinvestigated.substring(0,idToBeinvestigated.indexOf("_"));
							System.out.println("Ip investigated "+ipToBeinvestigated);
							System.out.println(MasterGlobalInformation.getAllWorkerMapReduceDetails().get(ipToBeinvestigated).getMapStatus().get(idToBeinvestigated).getWorkerMapReduceId());
							
							if(MasterGlobalInformation.getAllWorkerMapReduceDetails().get(ipToBeinvestigated).getMapStatus().get(idToBeinvestigated).getStatus()==JobStatus.COMPLETE 
									|| !(MasterGlobalInformation.getAllWorkerMapReduceDetails().get(ipToBeinvestigated).getMapStatus().get(idToBeinvestigated).getFileName().equals(chunkDetails.getKey()))){
								chunkDetails.getValue().setJobStatus("COMPLETE");
								countOfComplete++;
							}
							

						}
						if(countOfComplete>=MasterGlobalInformation.getMasterStaticChunkMap().size()){
							MappersNotCompleted = true;
							break;
						}
						
					}
					if(MappersNotCompleted == true){
						break;
					}	
				}
				
				/* If code reaches here all the map jobs are complete */

				check = true;
			} catch (Exception e) {

				e.printStackTrace();
			}
		} catch (NotBoundException | IOException e) {

			System.out.println("URL not found or not bound");
			check = false;
		}

		return check;
	}

}
