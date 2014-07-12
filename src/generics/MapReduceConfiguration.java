package generics;

public class MapReduceConfiguration{
	
	//Client Settings 
	
	/* number reducers can be set by the user 
	 * number of Mappers will depend on chunk 
	 * size and and will use all available
	 * Mappers.
	 */
	private int reducers = 1;
	
	/* These fields are used by master to to determine the Type of object */ 
	private Class<?> mapperClass = null;
	private Class<?> reducerClass = null;

	/* These fields are used to set the input key and Value Types
	 * Default is String
	 */
	private Class<?> inputKeyType = String.class;
	private Class<?> inputValueType = String.class;

	/* These fields are used to set the output key and Value Types
	 * Default is String
	 */
	private Class<?> outputKeyType = String.class;
	private Class<?> outputValueType = String.class;

	/* These fields are use to set the input and outPut Path in the 
	 * our folder which will be used as our dfs on each machine
	 */
	private String inputPath = "";
	private String outPath = "";
	
	/* This field is used to set the name of job */	
	private String jobName = "";

	
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
	
	public Class<?> getMapperClass() {
		return mapperClass;
	}
	
	public void setMapperClass(Class<?> mapperClass) {
		this.mapperClass = mapperClass;
	}
	
	public Class<?> getReducerClass() {
		return reducerClass;
	}
	
	public void setReducerClass(Class<?> reducerClass) {
		this.reducerClass = reducerClass;
	}
	
	public String getInputPath() {
		return inputPath;
	}
	
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	
	public String getOutPath() {
		return outPath;
	}
	
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	
	
		
}