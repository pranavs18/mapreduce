package worker;


import generics.ChunkProperties;
import generics.MapReduceConfiguration;
import generics.fakeDistributedFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface SlaveRemoteInterface extends Remote {
	
	public ConcurrentHashMap<String, ChunkProperties> splitFileIntoChunks(String filename , MapReduceConfiguration config, Set<String> workerIps , String splitIp) throws RemoteException, FileNotFoundException, IOException;
	public ConcurrentHashMap<String, ArrayList<String>> transferChunks(String fileName, byte buffer[]) throws IOException;
	public void transferJar(String jarName, byte buffer[]) throws IOException;
	public boolean transferChunkOnRequest(String Name, byte[] buffer) throws IOException;
	public String transferChunktoSlave(String chunkName, String fileName, ArrayList<String> visitedIPs , Set<String> workerIps, String splitIp) throws FileNotFoundException, IOException, NotBoundException;
}

