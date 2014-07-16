package master;

import generics.MapReduceConfiguration;
import generics.MapReduceStarterInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StartMapReduceJob extends UnicastRemoteObject implements MapReduceStarterInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nameNodeIp;
	
	protected StartMapReduceJob(String nameNodeIp) throws RemoteException {
		this.nameNodeIp = nameNodeIp;
	}

	@Override
	public Boolean StartJob(MapReduceConfiguration config, byte[] jarBytes)
			throws RemoteException {
		 
		/* Here we first call an rmi function asking name node for the map of splits and their respective locations(IP) */
	//	MapReduceStarterInterface jobStarter = (MapReduceStarterInterface)Naming.lookup("rmi://localhost:23390/launcher");		
		
		//Boolean status = jobStarter.StartJob(config, JarFileByteArray);
		
		
		return null;
	}

}
