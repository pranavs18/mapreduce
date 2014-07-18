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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class StartMapReduceJob extends UnicastRemoteObject implements MapReduceStarterInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected StartMapReduceJob() throws RemoteException {

	}


	public Boolean StartJob(MapReduceConfiguration config, String splitIp)

			throws RemoteException, MalformedURLException {

		/* Here we first call an rmi function asking name node for the map of splits and their respective locations(IP) */
		Boolean check = null;
		System.out.println("Reached here");
		try {
			MasterToNameNodeInterface fileChunkMapRequst = (MasterToNameNodeInterface)Naming.lookup("rmi://"+MasterGlobalInformation.getNameNodeIp()+":23392/split");

			try {
				Set<String> setOfworkerIpAddresses = MasterGlobalInformation.getAllWorkerMapReduceDetails().keySet();
				System.out.println("Key Set : "+setOfworkerIpAddresses);
				HashSet<String >workerIpAddresses = new HashSet<String>();
				for(String s: setOfworkerIpAddresses){
					workerIpAddresses.add(s);
					
				}
				

				ConcurrentHashMap<String, fakeDistributedFile> fileChunkMap =  fileChunkMapRequst.sendChunkMap(config,workerIpAddresses, splitIp);

				check = true;
			} catch (Exception e) {

				e.printStackTrace();
			}
		} catch (NotBoundException | IOException e) {

			System.out.println("URL not found or not bound");
			check = false;
		}

		return check;
	}

}
