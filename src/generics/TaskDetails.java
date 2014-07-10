/******************************************************************************************
 * 
 * @author - Pranav Saxena/ Vaibhav Suresh Kumar
 * 
 * TaskDetails Class Contains all information of a task (Map or reduce)
 * 
 ****************************************************************************************/

package generics;

public class TaskDetails {

	String workerIp;
	String workerMapReduceId;
	String fileName;
	String localFilePath;
	JobStatus status;
	
	public TaskDetails(String workerIp, String workerMapReduceId,
			String fileName, String localFilePath, JobStatus status) {
		super();
		this.workerIp = workerIp;
		this.workerMapReduceId = workerMapReduceId;
		this.fileName = fileName;
		this.localFilePath = localFilePath;
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
	
	public String getLocalFilePath() {
		return localFilePath;
	}
	
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	
	public JobStatus getStatus() {
		return status;
	}
	
	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
	public void setAsDefault(){

		this.fileName = null;
		this.localFilePath = null;
		this.status = JobStatus.AVAILABLE;
		
	}
	
	

}
