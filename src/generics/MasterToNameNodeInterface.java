package generics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public interface MasterToNameNodeInterface  extends Remote {

	public ConcurrentHashMap<String, fakeDistributedFile> sendChunkMap(MapReduceConfiguration config) throws RemoteException, NotBoundException, FileNotFoundException, IOException;
}
