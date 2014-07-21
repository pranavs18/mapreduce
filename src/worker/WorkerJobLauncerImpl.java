package worker;

import generics.MapReduceConfiguration;
import generics.MasterToWorkerInterface;
import generics.TaskDetails;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import master.JobStatus;


public class WorkerJobLauncerImpl extends UnicastRemoteObject implements MasterToWorkerInterface{

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


}