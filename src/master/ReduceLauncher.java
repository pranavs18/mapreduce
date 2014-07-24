package master;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import generics.MapReduceConfiguration;
import generics.MasterToWorkerInterface;

public class ReduceLauncher implements Runnable{


	MapReduceConfiguration config;
	String ip;

	public ReduceLauncher(MapReduceConfiguration config, String ip) {
		this.config = config;
		this.ip = ip;
	}


	public void launchReduce(){
		MasterToWorkerInterface launchReduce = null;
		try {
			launchReduce = (MasterToWorkerInterface)Naming.lookup("rmi://"+ip+":9876/job");

			Boolean isJobLaunchSuccessful = launchReduce.launchReduceJob(config);
			
			if(isJobLaunchSuccessful == true){
				System.out.println("Reducer is successful");
				MasterGlobalInformation.getIncreasedReducerSuccessCount();
			}

		} catch (NotBoundException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void run() {
		launchReduce();
	}

}
