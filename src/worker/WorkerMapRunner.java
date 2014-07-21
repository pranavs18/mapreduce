package worker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import master.JobStatus;
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
			
			Class<?> params[] = {};
			Class<?> mapClass = Class.forName("client.WordCount");
			Object[] arguments= {};
			
			Object mapObject = mapClass.newInstance();
			
			Method method = mapClass.getDeclaredMethod("map",params);
			method.setAccessible(true);
			method.invoke(mapObject, (Object[])null);
	
			
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Inet Address Error");
			e.printStackTrace();
		}

		TaskDetails mapDetails = null;
		try {
			mapDetails = new TaskDetails(InetAddress.getLocalHost().getHostAddress().toString(), allotedId, fileName, JobStatus.COMPLETE);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		WorkerTasksStatus.putInStatusMap(allotedId, mapDetails);
		WorkerTasksStatus.getDecreasedMapperNumber();
		WorkerTasksStatus.setMapFull(false);
		System.out.println("These are sent in heartbeat "+allotedId+" "+fileName+" "+JobStatus.COMPLETE); //remove

	}





}
