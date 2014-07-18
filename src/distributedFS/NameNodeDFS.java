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

import generics.fakeDistributedFile;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import worker.SlaveRemoteInterface;


public class NameNodeDFS extends Thread implements Runnable {

	static String filename ;

	public static ConcurrentHashMap<String,ArrayList<fakeDistributedFile>> chunkIDtoMachines = new ConcurrentHashMap<String,ArrayList<fakeDistributedFile>>();
	public static ConcurrentHashMap<String,ArrayList<fakeDistributedFile>> FiletoChunkList = new ConcurrentHashMap<String,ArrayList<fakeDistributedFile>>();

	private static SlaveRemoteInterface remote;
	public NameNodeDFS(){

	}
	public static void main(String args[]) throws IOException{

		//NameNodeDFS nn = new NameNodeDFS();
		
		/* Here args[0] = masterIP */
		NameNodeHeartBeat nnhb = new NameNodeHeartBeat(args[0], 23333);
		RemoteCallee rc = new RemoteCallee();
		Registry reg = LocateRegistry.createRegistry(23392);
		reg.rebind("split", rc);
		
	    System.setProperty("java.security.policy","/Users/VSK/Documents/Git/mapreduce/policy.txt");
		System.setSecurityManager(new java.rmi.RMISecurityManager());
		

		new Thread(nnhb).start();
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
