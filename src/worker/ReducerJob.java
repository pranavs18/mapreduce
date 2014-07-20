package worker;

import java.util.ArrayList;


public class ReducerJob implements Runnable {

	private int workerID;
	private int jobID;
	private String inputFile;
	private String outputFile;
	private Reducer reducer;
	
	 public ReducerJob(Reducer reducer, String outputFile, int jobID, int workerID, int partitionNum, ArrayList<Integer> mapperJobIds) {
	       
	        this.reducer = reducer;
	        this.outputFile = outputFile;
            this.jobID = jobID;
	       
	    }
	
	@Override
	public void run() {
		
		
	}
	
	
	
}