package generics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterToWorkerInterface extends Remote  {
	
	public Boolean launchMapJob(String fileChunkName, String mapperId, MapReduceConfiguration config) throws RemoteException, NotBoundException, FileNotFoundException, IOException;
    public void sortIntermediateFiles(MapReduceConfiguration config) throws IOException;   
	
}