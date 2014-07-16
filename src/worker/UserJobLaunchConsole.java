/*************
 * @author Pranav Saxena / Vaibhav Suresh Kumar
 * 
 * 
 */
package worker;

import generics.MapReduceConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserJobLaunchConsole implements Runnable {

	public static void main(String args[]) throws NumberFormatException, IOException{
		System.out.println("The User Console will allow you to start map-reduce jobs, list running map-reduce jobs /");
		int n=0;
		while(true){
			System.out.println("Press \n 1 - Launch a map-reduce job \n 2 - Kill a map-reduce job \n 3 - List the map-reduce jobs \n 4 - Stop the master node");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String s = "";
			switch(n){
			case 1:{
				/* User would need to enter the required details to run a Job. The classes entered need
				 * to be precompiled and must exist in the classpath
				 */
				MapReduceConfiguration config = new MapReduceConfiguration();

				/* Ask and set job details */
				System.out.println("\nEnter Job name");
				s = br.readLine();
				config.setJobName(s);
				
				System.out.println("\nEnter absolute path of your map reduce package");
				s = br.readLine();
				config.setUserJavaFilePath(s);
				
				System.out.println("\nEnter input file Path");
				s = "";
				s = br.readLine();
				config.setInputPath("/dfs"+s);

				System.out.println("\nEnter output file Path");
				s = "";
				s = br.readLine();
				config.setInputPath("/dfs"+s);


				try{

					System.out.println("\nEnter Map class name");
					s = "";
					s = br.readLine();
					config.setMapperClass(Class.forName(s));
					int indexOfLastDot = s.lastIndexOf("\\.");
					s = s.substring(0, indexOfLastDot-1);
					config.setUserProgramPackageName(s);
					
					System.out.println("Package Name: "+s);  //remove

					System.out.println("\nEnter Reduce class name");
					s = "";
					s = br.readLine();
					config.setReducerClass(Class.forName(s));

					System.out.println("\nEnter Input key Type (Integer, UserDefined classes ...)");
					System.out.println("You can leave it blank to use default type String");
					s = "";
					s = br.readLine();
					if(!s.equals("")){
					config.setInputKeyType(Class.forName(s));
					}
					
					System.out.println("\nEnter Input value Type (String, Integer, UserDefined classes ...)");
					System.out.println("You can leave it blank to use default type Integer");
					s = "";
					s = br.readLine();
					if(!s.equals("")){
					config.setInputValueType(Class.forName(s));
					}

					System.out.println("\nEnter Output key Type (String, Integer, UserDefined classes ...)");
					System.out.println("You can leave it blank to use default type String");
					s = "";
					s = br.readLine();
					if(!s.equals("")){
					config.setOutputKeyType(Class.forName(s));
					}
					
					System.out.println("\nEnter Output valur Type (String, Integer, UserDefined classes ...)");
					System.out.println("You can leave it blank to use default type Integer");
					s = "";
					s = br.readLine();
					if(!s.equals("")){
					config.setOutputValueType(Class.forName(s));
					}
					
					MapReduceJobClient newJob = new MapReduceJobClient(config);
					/* Running Job in a new Thread */
					Thread jobThread = new Thread(newJob);
					jobThread.start();

				}
				catch(ClassNotFoundException e){

					System.out.println("\nA class that was entered as input was not found."
							+ "\n 1. Please ensure that you have compiled all the required classes"
							+ "\n 2. Please ensure that you have entered the correct name of the classes");

				}
				

			}

			case 2:{

			}

			case 3:{

			}
			}
		}

	}

	@Override
	public void run() {


	}


}