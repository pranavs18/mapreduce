package master;

import generics.TaskDetails;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterGlobalInformation {

	
	private static int maxMapperPerSystem = 4;
	private static int maxReducesPerSystem = 2;
	private static String nameNodeIp = "";
	private static AtomicInteger connectionNumber = new AtomicInteger(0);
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>> allMapTaskDetails = new ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>>();
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>> allReduceTaskDetails = new ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>>();
	
	
	
	public static int getMaxMapperPerSystem() {
		return maxMapperPerSystem;
	}
	public static void setMaxMapperPerSystem(int maxMapperPerSystem) {
		MasterGlobalInformation.maxMapperPerSystem = maxMapperPerSystem;
	}
	public static int getMaxReducesPerSystem() {
		return maxReducesPerSystem;
	}
	public static void setMaxReducesPerSystem(int maxReducesPerSystem) {
		MasterGlobalInformation.maxReducesPerSystem = maxReducesPerSystem;
	}
	public static String getNameNodeIp() {
		return nameNodeIp;
	}
	public static void setNameNodeIp(String nameNodeIp) {
		MasterGlobalInformation.nameNodeIp = nameNodeIp;
	}
	public static int getConnectionNumber() {
		return connectionNumber.addAndGet(1);
	}
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>> getAllMapTaskDetails() {
		return allMapTaskDetails;
	}
	public static void setAllMapTaskDetails(
			ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>> allMapTaskDetails) {
		MasterGlobalInformation.allMapTaskDetails = allMapTaskDetails;
	}
	
	public static void setAllReduceTaskDetails(ConcurrentHashMap<String, ConcurrentHashMap<String, TaskDetails>> allReduceTaskDetails){
		MasterGlobalInformation.allReduceTaskDetails = allReduceTaskDetails;
	}
	
	public static ConcurrentHashMap<String,	ConcurrentHashMap<String, TaskDetails>> getAllReduceTaskDetails(){
		return allReduceTaskDetails;
		
	}
	
	

}
