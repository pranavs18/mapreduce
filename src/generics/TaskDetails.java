/******************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * TaskDetails Class Contains all information of a task (Map or reduce)
 * 
 ****************************************************************************************/

package generics;

import java.io.Serializable;

import master.JobStatus;


public class TaskDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String workerIp;
	String workerMapReduceId;
	String fileName;
	JobStatus status;
	
	public TaskDetails(String workerIp, String workerMapReduceId,
			String fileName, JobStatus status) {
		
		this.workerIp = workerIp;
		this.workerMapReduceId = workerMapReduceId;
		this.fileName = fileName;
		this.status = status;
	}
	
	
	public String getWorkerIp() {
		return workerIp;
	}
	public void setWorkerIp(String workerIp) {
		this.workerIp = workerIp;
	}
	
	public String getWorkerMapReduceId() {
		return workerMapReduceId;
	}
	
	public void setWorkerMapReduceId(String workerMapReduceId) {
		this.workerMapReduceId = workerMapReduceId;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public JobStatus getStatus() {
		return status;
	}
	
	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
	public void setAsDefault(){

		this.fileName = null;
		this.status = JobStatus.AVAILABLE;
		
	}
	
	

}
