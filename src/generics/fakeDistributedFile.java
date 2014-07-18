/************************************************************************************************
 * 
 * @author Pranav Saxena / Vaibhav Suresh Kumar 
 * 
 * fakeDistrbutedFile - representing the properties of the chunks created out of a big file and being stored in different workers
 **********************************************************************************/

package generics; 

import java.io.Serializable;

public class fakeDistributedFile implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String filename;
	private String ipAddress;
	private String chunkID;
	private String chunkPath;
	private int replicas;
	
	public fakeDistributedFile(){	
	}
	
	public fakeDistributedFile(String filename, String ipAddress, String chunkID, String chunkPath){
		this.setFilename(filename);
		this.setIpAddress(ipAddress);
		this.setChunkID(chunkID);
		this.setChunkPath(chunkPath);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getChunkID() {
		return chunkID;
	}

	public void setChunkID(String uniquechunkName) {
		this.chunkID = uniquechunkName;
	}

	public String getChunkPath() {
		return chunkPath;
	}

	public void setChunkPath(String chunkPath) {
		this.chunkPath = chunkPath;
	}

	public int getReplicas() {
		return replicas;
	}

	public void setReplicas(int replicas) {
		this.replicas = replicas;
	}
	
}