/******************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * WorkerTasksStatus: Contains the actual task maps and other common information like ID max Mappers allowed, 
 * max reduces allowed.
 *******************************************************************************************/

package worker;

import generics.TaskDetails;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import master.JobStatus;

public class WorkerTasksStatus {

	private static ConcurrentHashMap<String,TaskDetails> taskStatusMap = new ConcurrentHashMap<String, TaskDetails>();
	private static ConcurrentHashMap<String,TaskDetails> taskStatusReduce = new ConcurrentHashMap<String, TaskDetails>();
	private static int numberOfMaps = 4;
	private static int numberOfReduces = 2;
	private static int workerId;
	private static AtomicInteger numberOfMapsCurrentlyRunning = new AtomicInteger(0);
	private static AtomicInteger numberOfReducesCurrentlyRunning = new AtomicInteger(0);
	
	private static Boolean mapFull = false;
	private static Boolean reduceFull = false;

	
	public WorkerTasksStatus() {

	}
	
	public WorkerTasksStatus(int workerId,int numberOfMaps, int numberOfReduces) {
		WorkerTasksStatus.numberOfMaps = numberOfMaps;
		WorkerTasksStatus.numberOfReduces = numberOfReduces;
		WorkerTasksStatus.workerId = workerId;
	}

	/* The key here is a String with job Id eg. 1_m1 or 2_r2 etc 
	 * The count of this hashmap will not exceed the mappers and reducers per
	 * system as described in the config file
	 */ 

     public static ConcurrentHashMap<String, TaskDetails> getTaskStatusMap() {
		return taskStatusMap;
	}

	public static ConcurrentHashMap<String, TaskDetails> getTaskStatusReduce() {
		return taskStatusReduce;
	}
	
	
	
	public static int getWorkerId() {
		return workerId;
	}

	/* Checks for correctness of query and adds to map (Assumption: Master will
	 * only send a job if a mapper is in available status)*/
    public static String putInStatusMap(String jobId, TaskDetails taskDetails){
    	int availability = 0;
    	String returnValue = "";
 
    	for(ConcurrentHashMap.Entry<String, TaskDetails> entry : taskStatusMap.entrySet()){
    		if((entry.getValue().getStatus() == JobStatus.AVAILABLE) || entry.getValue().getStatus() == JobStatus.COMPLETE){
    			++availability;
    		}
    	}
    	if((taskStatusMap.size()<=numberOfMaps) || availability>0){

    		taskStatusMap.put(jobId, taskDetails);
    		returnValue = "success";
    	}
    	else{
    		returnValue = "mapfullcapacity";
    	}
		return returnValue;
    }
	
    public static TaskDetails getInStatusMap(String jobId){
    	 return taskStatusMap.get(jobId);
    }
    
    /* Checks for correctness of query and adds to map (Assumption: Master will
	 * only send a job if a reduce is in available status)*/
    public static String putInStatusReduce(String jobId, TaskDetails taskDetails){
    	int availability = 0;
    	String returnValue = "";
 
    	for(ConcurrentHashMap.Entry<String, TaskDetails> entry : taskStatusReduce.entrySet()){
    		if((entry.getValue().getStatus() == JobStatus.AVAILABLE) || entry.getValue().getStatus() == JobStatus.COMPLETE){
    			++availability;
    		}
    	}
    	if((taskStatusReduce.size()<=numberOfReduces) || availability>0){

    		taskStatusReduce.put(jobId, taskDetails);
    		returnValue = "success";
    	}
    	else{
    		returnValue = "reducefullcapacity";
    	}
		return returnValue;
    }
    
    public static TaskDetails getInStatusReduce(String jobId){
   	 return taskStatusReduce.get(jobId);
   }
    
    /* Creates an initial map with default values
     * Warning: This is to be used only during worker node startup. It can potentially destroy the maps
     * if called later.
     */
    public static void initialTaskMapCreator() throws UnknownHostException{
    	for(int i=0; i<numberOfMaps;i++){
    		String jobId = InetAddress.getLocalHost().getHostAddress()+"_m"+(i+1);
    		TaskDetails taskDetails = new TaskDetails(InetAddress.getLocalHost().getHostAddress(), jobId,  null, JobStatus.AVAILABLE);
    		putInStatusMap(jobId, taskDetails);
    	}
    	for(int i=0; i<numberOfReduces;i++){
    		String jobId = InetAddress.getLocalHost().getHostAddress()+"_r"+(i+1);
    		TaskDetails taskDetails = new TaskDetails(InetAddress.getLocalHost().getHostAddress(), jobId,  null, JobStatus.AVAILABLE);
    		putInStatusReduce(jobId, taskDetails);
    	}
    }

	public static int getNumberOfMaps() {
		return numberOfMaps;
	}

	public static int getNumberOfReduces() {
		return numberOfReduces;
	}
    
	
	public static int getIncreasedMapperNumber() {
		return numberOfMapsCurrentlyRunning.addAndGet(1);
	}
	public static int getDecreasedMapperNumber() {
		return numberOfMapsCurrentlyRunning.decrementAndGet();
	}
	
	public static int getIncreasedReducerNumber() {
		return numberOfReducesCurrentlyRunning.addAndGet(1);
	}
	public static int getDecreasedReducerNumber() {
		return numberOfReducesCurrentlyRunning.decrementAndGet();
	}

	public static Boolean getMapFull() {
		return mapFull;
	}

	public static void setMapFull(Boolean mapFull) {
		WorkerTasksStatus.mapFull = mapFull;
	}

	public static Boolean getReduceFull() {
		return reduceFull;
	}

	public static void setReduceFull(Boolean reduceFull) {
		WorkerTasksStatus.reduceFull = reduceFull;
	}

	public static AtomicInteger getNumberOfMapsCurrentlyRunning() {
		return numberOfMapsCurrentlyRunning;
	}


	public static AtomicInteger getNumberOfReducesCurrentlyRunning() {
		return numberOfReducesCurrentlyRunning;
	}

	
}
