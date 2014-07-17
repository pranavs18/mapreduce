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

			String mapperAbstractClass= config.getMapperClass().getSuperclass().getName();
			String reducerAbstractClass = config.getReducerClass().getSuperclass().getName();
			/* Check if the User Mapper and reducer classes extend the 
			 * Mapper abstract class and reducer abstract classes respectively
			 */

			//if(mapperAbstractClass!="" || reducerAbstractClass!=""){
			//	System.out.println("Please check if your mapper or reducer class extend have appropriate super classes");
			//}


			//else{
			/* Create a .jar file will all user class files for transfer */
			try {
//				Archiver jarMaker = new Archiver();
//				File directory = new File (config.getUserJavaFilePath());  
//				File newJarCreated = new File(config.getUserProgramPackageName()+".jar");
//				File[] filesInDirectory = directory.listFiles();
//				if (filesInDirectory != null) {
//
//					jarMaker.createJar(newJarCreated, filesInDirectory);
//
//				} else {
//					System.out.println("No files found");
//				}
//
//
//				/*  Make RMI call to master here with MapReduceConfiguration object as parameter
//				 *  Send the .class file in a jar to master
//				 */
//				byte[] JarFileByteArray = new byte[10240];
//				FileInputStream fis = new FileInputStream(newJarCreated);
//				while (true) {
//					int bytesRead = fis.read(JarFileByteArray, 0, JarFileByteArray.length);
//					if (bytesRead <= 0)
//						break;
//				}
				
				/* Remote call to master to start job */
				
				MapReduceStarterInterface jobStarter = (MapReduceStarterInterface)Naming.lookup("rmi://127.0.0.1:23390/launcher");		
				Boolean status = jobStarter.StartJob(config); 
				
				System.out.println("Job "+config.getJobName()+" has Started");
				
				if(status == true){
					System.out.println("Job "+config.getJobName()+" has Completed");
				}
				else{
					System.out.println("Error occured while running Job "+config.getJobName());
				}
				

			} catch (IOException e) {
				System.out.println("An unexpected error occured. Please try again");
				e.printStackTrace();
			} catch (NotBoundException e) {
				System.out.println("Object was not found on lookup");
				e.printStackTrace();
			}
			

			//}

		}

	}

	@Override
	public void run() {

		MapReduceJobClient.runJob(config);

	}


}