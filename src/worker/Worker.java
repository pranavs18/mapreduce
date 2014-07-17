package worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Worker implements Runnable {

	private String MasterIp ;
	int workerServerPort = 23333;

	public Worker(String MasterIp){

		this.MasterIp = MasterIp;	
	}

	public Worker() {	

	}

	public void startWorkerHost(String MasterIp) throws UnknownHostException, IOException{
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
			/* This might not be required when using an RMI */
			startWorkerHost(MasterIp);
		} catch (UnknownHostException e) {	
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws RemoteException{
		if(args.length != 1){
			System.out.println("Please enter the Arguments of the form - MasterIp");

		}


		MasterInformation.setMasterHost(args[0]);
		String MasterIp = MasterInformation.getMasterHost(); 


		System.setProperty("java.security.policy","C:/Users/PRANAV/Documents/mapreduce/policy.txt");
		System.setSecurityManager(new java.rmi.RMISecurityManager());
		RemoteSplitterImpl remote = new RemoteSplitterImpl();
		
		
		try {

			Registry registry = LocateRegistry.createRegistry(9876);
			registry.rebind("Remote", remote);
			System.out.println("Remote bounded" + remote);
		} catch (RemoteException e) {

		}

		//Worker worker = new Worker(MasterIp);

		WorkerRegisterHeartBeat heartBeat = new WorkerRegisterHeartBeat(MasterIp);

		// Starts worker host thread
		//new Thread(worker).start();

		// Start heart beat thread
		new Thread(heartBeat).start();
	}

}