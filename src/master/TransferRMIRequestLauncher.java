package master;

import generics.MapReduceConfiguration;
import generics.MasterToWorkerInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class TransferRMIRequestLauncher implements Runnable{

	ConcurrentHashMap<String, HashSet<String>> ipFileNameExtension = new ConcurrentHashMap<String, HashSet<String>>();
	MapReduceConfiguration config = null;
	String IP;
	public TransferRMIRequestLauncher(String iP,
			ConcurrentHashMap<String, HashSet<String>> ipFileNameExtension,
			MapReduceConfiguration config) {
		super();
		this.ipFileNameExtension = ipFileNameExtension;
		this.config = config;
		IP = iP;
	}





	@Override
	public void run() {
		MasterToWorkerInterface transferFiles = null;
		try {
			transferFiles = (MasterToWorkerInterface)Naming.lookup("rmi://"+IP+":9876/job");

			Boolean isJobLaunchSuccessful = transferFiles.launchTransfer(config, ipFileNameExtension);
			 if(isJobLaunchSuccessful == true){
				 MasterGlobalInformation.getIncreasedTransferSuccessCount();
			 }
			
		} catch (NotBoundException | IOException e) {
			e.printStackTrace();
		}

	}




}
