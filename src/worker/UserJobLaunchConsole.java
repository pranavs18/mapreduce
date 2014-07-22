/*************
 * @author Pranav Saxena / Vaibhav Suresh Kumar
 * 
 * 
 */
package worker;

import generics.MapReduceConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class UserJobLaunchConsole{

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
	}

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
				do{
					System.out.println("\nEnter Job name");
					s = br.readLine();
					s = "Split";
					config.setJobName(s);
					if(s.equals("")){
						System.out.println("You need to enter a job name to proceed");
					}
				}while(s.equals(""));

				do{
					System.out.println("\nEnter path of your map reduce package \n/dfs/Enter_directory");
					s="";
					s = br.readLine();
					s = "/dfs/client";
					config.setUserPackagePath(".."+s);
					if(s.lastIndexOf(File.separator) == s.length()-1){
						s = s.substring(0, s.length() -1);
					}
					if(!s.contains(File.separator+"dfs")){
						System.out.println("Not in dfs directory. Please add a dfs path");
					}
					if(s.equals("")){
						System.out.println("Please enter a dfs path");
					}
				}while((!s.contains(File.separator+"dfs")) || s.equals(""));



				do{
					System.out.println("\nEnter input file Path  \n/dfs/Path_to_inputfile");
					System.out.println("You can ignore Ip if the file is in this system");
					s = "";
					s = br.readLine();
					s = "/dfs/Input/pranav.txt";  //remove
					if(s.lastIndexOf(File.separator) == s.length()-1){
						s = s.substring(0, s.length() -1);
					}

					/* we split and obtain the Input file Ip of path if blank it shall be
					 * assumed that the input file is in the localhost */
					if(s.indexOf(File.separator) == 0){
						config.setSplitIP(InetAddress.getLocalHost().getHostAddress());
						config.setInputPath(".."+s);
					}
					else{

						config.setSplitIP(s.substring(0,s.indexOf(File.separator)));
						config.setInputPath(".."+s.substring(s.indexOf(File.separator),s.length()));

					}
					if(!s.contains(File.separator+"dfs")){
						System.out.println("Not in dfs directory. Please add a dfs path");
					}
					if(s.equals("")){
						System.out.println("Please enter a dfs path");
					}
				}while((!s.contains(File.separator+"dfs")) || s.equals(""));

				do{
					System.out.println("\nEnter output file Path  \n(/dfs/Enter_directory");
					System.out.println("You can ignore Ip if the file is in this system");
					s = "";
					s = br.readLine();
					s = "/dfs/myOutputFolder";  //remove
					if(s.lastIndexOf(File.separator) == s.length()-1){
						s = s.substring(0, s.length() -1);
					}
					/* we split and obtain the Input file Ip of path if blank it shall be
					 * assumed that the input file is in the localhost */
					if(s.indexOf(File.separator) == 0){
						config.setOutPutIp(InetAddress.getLocalHost().getHostAddress());
						config.setOutputPath(".."+s);

					}
					else{

						config.setOutPutIp(s.substring(0,s.indexOf(File.separator)));
						config.setOutputPath(".."+s.substring(s.indexOf(File.separator),s.length()));

					}
					if(!s.contains(File.separator+"dfs")){
						System.out.println("Not in dfs directory. Please add a dfs path");
					}
					if(s.equals("")){
						System.out.println("Please enter a dfs path");
					}
				}while((!s.contains(File.separator+"dfs")) || s.equals(""));

				System.out.println("\nEnter Map class name");
				s = "";
				s = br.readLine();
				s= "client.WordCount";//remove
				config.setMapperClass(s);
				int indexOfLastDot = s.lastIndexOf(".");
				s = s.substring(0, indexOfLastDot);
				config.setUserProgramPackageName(s);

				System.out.println("\nEnter Reduce class name");
				s = "";
				s = br.readLine();
				s="client.WordCount"; //remove
				config.setReducerClass("client.WordCount");

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

				System.out.println("\nEnter Output value Type (String, Integer, UserDefined classes ...)");
				System.out.println("You can leave it blank to use default type Integer");
				s = "";
				s = br.readLine();
				if(!s.equals("")){
					config.setOutputValueType(s);
				}


				do{
					System.out.println("\nEnter number of reducers");
					System.out.println("You can leave it blank to use default value 2");


					s = "";
					s = br.readLine();
					if(s.equals("")){
						s="1";
					}
					if(UserJobLaunchConsole.isInteger(s) == true){
						config.setReducers(Integer.parseInt(s));
					}

					else{
						System.out.println("Please Enter an integer");
					}
				}while(UserJobLaunchConsole.isInteger(s) == false);
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