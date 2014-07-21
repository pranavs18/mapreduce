package worker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;


public class Mapper implements Serializable {


	private static final long serialVersionUID = 1L;
	protected Mapper mapResult;
	protected String fileChunkName;
	
	protected Mapper(String fileChunkName){
		this.fileChunkName = fileChunkName;
	}
	
	public Mapper(){
		
	}
	public void writeToFile(String key, String value)  {
		 String path = ".." + File.separator + "dfs" + File.separator + "intermediate";
		// System.out.println("Creating intermediate mapper output files at path...." + path);
		 File dir = new File(path);
			if (!dir.exists()) {
				boolean result = dir.mkdirs();

				if (result) {
					//System.out.println("Intermediate Map Folder is created");

				} 
			}
		 File outputFile = new File(path + File.separator + fileChunkName + "_mapper.txt");
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
	
}