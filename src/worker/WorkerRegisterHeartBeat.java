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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WorkerRegisterHeartBeat implements Runnable {
	
	String masterIp;
	int masterPort;
	
	public WorkerRegisterHeartBeat(String masterIp, int masterPort) {
		this.masterIp = masterIp;
		this.masterPort = masterPort;
	}
	
	public void startRegisterAndHeartBeat() throws IOException{
		
		
		Socket taskManagerSocket = null;
		try {
			taskManagerSocket = new Socket(masterIp, masterPort);
		} catch (IOException e1) {
			System.out.println("Could not establish connection with Master");
		}
		System.out.println("Connection to master established - Sending heart beat");
		
		/* Keeps sending heartbeat with process map and its own server port number */
		
		PrintStream out = null;
		InputStreamReader input = null;
		BufferedReader in = null;
		try {
			out = new PrintStream(taskManagerSocket.getOutputStream());
			input = new InputStreamReader(taskManagerSocket.getInputStream());
			in = new BufferedReader(input);
		} catch (IOException e) {
			System.out.println("Error Occured while trying to create io streams");
		}	
		
		
		try {
			out.println("Hello from Ip:"+InetAddress.getLocalHost().getHostAddress()+" and Port:20000");
		} catch (UnknownHostException e1) {
			System.out.println("Could not obtain local Machines Ip address");
		}
		out.flush();
		System.out.println("Sent hello message to master.. Waiting for configuration message");
		
		
		
		try {
			/*Get configuration input from Master */
			String args[];
			String readString = "";
			while(( readString = in.readLine()) != null){
				/* args[] contains the config information (worker id, maxMappers, maxReduces) We store this in a 
				 * global location for all the threads to read */
				args = readString.split(" ");
				new WorkerTasksStatus(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				WorkerTasksStatus.initialTaskMapCreator();
			}
		} catch (IOException e) {
			System.out.println("Error occured while reading a line");
		}
		
		out.close(); //Closing initial PrintStream
			
		
		/* Send heart beat with the following information to master
		 * 1. Ip address
		 * 2. Hashmap of map Ids with their current status (Running, Complete, Available) 
		 * 	  (This will not exceed the max count sent by master) 
		 * 3. Hashmap of map Ids with their current status (Running, Complete, Available)
		 * 	  (This will not exceed the max count sent by master) 
		 */
		
		String readS = "";
		while((readS = in.readLine())!=null){
			System.out.println("Server Response: "+readS);
			WorkerMessageToMaster message = new WorkerMessageToMaster(WorkerTasksStatus.getTaskStatusMap(), WorkerTasksStatus.getTaskStatusReduce());
			PrintStream ipOut = null;
			ipOut = new PrintStream(taskManagerSocket.getOutputStream());
			ipOut.println(InetAddress.getLocalHost().getHostAddress());
			ipOut.flush();
			ipOut.close();  // to avoid any multiple stream error
			
			ObjectOutputStream oos = new ObjectOutputStream(taskManagerSocket.getOutputStream());
			oos.writeObject(message);
			oos.close(); // to avoid any multiple stream error
		}	
		in.close();
		input.close();
	}

	@Override
	public void run() {
		
		try {
			startRegisterAndHeartBeat();
		} catch (IOException e) {
			System.out.println("Could not register worker");
		}
		
	}
	
	
	

}
