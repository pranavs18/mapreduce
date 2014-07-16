package master;

import generics.WorkerMessageToMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class MasterHeartBeatAnalyzer implements Runnable{

	Socket clientSocket = null;
	
	
	
	public MasterHeartBeatAnalyzer(Socket clientSocket) {
		
		this.clientSocket = clientSocket;
	}

	public void heartBeat() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = null;
		PrintStream out = null;
		InputStreamReader input = null;
		BufferedReader in = null;
		try {
			out = new PrintStream(clientSocket.getOutputStream());
			input = new InputStreamReader(clientSocket.getInputStream());
			in = new BufferedReader(input);
		} catch (IOException e) {
			
			System.out.println("Error Occured while trying to create io streams");
		
		}
		String tempStr = "";
		String ip = "";
		if((tempStr = in.readLine())!=null){
			
			 ip = tempStr.substring(tempStr.indexOf(":")+1, tempStr.length());
			 System.out.println("Connection requested by Worker with Ip: "+ip);
			
		}
		
		int conn = MasterGlobalInformation.getConnectionNumber();
		
		String outString =conn+" "+MasterGlobalInformation.getMaxMapperPerSystem()+" "+MasterGlobalInformation.getMaxReducesPerSystem();
		System.out.println("Sending: "+outString);
		out.println(outString);
		out.flush();
		
		String temp = in.readLine();
		out.println("Start sending");
		out.flush();
				
		while (true ) {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			WorkerMessageToMaster taskMapObject = (WorkerMessageToMaster)ois.readObject();
			tempStr = "";
			MasterGlobalInformation.getAllMapTaskDetails().put(clientSocket.getInetAddress().toString(), taskMapObject.getMapStatus());
			MasterGlobalInformation.getAllReduceTaskDetails().put(clientSocket.getInetAddress().toString(), taskMapObject.getReduceStatus());
			System.out.println("System map info : "+MasterGlobalInformation.getAllMapTaskDetails());
		}
		
		
	}

	public void run() {
		
		try {
			heartBeat();
		} catch (ClassNotFoundException e) {
			System.out.println("Object was not received");
			
		} catch (IOException e) {
			System.out.println("Stream error");
		}
		
	}

}
