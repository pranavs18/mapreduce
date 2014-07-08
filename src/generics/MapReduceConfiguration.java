package generics;

public class MapReduceConfiguration{
	 
	public static final int reducers = 3;
	public static final int chunk_size = 32768;
	public static final int jobsOnSlaveNode = 4;
	public static final int replicationFactor = 2;
	public static final int datanode_port = 9999;
	public static final String Master = "127.0.0.1";
	public static final String Slaves[] = {"127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1"};
	
		
}