package worker;

import generics.Archiver;
import generics.MapReduceConfiguration;
import generics.MapReduceStarterInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class MapReduceJobClient implements Runnable{


	MapReduceConfiguration config;



	public MapReduceJobClient(MapReduceConfiguration config) {
		this.config = config;
	}

	public static void runJob(MapReduceConfiguration config){

		/* This would be a client function that will use an rmi call and transfer the config object to master */
		if((config.getInputPath()).equals("")  ||(config.getOutputPath()).equals("") || 
				(config.getJobName()).equals("") || config.getMapperClass() == null || config.getReducerClass()==null){

			
			System.out.println(config.getInputPath() +" "+ config.getOutputPath() +" "+ 
			config.getJobName()+" "+config.getMapperClass()+" "+config.getReducerClass());
			System.out.println("Some of the parameters have not been provided Correctly. Please read the documentation"
					+ " to give inputs in the correct format");

		}
		else{
			
			/* Create a .jar file will all user class files for transfer */
			try {

				/* Remote call to master to start job */
				System.out.println("Master Ip: "+MasterInformation.getMasterHost());  //remove
				

				MapReduceStarterInterface jobStarter = (MapReduceStarterInterface)Naming.lookup("rmi://"+MasterInformation.getMasterHost()+":23390/launcher");		

				String splitIp = config.getSplitIP();

				Boolean status = jobStarter.StartJob(config,splitIp); 				
				System.out.println("Job "+config.getJobName()+" has Started");
				
				if(status == true){
					System.out.println("Job "+config.getJobName()+" has Completed");
				}
				else{
					System.out.println("Error occured while running Job: "+config.getJobName());
					System.out.println("Please check if all the given inputs are correct");
				}

			} catch (IOException e) {
				System.out.println("An unexpected error occured. Please try again");
			} catch (NotBoundException e) {
				System.out.println("Object was not found on lookup");
				e.printStackTrace();
			}
			catch(Exception e){
				System.out.println("Error occured while running Job: "+config.getJobName());
				System.out.println("Please check if all the given inputs are correct");
			}


		}

	}

	@Override
	public void run() {

		MapReduceJobClient.runJob(config);

	}


}