/***************************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * WorkerMessageToMaster class is the message class whose object is sent to master with task detail maps
 * The same class must be present in the master host 
 * 
 ****************************************************************************************************/

package master;

import java.io.Serializable;
import java.util.Map;

public class WorkerMessageToMaster implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<String, TaskDetails> mapStatus;
	private Map<String, TaskDetails> reduceStatus;
	
	public WorkerMessageToMaster(Map<String, TaskDetails> mapStatus,
			Map<String, TaskDetails> reduceStatus) {
		this.mapStatus = mapStatus;
		this.reduceStatus = reduceStatus;
	}

	public Map<String, TaskDetails> getMapStatus() {
		return mapStatus;
	}

	public void setMapStatus(Map<String, TaskDetails> mapStatus) {
		this.mapStatus = mapStatus;
	}

	public Map<String, TaskDetails> getReduceStatus() {
		return reduceStatus;
	}

	public void setReduceStatus(Map<String, TaskDetails> reduceStatus) {
		this.reduceStatus = reduceStatus;
	}	

}
