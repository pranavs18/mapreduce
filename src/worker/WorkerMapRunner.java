package worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

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


			Class<?> params[] = {String.class,String.class, Mapper.class};
			Class<?> mapClass = Class.forName("client.WordCount");
			String fileInputPath = ".."+File.separator+"dfs"+File.separator+"chunks"+File.separator+fileName;	
			System.out.println("Searching for a file at... " +fileInputPath);

			Mapper mpr = new Mapper(fileName, mapReduceConfig.getInputPath(), mapReduceConfig.getReducers());



			Object mapObject = mapClass.newInstance();
			Method method = mapClass.getDeclaredMethod("map",params);
			method.setAccessible(true);


			File file = new File(fileInputPath);
			if(file.exists()){
				BufferedReader buf = null;
				try {
					buf = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					System.out.println("File not found for reading");
					e.printStackTrace();
				}
				String line;
				Integer i = 0;
				while((line = buf.readLine()) != null){
					i++;
					Object[] arguments= {i.toString(),line,mpr};
					method.invoke(mapObject, arguments);
				}
				buf.close();
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | IOException e) {
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
		WorkerTasksStatus.getIdListForCheck().remove(allotedId);
		for(ConcurrentHashMap.Entry<String, TaskDetails> status: WorkerTasksStatus.getTaskStatusMap().entrySet()){
             
			System.out.println("Key: "+status.getKey()+" Latest fileRun: "+status.getValue().getFileName()+" Current Status: "+status.getValue().getStatus());
 
		}

	}

}
