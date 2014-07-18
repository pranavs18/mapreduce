package worker;


import generics.MapReduceConfiguration;
import generics.fakeDistributedFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface SlaveRemoteInterface extends Remote {
	
	public ArrayList<fakeDistributedFile> splitFileIntoChunks(String filename , MapReduceConfiguration config, Set<String> workerIps , String splitIp) throws RemoteException, FileNotFoundException, IOException;
	public void transferChunks(String fileName, byte buffer[]) throws IOException;
}

