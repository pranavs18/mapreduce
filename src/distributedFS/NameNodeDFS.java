package distributedFS;
/*****************************************************************************************
 * 
 * @author - Pranav Saxena/Vaibhav Suresh Kumar
 * 
 * NameNodeDFS class acts as the master for our own distributed file system 
 * Functionality - 
 * 
 * 
 ************************************************************************************************/




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import worker.SlaveRemoteInterface;


public class NameNodeDFS extends Thread implements Runnable {
	
	static String filename = "src/pranav.txt";
	private int chunkID = 0;  // unique chunk identifier
	// map for storing filename and chunk ID
	ConcurrentHashMap<String,String> chunkMap = new ConcurrentHashMap<String,String>();  
	//map for storing the host which contains
	// the chunks its local file system
	ConcurrentHashMap<Integer,String> hostContainer = new ConcurrentHashMap<Integer,String>();
  	private int portNumber = 10000;
  	private static SlaveRemoteInterface remote;
	public NameNodeDFS(){
		
	}
	public static void main(String args[]) throws IOException{
		
        NameNodeDFS nn = new NameNodeDFS();
        System.setProperty("java.security.policy","C:/Users/PRANAV/Documents/mapreduce/policy.txt");
        System.setSecurityManager(new java.rmi.RMISecurityManager());
       // Registry registry = LocateRegistry.getRegistry(9876);
        try {
			remote = (SlaveRemoteInterface) Naming.lookup("//127.0.0.1:9876/Remote");
			remote.splitFileIntoChunks(filename);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        new Thread(nn).start();
		
	
	}
	
	public void connectToSlave() throws IOException{
		
	  System.out.println("Distributed Master Node sarted on port 10000");
	  //Socket dfsToMaster= new Socket("127.0.0.1",10000);
	
		}
	
	@SuppressWarnings("resource")
	public ArrayList<String> splitFileIntoChunks(String filename) throws IOException{
		ArrayList<String> chunkContainer = new ArrayList<String>();
		File file = new File(filename);
		int numberofLines = 0;
		int chunkSize = 25;
		int chunkNumber = 1;
		if(!file.exists())
			return chunkContainer;
		else
			System.out.println("\n File found");
		Scanner scanner = null;
	    
	    scanner = new Scanner(file);
		
		String chunkFileName = filename.substring(0, filename.length()-4);
		String newChunkName = chunkFileName + chunkNumber;
		BufferedWriter bw = null;
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
			bw = new BufferedWriter(new FileWriter(newChunkName, true));
		
			  numberofLines++;
			  try{
			  bw.write(line);
			  bw.newLine();
		      bw.flush();
			  }
			  catch(Exception e){
				  
			  }
			if(numberofLines == chunkSize){
				chunkNumber++;
				try {
					chunkContainer.add(newChunkName);
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				newChunkName = chunkFileName + chunkNumber;
				numberofLines = 0;
				  
		  }
		}
		return chunkContainer;
	}
	
	public void run(){
		try {
			connectToSlave();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
