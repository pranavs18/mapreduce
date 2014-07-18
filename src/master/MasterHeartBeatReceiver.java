package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MasterHeartBeatReceiver implements Runnable {

	private int port = 23333 ;	
	public void createConnection() throws IOException, ClassNotFoundException{
		ServerSocket ss = null;

		try {
			ss = new ServerSocket(port);	
			while(true){
				Socket clientSocket = ss.accept();
				MasterHeartBeatAnalyzer newHeartBeatThread = new MasterHeartBeatAnalyzer(clientSocket);
				new Thread(newHeartBeatThread).start();
			}
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		finally{

			ss.close();

		}
	}

	public void run(){

		MasterHeartBeatReceiver pm = new MasterHeartBeatReceiver();
		try {
			pm.createConnection();
		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}
	}


}


