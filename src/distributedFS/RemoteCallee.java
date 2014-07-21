package distributedFS;

import generics.ChunkProperties;
import generics.MapReduceConfiguration;
import generics.MasterToNameNodeInterface;
import generics.fakeDistributedFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import worker.SlaveRemoteInterface;

public class RemoteCallee extends UnicastRemoteObject implements MasterToNameNodeInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected RemoteCallee() throws RemoteException {
		
	}

	public static ConcurrentHashMap<String,ArrayList<fakeDistributedFile>> FiletoChunkList = new ConcurrentHashMap<String,ArrayList<fakeDistributedFile>>();
	public static ConcurrentHashMap<String,ConcurrentHashMap<String, ChunkProperties>> GlobalFileMap = new ConcurrentHashMap<String,ConcurrentHashMap<String, ChunkProperties>>();

	private static SlaveRemoteInterface remote;
	

	public  ConcurrentHashMap<String, ChunkProperties> sendChunkMap(MapReduceConfiguration config, Set<String> workerIps, String splitIp) throws RemoteException, NotBoundException, FileNotFoundException, IOException{
		
		if(GlobalFileMap.contains(config.getInputPath())){
			return GlobalFileMap.get(config.getInputPath());
		}
		System.out.println("Set: "+ workerIps);
		ArrayList<fakeDistributedFile> al = new ArrayList<fakeDistributedFile>();
		ConcurrentHashMap<String, ChunkProperties> cp = new ConcurrentHashMap<String, ChunkProperties>();
		System.out.println("worker IP: "+splitIp);
		remote = (SlaveRemoteInterface) Naming.lookup("rmi://"+splitIp+":9876/Remote");
		cp = remote.splitFileIntoChunks(config.getInputPath(), config, workerIps, splitIp);
		for(String s:cp.keySet()){
			System.out.println("Block Created with name " + s);
		}
		FiletoChunkList.put(config.getInputPath(), al);
         
		GlobalFileMap.put(config.getInputPath(), cp);
		return cp;
  }
	
	public ChunkProperties requestForChunkTransfer(String newChunkName,String fileName, ArrayList<String> visitedIPs, Set<String> workerIps) throws IOException, NotBoundException {
		 
	    String ipAddresstoTransfer = null;
	   
		for(String v:visitedIPs){
			for(String w:workerIps ){
				if(!v.equals(w)){
					ipAddresstoTransfer = w;
					break;
				}
			}
		}
		if(ipAddresstoTransfer == null){
			ipAddresstoTransfer = visitedIPs.get(0);
		    System.out.println("Transferred machine IP" + ipAddresstoTransfer);
		}
		
		
        String path = ".." + File.separator + "dfs" + File.separator + "chunks";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        File file = null;
        for(File f:listOfFiles){
        	if(f.getName().equals(newChunkName)){
        		System.out.println("\n File to be transferred found in the DFS...\n Transferring now.... ");
        		file = new File(newChunkName);
        		break;
        	}
        }
        
        byte buffer[] = new byte[(int)file.length()];
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(path+File.separator+newChunkName));
		input.read(buffer,0,buffer.length);
		input.close();  	
		SlaveRemoteInterface obj = null;
		obj = (SlaveRemoteInterface) Naming.lookup("//"+ ipAddresstoTransfer +":9876/Remote");
		boolean flag = obj.transferChunkOnRequest(newChunkName, buffer);
		if(flag == true){
			if(!GlobalFileMap.get(fileName).get(newChunkName).getCHUNK_IP_LIST().get(0).equals(ipAddresstoTransfer))
				GlobalFileMap.get(fileName).get(newChunkName).getCHUNK_IP_LIST().add(ipAddresstoTransfer);
		}
	return GlobalFileMap.get(fileName).get(newChunkName);
}

	
}