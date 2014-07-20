package generics;
import java.io.Serializable;
import java.util.ArrayList;


public class ChunkProperties implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String jobStatus; 
private String jobMachineHolder;
private ArrayList<String> CHUNK_IP_LIST;
private fakeDistributedFile fdf;
 
 public ChunkProperties(){
	 
 }

 public ChunkProperties(String jobStatus, String jobMachineHolder, ArrayList<String> CHUNK_IP_LIST, fakeDistributedFile fdf){
	 this.jobMachineHolder = jobMachineHolder;
	 this.jobStatus = jobStatus;
	 this.CHUNK_IP_LIST= CHUNK_IP_LIST;
	 this.setFdf(fdf);
 }

public String getJobStatus() {
	return jobStatus;
}

public void setJobStatus(String jobStatus) {
	this.jobStatus = jobStatus;
}

public String getJobMachineHolder() {
	return jobMachineHolder;
}

public void setJobMachineHolder(String jobMachineHolder) {
	this.jobMachineHolder = jobMachineHolder;
}

public ArrayList<String> getCHUNK_IP_LIST() {
	return CHUNK_IP_LIST;
}

public void setCHUNK_IP_LIST(ArrayList<String> cHUNK_IP_LIST) {
	CHUNK_IP_LIST = cHUNK_IP_LIST;
}

public fakeDistributedFile getFdf() {
	return fdf;
}

public void setFdf(fakeDistributedFile fdf) {
	this.fdf = fdf;
}
	
}