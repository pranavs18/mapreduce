/***************************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * WorkerMessageToMaster class is the message class whose object is sent to master with task detail maps
 * The same class must be present in the master host 
 * 
 ****************************************************************************************************/

package generics;

import java.io.Serializable; 
import java.util.concurrent.ConcurrentHashMap;
import generics.TaskDetails;

public class WorkerMessageToMaster implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ConcurrentHashMap<String, TaskDetails> mapStatus;
	private ConcurrentHashMap<String, TaskDetails> reduceStatus;
	private Boolean mapFull = false;
	private Boolean reduceFull = false;
		
	public WorkerMessageToMaster(ConcurrentHashMap<String, TaskDetails> mapStatus,
			ConcurrentHashMap<String, TaskDetails> reduceStatus, Boolean mapFull, Boolean reduceFull) {
		super();
		this.mapStatus = mapStatus;
		this.reduceStatus = reduceStatus;
		this.mapFull = mapFull;
		this.reduceFull = reduceFull;
	}

	public ConcurrentHashMap<String, TaskDetails> getMapStatus() {
		return mapStatus;
	}

	public void setMapStatus(ConcurrentHashMap<String, TaskDetails> mapStatus) {
		this.mapStatus = mapStatus;
	}

	public ConcurrentHashMap<String, TaskDetails> getReduceStatus() {
		return reduceStatus;
	}

	public void setReduceStatus(ConcurrentHashMap<String, TaskDetails> reduceStatus) {
		this.reduceStatus = reduceStatus;
	}

	public Boolean getMapFull() {
		return mapFull;
	}

	public void setMapFull(Boolean mapFull) {
		this.mapFull = mapFull;
	}

	public Boolean getReduceFull() {
		return reduceFull;
	}

	public void setReduceFull(Boolean reduceFull) {
		this.reduceFull = reduceFull;
	}
	
}
