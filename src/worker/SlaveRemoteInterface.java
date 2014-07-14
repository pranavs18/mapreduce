package worker;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SlaveRemoteInterface extends Remote {
	
	public ArrayList<String> splitFileIntoChunks(String filename) throws RemoteException, FileNotFoundException, IOException;
}

