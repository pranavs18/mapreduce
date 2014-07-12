 /*************
  * @author Pranav Saxena / Vaibhav Suresh Kumar
  * 
  * 
  */
package generics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import master.Master;

public class UserConsoleManager implements Runnable {

	public static void main(String args[]) throws NumberFormatException, IOException{
		System.out.println("The User Console will allow you to start map-reduce jobs, list running map-reduce jobs /");
		Master mnode = new Master("127.0.0.1",9999);
		new Thread(mnode).start();
		int n=0;
		while(true){
			System.out.println("Press \n 1 - Launch a map-reduce job \n 2 - Kill a map-reduce job \n 3 - List the map-reduce jobs \n 4 - Stop the master node");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			n = Integer.parseInt(br.readLine());
			switch(n){
			case 1:{
				MapReduceConfiguration config = new MapReduceConfiguration();
				/* Get all details and handle data
				
				/* Running Job */
				MapReduceJobClient.runJob(config);
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