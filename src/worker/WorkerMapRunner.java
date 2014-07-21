package worker;

import java.net.InetAddress;
import java.net.UnknownHostException;

import master.JobStatus;

import com.sun.jmx.snmp.tasks.TaskServer;

import generics.MapReduceConfiguration;
import generics.TaskDetails;

public class WorkerMapRunner implements Runnable{

	String fileName = "";
	String allotedId = "";
	MapReduceConfiguration mapReduceConfig = null;


	public WorkerMapRunner(String fileName, String allotedId,
			MapReduceConfiguration mapReduceConfig) {
		super();
		this.fileName = fileName;
		this.allotedId = allotedId;
		this.mapReduceConfig = mapReduceConfig;
	}


	@Override
	public void run() {

		try {
			/* read a file and Use reflections to run Map function */
			/* Store the output in a file in key Value pair */
			System.out.println("Reached Launcher"); //remove
			
			Thread.sleep(1000); //remove
			
			TaskDetails mapDetails = new TaskDetails(InetAddress.getLocalHost().getHostAddress(), allotedId, fileName, JobStatus.COMPLETE);
			WorkerTasksStatus.putInStatusMap(allotedId, mapDetails);
			WorkerTasksStatus.getDecreasedMapperNumber();
			WorkerTasksStatus.setMapFull(false);
			System.out.println("These are sent in heartbeat "+allotedId+" "+fileName+" "+JobStatus.COMPLETE); //remove
			
		} catch (UnknownHostException | InterruptedException e) {
			System.out.println("Inet Address Error");
		}



	}





}
