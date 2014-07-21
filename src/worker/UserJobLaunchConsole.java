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
import java.net.InetAddress;

public class UserJobLaunchConsole{

	public static void main(String args[]) throws NumberFormatException, IOException{
		MasterInformation.setMasterHost(args[0]);
		System.out.println("The User Console will allow you to start map-reduce jobs, list running map-reduce jobs /");
		int n=0;
		while(true){
			System.out.println("Press \n 1 - Launch a map-reduce job \n 2 - Kill a map-reduce job \n 3 - List the map-reduce jobs \n 4 - Stop the master node");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			n = Integer.parseInt(br.readLine());
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
				config.setJobName(/*s*/"Split");

				System.out.println("\nEnter path of your map reduce package in dfs folder");
				s="";
				s = br.readLine();

				config.setUserJavaFilePath(/*s*/"C:/Users/PRANAV/Documents/mapreduce/src/client");


				System.out.println(/*s*/"\nEnter input file Path (Consider the dfs folder as root folder)");
				s = "";
				s = br.readLine();

				config.setInputPath(/*config.getDfsPath()+s*/"C:/Users/PRANAV/Documents/mapreduce/pranav.txt");

				System.out.println("Input path: "+config.getInputPath());

				System.out.println("\nEnter output file Path  (Consider the dfs folder as root folder)");
				s = "";
				s = br.readLine();
				config.setOutputPath(/*config.getDfsPath()+s*/"/hello");

				System.out.println("\nEnter Map class name");
				s = "";
				s = br.readLine();
				config.setMapperClass(/*s*/"client.WordCount");
				int indexOfLastDot = s.lastIndexOf(".");
				//s = s.substring(0, indexOfLastDot);
				config.setUserProgramPackageName(/*s*/"client");

				System.out.println("\nEnter Reduce class name");
				s = "";
				s = br.readLine();
				config.setReducerClass(/*s*/"client.WordCount");

				System.out.println("\nEnter Input key Type (Integer, UserDefined classes ...)");
				System.out.println("You can leave it blank to use default type String");
				s = "";
				s = br.readLine();
				if(!s.equals("")){
					config.setInputKeyType(s);
				}

				System.out.println("\nEnter Input value Type (String, Integer, UserDefined classes ...)");
				System.out.println("You can leave it blank to use default type Integer");
				s = "";
				s = br.readLine();
				if(!s.equals("")){
					config.setInputValueType(s);
				}

				System.out.println("\nEnter Output key Type (String, Integer, UserDefined classes ...)");
				System.out.println("You can leave it blank to use default type String");
				s = "";
				s = br.readLine();
				if(!s.equals("")){
					config.setOutputKeyType(s);
				}

				System.out.println("\nEnter Output valur Type (String, Integer, UserDefined classes ...)");
				System.out.println("You can leave it blank to use default type Integer");
				s = "";
				s = br.readLine();
				if(!s.equals("")){
					config.setOutputValueType(s);
				}


				System.out.println("\nEnter number of reducers");
				System.out.println("You can leave it blank to use default value 1");

				s = "";
				s = br.readLine();
				if(s.equals("")){
					s="1";
				}
				config.setReducers(Integer.parseInt(s));

                 
				config.setWorkerIpForSplit(InetAddress.getLocalHost().getHostAddress());
                config.setSplitIP("128.237.186.178");
				MapReduceJobClient newJob = new MapReduceJobClient(config);
				/* Running Job in a new Thread */
				Thread jobThread = new Thread(newJob); 
				jobThread.start();

			}

			case 2:{

			}

			case 3:{

			}
			}
		}

	}

}