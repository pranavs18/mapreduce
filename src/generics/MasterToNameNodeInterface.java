package generics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface MasterToNameNodeInterface  extends Remote {

	public ConcurrentHashMap<String, fakeDistributedFile> sendChunkMap(MapReduceConfiguration config, Set<String> workerIpAddresses, String ipOfMainfile) throws RemoteException, NotBoundException, FileNotFoundException, IOException;
}
