package master;

import generics.WorkerMessageToMaster;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterGlobalInformation {

	
	private static int maxMapperPerSystem = 4;
	private static int maxReducesPerSystem = 2;
	private static String nameNodeIp = "";
	private static AtomicInteger connectionNumber = new AtomicInteger(0);
	public static ConcurrentHashMap<String, WorkerMessageToMaster> allWorkerMapReduceDetails = new ConcurrentHashMap<String,WorkerMessageToMaster>();
	
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
	public static ConcurrentHashMap<String, WorkerMessageToMaster> getAllWorkerMapReduceDetails() {
		return allWorkerMapReduceDetails;
	}
	public static void setAllWorkerMapReduceDetails(
			ConcurrentHashMap<String, WorkerMessageToMaster> allWorkerMapReduceDetails) {
		MasterGlobalInformation.allWorkerMapReduceDetails = allWorkerMapReduceDetails;
	}
}
