package master;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Master {

	public static void main(String[] args) {

		if(args[0].equals("")){
			System.out.println("Please enter Ip of string");

		}

		else{
			if(args.length == 3){
				MasterGlobalInformation.setMaxMapperPerSystem(Integer.parseInt(args[1]));
				MasterGlobalInformation.setMaxReducesPerSystem(Integer.parseInt(args[2]));

			}

			Registry registry;
			try {
				registry = LocateRegistry.createRegistry(23390);
				StartMapReduceJob startJob = new StartMapReduceJob(args[0]);
				registry.rebind("launcher", startJob);
			} catch (RemoteException e) {
				System.out.println("Could not bind Objects to regstry");
			}
			

			MasterHeartBeatReceiver hearbeatReceiver = new MasterHeartBeatReceiver();
			new Thread(hearbeatReceiver).start();

		}


	}

}
