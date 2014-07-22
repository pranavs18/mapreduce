package worker;

import generics.MapReduceConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class Mapper extends MapReduceConfiguration implements Serializable {


	private static final long serialVersionUID = 1L;
	protected Mapper mapResult;
	protected String fileChunkName;
	protected String inputPath;
	protected int reducers;
	
	protected Mapper(String fileChunkName, String inputPath , int reducers){
		this.fileChunkName = fileChunkName;
		this.inputPath= inputPath;
		this.reducers = reducers;
	}
	
	public Mapper(){
		
	}
	
	public void writeToFile(String key, String value)  {
		 int index = inputPath.lastIndexOf("/");
		 String parentFileName = inputPath.substring(index+1, inputPath.length()-4);
		 
		 String path = ".." + File.separator + "dfs" + File.separator + "intermediate" + File.separator + parentFileName;
		 
		 
		// System.out.println("Creating intermediate mapper output files at path...." + path);
		 File dir = new File(path);
			if (!dir.exists()) {
				boolean result = dir.mkdirs();

				if (result) {
					//System.out.println("Intermediate Map Folder is created");

				} 
			}
		 int hashValue = createPartition(key, this.reducers);
				 
	     path = path + File.separator + fileChunkName + "_mapper" + "_" + hashValue + ".txt";
		 
		 File outputFile = new File(path);
		 FileWriter bw = null;
		 try {
			
			 bw = new FileWriter(outputFile,true);
			 bw.write(key + " " + value + "\n");
			//System.out.println("Mapper file created");
			 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static int createPartition(String Key, int totalNumberOfReducers){
		// From the data structures class, using the hashcode of the key is a good hash function as it is always unique
		// but can sometimes lead to negative values which we have handled here
		int code = Key.hashCode();
		int keyToReducerMap = code % totalNumberOfReducers;
		if(keyToReducerMap < 0)
			keyToReducerMap = (-1) * keyToReducerMap;
		return keyToReducerMap;
	}
	
	
}