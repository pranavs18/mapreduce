/******************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * WorkerTasksStatus: Contains the actual task maps and other common information like ID max Mappers allowed, 
 * max reduces allowed.
 *******************************************************************************************/

package worker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import master.JobStatus;
import master.TaskDetails;

public class WorkerTasksStatus {

	private static Map<String,TaskDetails> taskStatusMap = Collections.synchronizedMap(new HashMap<String, TaskDetails>());
	private static Map<String,TaskDetails> taskStatusReduce = Collections.synchronizedMap(new HashMap<String, TaskDetails>());
	private static int numberOfMaps;
	private static int numberOfReduces;
	private static int workerId;

	
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

     public static Map<String, TaskDetails> getTaskStatusMap() {
		return taskStatusMap;
	}

	public static Map<String, TaskDetails> getTaskStatusReduce() {
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
 
    	for(Map.Entry<String, TaskDetails> entry : taskStatusMap.entrySet()){
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
    	for(Map.Entry<String, TaskDetails> entry : taskStatusReduce.entrySet()){
    		if((entry.getValue().getStatus() == JobStatus.AVAILABLE) || entry.getValue().getStatus() == JobStatus.COMPLETE){
    			++availability;
    		}
    	}
    	if((taskStatusReduce.size()<=numberOfReduces) && availability>0){
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
    		String jobId = workerId+"_m"+(i+1);
    		TaskDetails taskDetails = new TaskDetails(InetAddress.getLocalHost().getHostAddress(), jobId, null, null, JobStatus.AVAILABLE);
    		putInStatusMap(jobId, taskDetails);
    	}
    	for(int i=0; i<numberOfReduces;i++){
    		String jobId = workerId+"_r"+(i+1);
    		TaskDetails taskDetails = new TaskDetails(InetAddress.getLocalHost().getHostAddress(), jobId, null, null, JobStatus.AVAILABLE);
    		putInStatusReduce(jobId, taskDetails);
    	}
    }
	
}
