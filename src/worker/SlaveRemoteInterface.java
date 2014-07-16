package worker;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import distributedFS.fakeDistributedFile;

public interface SlaveRemoteInterface extends Remote {
	
	public ArrayList<fakeDistributedFile> splitFileIntoChunks(String filename) throws RemoteException, FileNotFoundException, IOException;
}

