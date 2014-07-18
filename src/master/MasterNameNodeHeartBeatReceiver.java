package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterNameNodeHeartBeatReceiver implements Runnable{
	private int port = 23340 ;	
	public void createConnection() throws IOException, ClassNotFoundException{
		ServerSocket ss = null;

		try {
			ss = new ServerSocket(port);	
			Socket clientSocket = ss.accept();
			
			PrintStream out = new PrintStream(clientSocket.getOutputStream());
			InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader in = new BufferedReader(input);
			
			String nameNodeIp = clientSocket.getInetAddress().getHostAddress().toString();
			MasterGlobalInformation.setNameNodeIp(nameNodeIp);
			while(in.readLine()!=null){
				out.println("Hello");
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

		MasterNameNodeHeartBeatReceiver pm = new MasterNameNodeHeartBeatReceiver();
		try {
			pm.createConnection();
		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

}
