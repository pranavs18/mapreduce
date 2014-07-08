package generics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Slave implements Runnable {

	String MasterIp ;
	private final int MasterPort = 9999;
	int workerServerPort;
	
	public Slave(String MasterIp, int workerServerPort){
		
		this.MasterIp = MasterIp;
		this.workerServerPort = workerServerPort;	
	}
	
	public Slave() {
	
		
	}
	
	public void startSlaveHost(String MasterIp, int workerServerPort) throws UnknownHostException, IOException{
		@SuppressWarnings("resource")
		ServerSocket workerServer = new ServerSocket(workerServerPort);

		while(true){
		
		Socket masterClientSocket = workerServer.accept();	
		InputStreamReader input = new InputStreamReader(masterClientSocket.getInputStream());
		BufferedReader in = new BufferedReader(input);
		
		String[] arguments;
		
		String readString = "";
		while(( readString = in.readLine()) != null){
			arguments = readString.split(" ");
			
			/* Start a new thread to perform operations as required by the 
			 * message sent by master
			 */	  

		 }

		}
	  
	}
	
	@Override
	public void run() {
		try {
			startSlaveHost(MasterIp,workerServerPort);
		} catch (UnknownHostException e) {	
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
		
    public static void main(String args[]){
    	if(args.length != 3){
			System.out.println("Please enter the Arguments of the form - HostIp port");
			
		}
		
		String MasterIp = args[0]; 
		int workerServerPort = Integer.parseInt(args[2]);
	
		Slave worker = new Slave(MasterIp, workerServerPort);
		//HeartBeat heartBeat = new HeartBeat(MasterIp, MasterPort,workerServerPort);
		
		// Starts worker host thread
		new Thread(worker).start();
		
		// Start heartbeat thread
		//new Thread(heartBeat).start();
    }

}