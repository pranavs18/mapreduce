package worker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Mapper implements Serializable {


	private static final long serialVersionUID = 1L;
	protected Mapper mapResult;
	protected String fileChunkName;
	
	protected Mapper(String fileChunkName){
		this.fileChunkName = fileChunkName;
	}
	
	public void writeToFile(String key, String value)  {
		 String path = ".." + File.separator + "dfs" + File.separator + "intermediate";
		 File dir = new File(path);
			if (!dir.exists()) {
				boolean result = dir.mkdirs();

				if (result) {
					System.out.println("Intermediate Map Folder is created");

				} 
			}
		 File outputFile = new File(path + File.separator + fileChunkName + "_mapper.txt");
		 try {
			
			FileWriter bw = new FileWriter(outputFile);
			bw.write(key + " " + value + "\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}