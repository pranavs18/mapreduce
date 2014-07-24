package generics;

import java.io.Serializable;


public class MapReduceConfiguration implements Serializable{
	
	//Client Settings 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* number reducers can be set by the user 
	 * number of Mappers will depend on chunk 
	 * size and and will use all available
	 * Mappers.
	 */
	private int reducers = 1;

	/* These fields are used by master to to determine the Type of object */ 
	private String mapperClass = null;
	private String reducerClass = null;

	
	/* These fields are used to set the input key and Value Types
	 * Default is String
	 */
	private String inputKeyType = "String.class";
	private String inputValueType = "String.class";

	/* These fields are used to set the output key and Value Types
	 * Default is String
	 */
	private String outputKeyType = "String.class";
	private String outputValueType = "String.class";

	/* These fields are use to set the input and outPut Path in the 
	 * our folder which will be used as our dfs on each machine
	 */
	private String inputPath = "";
	private String outputPath = "";
	
	/* This field is used to set the name of job */	
	private String jobName = "";
	
	/* Use to store package name that contains the user defined map reduce files */
	private String userProgramPackageName = "";

	/* Path of user defined functions */
	private String userPackagePath = "";
	
	/* Master Ip */
	 private String MasterIp;
	
	 /* Worker Ip that contains the file to be split */
	 private String splitIP;
	 
	 /*Worker Ip on which the output is to be saved */
	 private String outPutIp;
	 
	
	
	/* Getters and setter of fields above */
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getReducers() {
		return reducers;
	}
	
	public void setReducers(int reducers) {
		this.reducers = reducers;
	}
	
	public String getInputPath() {
		return inputPath;
	}
	
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getUserProgramPackageName() {
		return userProgramPackageName;
	}

	public void setUserProgramPackageName(String userProgramPackageName) {
		this.userProgramPackageName = userProgramPackageName;
	}

	

	public String getUserPackagePath() {
		return userPackagePath;
	}

	public void setUserPackagePath(String userPackagePath) {
		this.userPackagePath = userPackagePath;
	}

	public String getMasterIp() {
		return MasterIp;
	}

	public void setMasterIp(String masterIp) {
		MasterIp = masterIp;
	}


	public String getOutPutIp() {
		return outPutIp;
	}

	public void setOutPutIp(String outPutIp) {
		this.outPutIp = outPutIp;
	}

	public String getMapperClass() {
		return mapperClass;
	}

	public void setMapperClass(String mapperClass) {
		this.mapperClass = mapperClass;
	}

	public String getReducerClass() {
		return reducerClass;
	}

	public void setReducerClass(String reducerClass) {
		this.reducerClass = reducerClass;
	}

	public String getInputKeyType() {
		return inputKeyType;
	}

	public void setInputKeyType(String inputKeyType) {
		this.inputKeyType = inputKeyType;
	}

	public String getInputValueType() {
		return inputValueType;
	}

	public void setInputValueType(String inputValueType) {
		this.inputValueType = inputValueType;
	}

	public String getOutputKeyType() {
		return outputKeyType;
	}

	public void setOutputKeyType(String outputKeyType) {
		this.outputKeyType = outputKeyType;
	}

	public String getOutputValueType() {
		return outputValueType;
	}

	public void setOutputValueType(String outputValueType) {
		this.outputValueType = outputValueType;
	}

	public String getSplitIP() {
		return splitIP;
	}

	public void setSplitIP(String splitIP) {
		this.splitIP = splitIP;
	}
	

		
}