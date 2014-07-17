package distributedFS;

import generics.MapReduceConfiguration;
import generics.MasterToNameNodeInterface;
import generics.fakeDistributedFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import worker.SlaveRemoteInterface;

public class RemoteCallee extends UnicastRemoteObject implements MasterToNameNodeInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected RemoteCallee() throws RemoteException {
		
	}

	public static ConcurrentHashMap<String,ArrayList<fakeDistributedFile>> chunkIDtoMachines = new ConcurrentHashMap<String,ArrayList<fakeDistributedFile>>();
	public static ConcurrentHashMap<String,ArrayList<fakeDistributedFile>> FiletoChunkList = new ConcurrentHashMap<String,ArrayList<fakeDistributedFile>>();

	private static SlaveRemoteInterface remote;
	
	public ConcurrentHashMap<String, ArrayList<fakeDistributedFile>> sendChunkMap(MapReduceConfiguration config) throws RemoteException, NotBoundException, FileNotFoundException, IOException{
		ArrayList<fakeDistributedFile> al = new ArrayList<fakeDistributedFile>();
		remote = (SlaveRemoteInterface) Naming.lookup("//127.0.0.1:9876/Remote");
		al = remote.splitFileIntoChunks(config.getInputPath(), config);

		for(int i=0;i< al.size();i++){
			System.out.println("Block Created with name " + al.get(i));
		}
		FiletoChunkList.put(config.getInputPath(), al);

		for(int i=0;i< al.size();i++){

			String chunkID = al.get(i).getChunkID();
			chunkIDtoMachines.put(chunkID, al);
	}
		return chunkIDtoMachines;
  }
}