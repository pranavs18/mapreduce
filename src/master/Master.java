/*************************************************************************************************
 * 
 * @author Pranav Saxena / Vaibhav Suresh Kumar
 * Master class 
 * Responsible for the following tasks - 
 * 1) Keep track of slave list 
 * 2) Migrate task from one slave to another
 * 3) Query each slave node
 * 4) Re-batch killed task to next available node ( fault-tolerant)
 * 5) Assign task to slave node 
 * 
 */
package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Master implements Runnable {

    private String Ipaddress;
    private int port;
    static int conn=0;
    public Master(String ipAddress, int port) {
		this.Ipaddress= ipAddress;
		this.port = port;
	}
    
    public void createConnection() throws IOException{
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(this.port);
		
		while ( true ) {
		    try {
			Socket clientSocket = ss.accept();
			conn++;
			slaveProcessConnection newconn = new slaveProcessConnection(clientSocket, conn, this);
			new Thread(newconn).start();
		    }   
		    catch (IOException e) {
			System.out.println(e);
		    }
	
		}
	}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		finally{
			ss.close();
		}
	}

    
	@Override
	public void run() {
		System.out.println("\n Master started \n");
		Master mNode = new Master(Ipaddress,port);
		try {
			mNode.createConnection();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	class slaveProcessConnection implements Runnable {
		  Master server;
		  Socket SOCK;
		  int id;
		  BufferedReader br = null;
	      PrintStream ps = null;
	      boolean done = false;
		  
	      HashMap<InetAddress,Integer> SocketTable = new HashMap<InetAddress,Integer>();
	      public slaveProcessConnection(Socket client, int id, Master pm) {
				this.SOCK = client;
				this.id = id;
				this.server = pm;
				System.out.println( "Connection " + id + " established with: " + SOCK );
				SocketTable.put(client.getInetAddress(), client.getPort());
			 //	mNode.ProcessTable.put(id,SocketTable);
			
				try {
				    br = new BufferedReader(new InputStreamReader(SOCK.getInputStream()));
				    ps = new PrintStream(SOCK.getOutputStream());
				} catch (IOException e) {
				    System.out.println(e);
				}
			}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	} 
	
}