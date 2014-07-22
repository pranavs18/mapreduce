package generics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public interface MasterToWorkerInterface extends Remote  {
	

	public Boolean launchMapJob(String fileChunkName, String mapperId, MapReduceConfiguration config) throws RemoteException, NotBoundException, FileNotFoundException, IOException;
	public void sortIntermediateFiles(MapReduceConfiguration config) throws IOException;
	public Boolean launchTransfer(MapReduceConfiguration config, ConcurrentHashMap<String, HashSet<String>> intermediateFileNameInfo) throws RemoteException, NotBoundException, FileNotFoundException, IOException;
	public boolean launchReduceJob(MapReduceConfiguration config) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException;

}