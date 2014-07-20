package master;

import generics.WorkerMessageToMaster;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class MasterHeartBeatAnalyzer implements Runnable{

	Socket clientSocket = null;

	public MasterHeartBeatAnalyzer(Socket clientSocket) {

		this.clientSocket = clientSocket;
	}

	public void heartBeat() throws IOException, ClassNotFoundException{
		try{
			PrintStream out = new PrintStream(clientSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

			String tempStr = "";
			int conn = MasterGlobalInformation.getConnectionNumber();
			Boolean shouldEnter = true;
			while (true ) {

				WorkerMessageToMaster taskMapObject = (WorkerMessageToMaster)ois.readObject();
				tempStr = "";
				tempStr = clientSocket.getInetAddress().toString().substring(1);
				MasterGlobalInformation.getAllWorkerMapReduceDetails().put(tempStr, taskMapObject);
				
				String outString =conn+" "+MasterGlobalInformation.getMaxMapperPerSystem()+" "+MasterGlobalInformation.getMaxReducesPerSystem();
				out.println(outString);
				out.flush();

				if(shouldEnter == true){
					System.out.println("Connection established with worker: "+clientSocket.getInetAddress().getHostAddress().toString());
					System.out.println("Alloted Id: "+conn);
				}
				shouldEnter = false;

			}
		}catch(IOException e){
			System.out.println("Stream Error");
			e.printStackTrace();
		}
	}



	public void run() {

		try {
			heartBeat();
		} catch (ClassNotFoundException e) {
			System.out.println("Object was not received");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Stream error");
		}

	}

}