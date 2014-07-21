package generics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface MasterToNameNodeInterface  extends Remote {


	public ConcurrentHashMap<String, ChunkProperties> sendChunkMap(MapReduceConfiguration config, Set<String> workerIpAddresses , String splitIp) throws RemoteException, NotBoundException, FileNotFoundException, IOException;
	public ChunkProperties requestForChunkTransfer(String chunkName, String fileName, ArrayList<String> visitedIPs , Set<String> workerIps) throws FileNotFoundException, IOException, NotBoundException;
}
