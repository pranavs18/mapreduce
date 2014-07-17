package worker;


import generics.MapReduceConfiguration;
import generics.fakeDistributedFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SlaveRemoteInterface extends Remote {
	
	public ArrayList<fakeDistributedFile> splitFileIntoChunks(String filename , MapReduceConfiguration config) throws RemoteException, FileNotFoundException, IOException;
}

