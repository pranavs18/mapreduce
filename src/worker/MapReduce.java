package worker;

import generics.MapReduceConfiguration;
import generics.SlaveRemoteInterface;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collections;



public class MapReduce extends MapReduceConfiguration implements Serializable {


	private static final long serialVersionUID = 1L;
	protected MapReduce mapResult;
	protected String fileChunkName;
	protected String inputPath;
	protected String outputPath;
	protected int reducers;
	private int hashvalue;
	private String name;
	private String tail;
	private MapReduceConfiguration config;
	protected MapReduce(String fileChunkName, String inputPath , String outputPath, int reducers){
		this.fileChunkName = fileChunkName;
		this.inputPath= inputPath;
		this.reducers = reducers;
		this.outputPath = outputPath;
	}

	public MapReduce(){

	}

	MapReduce( String inputPath , String outputPath, int reducers, String tail, String name,MapReduceConfiguration config){
		this.tail = tail;
		this.outputPath = outputPath;
		this.inputPath= inputPath;
		this.reducers = reducers;
		this.name = name;
		this.config = config;
	}

	public void writeToFile(String key, String value)  {
		int index = inputPath.lastIndexOf("/");
		String parentFileName = inputPath.substring(index+1, inputPath.length()-4);

		String path = ".." + File.separator + "dfs" + File.separator + "intermediate" + File.separator + parentFileName;

		File dir = new File(path);
		if (!dir.exists()) {
			boolean result = dir.mkdirs();

			if (result) {
				System.out.println("Intermediate Map Folder is created");

			} 
		}
		hashvalue = createPartition(key, this.reducers);

		path = path + File.separator + fileChunkName + "_mapper" + "_" + hashvalue + ".txt";

		File outputFile = new File(path);
		FileWriter bw = null;
		try {

			bw = new FileWriter(outputFile,true);
			bw.write(key + " " + value + "\n");

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

	public void writeReducerOutput(String key, String value) throws IOException, NotBoundException{
		String path = this.outputPath;
		String newpath= this.outputPath;
		File dir = new File(path);
		if (!dir.exists()) {
			boolean result = dir.mkdirs();

			if (result) {
				System.out.println("Final output folder is created");

			} 
		}
		newpath = path;
		path = path + File.separator + "finalOutput" + "_"+name+"_"+tail+ ".txt";

		File outputFile = new File(path);
		FileWriter bw = null;
		try {

			bw = new FileWriter(outputFile,true);
			bw.write(key + " " + value + "\n");

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

	private static int createPartition(String Key, int totalNumberOfReducers){
		// From the data structures class, using the hashcode of the key is a good hash function as it is always unique
		// but can sometimes lead to negative values which we have handled here
		int code = Key.hashCode();
		int keyToReducerMap = code % totalNumberOfReducers;
		if(keyToReducerMap < 0)
			keyToReducerMap = (-1) * keyToReducerMap;
		return keyToReducerMap;
	}


}