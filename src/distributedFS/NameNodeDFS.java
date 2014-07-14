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
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import worker.SlaveRemoteInterface;


public class NameNodeDFS extends Thread implements Runnable {
	
	static String filename = "C:/Users/PRANAV/Documents/mapreduce/src/pranav.txt";
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
        // change to canonical path
        System.setProperty("java.security.policy","C:/Users/PRANAV/Documents/mapreduce/policy.txt");
        System.setSecurityManager(new java.rmi.RMISecurityManager());
       
        try {
            ArrayList<String> al = new ArrayList<String>();
			remote = (SlaveRemoteInterface) Naming.lookup("//127.0.0.1:9876/Remote");
			al = remote.splitFileIntoChunks(filename);
			for(int i=0;i< al.size();i++){
				System.out.println("Block Created with name " + al.get(i));
			}
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        new Thread(nn).start();
		
	
	}
	
	public void connectToSlave() throws IOException{
		
	  System.out.println("Distributed Master Node sarted on port 10000");
	  //Socket dfsToMaster= new Socket("127.0.0.1",10000);
	
		}
	
	public void run(){
		try {
			connectToSlave();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
