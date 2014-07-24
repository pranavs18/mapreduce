package master;

import generics.ChunkProperties;
import generics.WorkerMessageToMaster;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterGlobalInformation {


	private static int maxMapperPerSystem = 4;
	private static int maxReducesPerSystem = 2;
	private static String nameNodeIp = "";

	private static AtomicInteger connectionNumber = new AtomicInteger(0);

	/* Count to give transfer success */
	private static AtomicInteger transferSuccessCount = new AtomicInteger(0);

	/* Count to give reducer chunk map */
	private static AtomicInteger reducerSuccessCount = new AtomicInteger(0);

	/* used to get worker information from all available workers and we would need to append the values here*/
	public static ConcurrentHashMap<String, WorkerMessageToMaster> allWorkerMapReduceDetails = new ConcurrentHashMap<String,WorkerMessageToMaster>();

	/* this map is obtained when a request is made to name node for all the chunk maps */
	public static ConcurrentHashMap<String, ChunkProperties> masterStaticChunkMap = new ConcurrentHashMap<String, ChunkProperties>();


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
	public static ConcurrentHashMap<String, ChunkProperties> getMasterStaticChunkMap() {
		return masterStaticChunkMap;
	}
	public static void setMasterStaticChunkMap(
			ConcurrentHashMap<String, ChunkProperties> masterStaticChunkMap) {
		MasterGlobalInformation.masterStaticChunkMap = masterStaticChunkMap;
	}

	public static int getIncreasedTransferSuccessCount(){

		return transferSuccessCount.addAndGet(1);

	}

	public static void resetTransferCount(){

		transferSuccessCount.set(0);

	}

	public static int getIncreasedReducerSuccessCount(){

		return reducerSuccessCount.addAndGet(1);

	}

	public static void resetReducerCount(){

		reducerSuccessCount.set(0);

	}


}
