package master;

import generics.ChunkProperties;
import generics.MasterToNameNodeInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Set;

public class MasterRequestForTransfer implements Runnable{


	String chunkName;
	String fileName;
	ArrayList<String> visitedIPs; 
	Set<String> workerIps;
	MasterToNameNodeInterface fileChunkMapRequest;
	
	String splitIp;

	public MasterRequestForTransfer(MasterToNameNodeInterface fileChunkMapRequest,String chunkName, String fileName, ArrayList<String> visitedIPs , Set<String> workerIps, String splitIp){
		this.chunkName = chunkName;
		this.fileName = fileName;
		this.visitedIPs = visitedIPs;
		this.workerIps = workerIps;
		this.fileChunkMapRequest = fileChunkMapRequest;
		this.splitIp = splitIp;
	}

	public void run(){
		ChunkProperties chunk = null;
		try {
			chunk = fileChunkMapRequest.requestForChunkTransfer(chunkName, fileName, visitedIPs, workerIps , splitIp);
		} catch (IOException | NotBoundException e) {
			e.printStackTrace();
		}

		MasterGlobalInformation.getMasterStaticChunkMap().put(chunkName,chunk);
		
	}


}
