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

import generics.WorkerMessageToMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WorkerRegisterHeartBeat implements Runnable {

	private String masterIp;
	private final int masterPort = 23333;

	public WorkerRegisterHeartBeat(String masterIp) {
		this.masterIp = masterIp;
	}

	public void startRegisterAndHeartBeat() throws IOException, InterruptedException{

		ObjectOutputStream oos = null;
		Socket taskManagerSocket = null;
		try {
			taskManagerSocket = new Socket(masterIp, masterPort);
		} catch (IOException e1) {
			System.out.println("Could not establish connection with Master");
		}
		System.out.println("Connection to master established - Sending heart beat");

		/* Keeps sending heart beat with process map and its own server port number */

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
			out.println("Hello from Ip:"+InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			System.out.println("Could not obtain local Machines Ip address");
		}
		out.flush();
		System.out.println("Sent hello message to master.. Waiting for configuration message");

		try {
			/*Get configuration input from Master */
			String args[];
			String readString = "";
			
			if(( readString = in.readLine()) != null){
				/* args[] contains the config information (worker id, maxMappers, maxReduces) We store this in a 
				 * global location for all the threads to read */
				System.out.println("Server sent the following config: "+readString );
				args = readString.split(" ");
				new WorkerTasksStatus(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				WorkerTasksStatus.initialTaskMapCreator();
				out.println("Thankyou");
				out.flush();
			}



			/* Send heart beat with the following information to master
			 * 1. Ip address
			 * 2. Hashmap of map Ids with their current status (Running, Complete, Available) 
			 * 	  (This will not exceed the max count sent by master) 
			 * 3. Hashmap of map Ids with their current status (Running, Complete, Available)
			 * 	  (This will not exceed the max count sent by master) 
			 */

			String readS = "";
			while(true){
				
				WorkerMessageToMaster message = new WorkerMessageToMaster(WorkerTasksStatus.getTaskStatusMap(), WorkerTasksStatus.getTaskStatusReduce());
				oos = new ObjectOutputStream(taskManagerSocket.getOutputStream());
				oos.writeObject(message);
				oos.flush();

				readS = "";
				Thread.sleep(1000);
			}
		} catch (IOException e) {
			System.out.println("Error occured while reading a line");
		}
		finally{
			oos.close();
			taskManagerSocket.close();
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
