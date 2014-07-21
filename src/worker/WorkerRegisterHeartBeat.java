/******************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * WorkerTaskManager class represents the worker node task handler which is responsible for 
 * registering with the master machine and starting the different Mapper and Reduce functions
 * and monitoring their status.
 * 
 *******************************************************************************************/

package worker;

import generics.TaskDetails;
import generics.WorkerMessageToMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerRegisterHeartBeat implements Runnable {

	private String masterIp;
	private final int masterPort = 23333;

	public WorkerRegisterHeartBeat(String masterIp) {
		this.masterIp = masterIp;
	}

	public void startRegisterAndHeartBeat() throws IOException, InterruptedException{

		
		Socket taskManagerSocket = null;
		try {
			taskManagerSocket = new Socket(masterIp, masterPort);
		} catch (IOException e1) {
			System.out.println("Could not establish connection with Master");
		}
		System.out.println("Connection to master established - Sending heart beat");

		System.out.println("Sent hello message to master.. Waiting for configuration message");
	
		InputStreamReader input = new InputStreamReader(taskManagerSocket.getInputStream());
		BufferedReader in = new BufferedReader(input);
		ObjectOutputStream oos = new ObjectOutputStream(taskManagerSocket.getOutputStream());;
		String readS = "";
		
		/* args[] contains the config information (worker id, maxMappers, maxReduces) We store this in a 
		 * global location for all the threads to read */
		String args[];
		
		
		/* Send heart beat with the following information to master
		 * 1. Ip address
		 * 2. Hashmap of map Ids with their current status (Running, Complete, Available) 
		 * 	  (This will not exceed the max count sent by master) 
		 * 3. Hashmap of map Ids with their current status (Running, Complete, Available)
		 * 	  (This will not exceed the max count sent by master) 
		 */
		
		Boolean shouldEnter = true;
		
		while(true){
			ConcurrentHashMap<String, TaskDetails> nonStaticMapObject = new ConcurrentHashMap<String, TaskDetails>();
			ConcurrentHashMap<String, TaskDetails> nonStaticReduceObject = new ConcurrentHashMap<String, TaskDetails>();
			nonStaticMapObject.putAll(WorkerTasksStatus.getTaskStatusMap());
			nonStaticReduceObject.putAll(WorkerTasksStatus.getTaskStatusReduce());
			
			Boolean mapFull = new Boolean(false);
			Boolean reduceFull = new Boolean(false);
			
			if(WorkerTasksStatus.getMapFull() == true){
				mapFull = true;
			}
			else{
				mapFull = false;
			}
			
			if(WorkerTasksStatus.getMapFull() == true){
				reduceFull = true;
			}
			else{
				reduceFull = false;
			}
			
			WorkerMessageToMaster message = new WorkerMessageToMaster(nonStaticMapObject, nonStaticReduceObject,mapFull,reduceFull);
			oos.writeObject(message);
			oos.flush();
			readS = in.readLine();
			
			if(shouldEnter == true){
				args = readS.split(" ");
				/* set worker Id and max mappers and reduces for a single machine */
				new WorkerTasksStatus(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
				WorkerTasksStatus.initialTaskMapCreator();
				System.out.println(readS);
			}
			shouldEnter = false;
			readS = "";
			Thread.sleep(50);
		}
	}

	@Override
	public void run() {

		try {
			startRegisterAndHeartBeat();
		} catch (IOException | InterruptedException e) {
			System.out.println("Client disconnected from server");
		}

	}




}