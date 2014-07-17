package master;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Master {

	public static void main(String args[]) {

		if(args.length == 2 || args.length == 0){
			if(args.length == 2){
				MasterGlobalInformation.setMaxMapperPerSystem(Integer.parseInt(args[0]));
				MasterGlobalInformation.setMaxReducesPerSystem(Integer.parseInt(args[1]));

			}

			Registry registry;
			try {
				registry = LocateRegistry.createRegistry(23390);
				StartMapReduceJob startJob = new StartMapReduceJob();
				registry.rebind("launcher", startJob);
				System.out.println("object bounded " + startJob);
			} catch (RemoteException e) {
				System.out.println("Could not bind Objects to regstry");
			}
			

			MasterHeartBeatReceiver hearbeatReceiver = new MasterHeartBeatReceiver();
			new Thread(hearbeatReceiver).start();
		}
		
		else{
			System.out.println("There should be either 2 arguments for max mappers and reduces "
					+ "or no arguments to use default values");
		}
		


	}

}
