/******************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * DataNodeDFS class represents the slave nodes which are responsible for storing chunks and reporting
 * all the chunks to the NameNode on startup.
 * 
 */
package distributedFS;

import java.util.concurrent.ConcurrentHashMap;

public class DataNodeDFS{
	
	private int slaveMachineID;
	public static ConcurrentHashMap<Integer,String> chunkMap = new ConcurrentHashMap<Integer, String>();
	
}