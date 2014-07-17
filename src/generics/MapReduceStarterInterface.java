package generics;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MapReduceStarterInterface extends Remote {
	
		Boolean StartJob(MapReduceConfiguration config, byte[] jarBytes) throws RemoteException, MalformedURLException;
		
}
