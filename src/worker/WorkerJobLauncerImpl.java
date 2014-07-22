package worker;

import generics.MapReduceConfiguration;
import generics.MasterToWorkerInterface;
import generics.TaskDetails;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;

import master.JobStatus;


public class WorkerJobLauncerImpl extends UnicastRemoteObject implements MasterToWorkerInterface{


	private static final long serialVersionUID = 1L;

	protected WorkerJobLauncerImpl() throws RemoteException {
		super();
	}

	@Override
	public Boolean launchMapJob(String fileChunkName, String mapperId, MapReduceConfiguration config)
			throws RemoteException, NotBoundException, FileNotFoundException,
			IOException {
		int i;	
		if((i =WorkerTasksStatus.getIncreasedMapperNumber())<=WorkerTasksStatus.getNumberOfMaps() && !(WorkerTasksStatus.getIdListForCheck().containsKey(mapperId))){
			if(i >= WorkerTasksStatus.getNumberOfMaps()){
				WorkerTasksStatus.setMapFull(true);
			}
			else{
				WorkerTasksStatus.setMapFull(false);
			}
			WorkerTasksStatus.getIdListForCheck().put(mapperId, "Entered");
			TaskDetails mapDetails  = new TaskDetails(InetAddress.getLocalHost().getHostAddress(), mapperId, fileChunkName, JobStatus.RUNNING);
			System.out.println("These are sent in heartbeat "+mapperId+" "+fileChunkName+" "+JobStatus.RUNNING); //remove
			WorkerTasksStatus.putInStatusMap(mapperId, mapDetails);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/* Start new Worker thread */

			WorkerMapRunner mapLauncher = new WorkerMapRunner(fileChunkName, mapperId, config);
			new Thread(mapLauncher).start();	
		}
		else{

			WorkerTasksStatus.getDecreasedMapperNumber();
			System.out.println("Workers Maps Full");
			return false;
		}


		return true;
	}


	@Override
	public void sortIntermediateFiles(MapReduceConfiguration config) throws IOException {
	    System.out.println(" LOCAL SORTING BEGINS");
		String filePath = config.getInputPath();
	   System.out.println("sorting..." + filePath);
	   int index = filePath.lastIndexOf("/");
	   String fileName = filePath.substring(index+1, filePath.length()-4);
	   System.out.println("Sorting... " + fileName);
	   String directoryPath = ".." + File.separator + "dfs" + File.separator + "intermediate" + File.separator + fileName;
	   System.out.println("File location ... " + directoryPath);
	   File folder = new File(directoryPath);
	   File[] files = folder.listFiles();
	   
	   for(File file:files){
		   ArrayList<String> temp = new ArrayList<String>();
		   System.out.println("Current file "+ file.getName());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
	        while((line=reader.readLine())!=null){
	            temp.add(line);
	        }
	        Collections.sort(temp);
	        reader.close();
	        
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	        for(String val : temp){
	                writer.write(val);      
	                writer.newLine();
	        }
	        writer.close();
	   }
		
	}

}