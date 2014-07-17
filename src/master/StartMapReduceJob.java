package master;

import generics.MapReduceConfiguration;
import generics.MapReduceStarterInterface;
import generics.MasterToNameNodeInterface;
import generics.fakeDistributedFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class StartMapReduceJob extends UnicastRemoteObject implements MapReduceStarterInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nameNodeIp = "127.0.0.1";
	
	protected StartMapReduceJob() throws RemoteException {
		
	}

	@Override
	public Boolean StartJob(MapReduceConfiguration config)
			throws RemoteException, MalformedURLException {
		 
		/* Here we first call an rmi function asking name node for the map of splits and their respective locations(IP) */
		
		System.out.println("Reached here");
		try {
			MasterToNameNodeInterface fileChunkMapRequst = (MasterToNameNodeInterface)Naming.lookup("rmi://127.0.0.1:23392/split");
			try {
				ConcurrentHashMap<String, fakeDistributedFile> fileChunkMap =  fileChunkMapRequst.sendChunkMap(config);
				System.out.println(fileChunkMap);
			} catch (Exception e) {

				e.printStackTrace();
			}
		} catch (NotBoundException | IOException e) {
			System.out.println("URL not found or not bound");
			e.printStackTrace();
		}
		
		
		
		
		
		return true;
	}

}
