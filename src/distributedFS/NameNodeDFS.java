/*****************************************************************************************
 * 
 * @author - Pranav Saxena/Vaibhav Suresh Kumar
 * 
 * NameNodeDFS class acts as the master for our own distributed file system and will be responsible
 * for dividing the file being read from the AFS into chunks with each chunk being assigned 
 * a unique identifier.
 * 
 ************************************************************************************************/

package distributedFS;

import java.util.concurrent.ConcurrentHashMap;

class ChunkMetaData {
	
	    private String fname;
	    private int filePointerOffset;
	    private int lengthOfFile;
	    private int chunkID;
	    
	  public ChunkMetaData(String fname, int offset, int len, int chunkID){
		  this.setFname(fname);
		  this.setfilePointerOffset(offset);
		  this.lengthOfFile = len;
		  this.setChunkID(chunkID);
	  }

	public int getChunkID() {
		return chunkID;
	}

	public void setChunkID(int chunkID) {
		this.chunkID = chunkID;
	}

	public int getOffset() {
		return filePointerOffset;
	}

	public void setfilePointerOffset(int offset) {
		this.filePointerOffset = offset;
	}

	public int getLengthOfFile() {
		return lengthOfFile;
	}

	public void setLengthOfFile(int lengthOfFile) {
		this.lengthOfFile = lengthOfFile;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	  
}

public class NameNodeDFS {
	
	private int chunkID = 0;  // unique chunk identifier
	// map for storing filename and chunk ID
	ConcurrentHashMap<String,String> chunkMap = new ConcurrentHashMap<String,String>();  
	//map for storing the host which contains
	// the chunks its local file system
	ConcurrentHashMap<Integer,String> hostContainer = new ConcurrentHashMap<Integer,String>();
  	
	public NameNodeDFS(){
		
	}
}