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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import master.JobStatus;
import master.TransferRMIRequestLauncher;


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

	public Boolean launchTransfer(MapReduceConfiguration config, ConcurrentHashMap<String, HashSet<String>> intermediateFileNameInfo) throws RemoteException, NotBoundException, FileNotFoundException,
	IOException {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for(ConcurrentHashMap.Entry<String, HashSet<String>> info : intermediateFileNameInfo.entrySet()){
		
			FileTransferAfterSort fileTrasnfer = new FileTransferAfterSort(info.getKey(), info.getValue(), config);
			Thread transferThread = new Thread(fileTrasnfer);
			transferThread.start();
			threads.add(transferThread);
		}
		for(Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(WorkerTasksStatus.getAddedWorkerAfterSortCount()>intermediateFileNameInfo.size()){
			return true;
		}
		else{
			return false;
		}

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
        if(files.length > 0){  
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
	
	
	public boolean launchReduceJob(MapReduceConfiguration config) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		int index = config.getInputPath().lastIndexOf("/");
		String inputFile = config.getInputPath().substring(index+1, config.getInputPath().length()-4);
		String path = ".." + File.separator+"dfs" + File.separator + "redinput" + File.separator + inputFile;
		File dir = new File(path);
		File[] files = dir.listFiles();
	    String tail= files[0].getName().substring(files[0].getName().lastIndexOf("_"), files[0].getName().lastIndexOf("."));
	    	
	    
		Map<String, ArrayList<String>> map = new TreeMap<String, ArrayList<String>>();
		System.out.println("REDUCER LAUNCHED"); //remove

		MapReduce mpr = new MapReduce();
       
		Class<?> params[] = {String.class,String.class, MapReduce.class};
		Class<?> mapClass = Class.forName("client.WordCount");
		Object mapObject = mapClass.newInstance();
		Method method = mapClass.getDeclaredMethod("reduce",params);
		method.setAccessible(true);
		
		for(File file:files){
			
			ArrayList<String> al = new ArrayList<String>();
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
				String words[] = line.split("\\s+");
				String key = words[0];
				String value = words[1];
				if(!map.containsKey(key)){
				 al.add(value);
				 map.put(key, al);
				}
				else{
					map.get(key).add(value);
				}
				Object[] arguments= {key,map.get(key)};
				method.invoke(mapObject, arguments);
				map.remove(key);
			}
			buf.close();
		}
		
		
		
		return true;
	}

}