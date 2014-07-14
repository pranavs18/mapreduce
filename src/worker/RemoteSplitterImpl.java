package worker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoteSplitterImpl extends UnicastRemoteObject implements SlaveRemoteInterface {
	
	
	private static final long serialVersionUID = 1L;

	public RemoteSplitterImpl()  throws RemoteException {
		
	}
	
	@SuppressWarnings("resource")
	public ArrayList<String> splitFileIntoChunks(String filename) throws RemoteException,IOException{
		ArrayList<String> chunkContainer = new ArrayList<String>();
		File file = new File(filename);
		int numberofLines = 0;
		int chunkSize = 25;
		int chunkNumber = 1;
		if(!file.exists())
			return chunkContainer;
		else
			System.out.println("\n File found");
		Scanner scanner = null;
	    
	    scanner = new Scanner(file);
		
		String chunkFileName = filename.substring(0, filename.length()-4);
		String newChunkName = chunkFileName + chunkNumber;
		BufferedWriter bw = null;
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
			bw = new BufferedWriter(new FileWriter(newChunkName, true));
		
			  numberofLines++;
			  try{
			  bw.write(line);
			  bw.newLine();
		      bw.flush();
			  }
			  catch(Exception e){
				  
			  }
			if(numberofLines == chunkSize){
				chunkNumber++;
				try {
					chunkContainer.add(newChunkName);
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				newChunkName = chunkFileName + chunkNumber;
				numberofLines = 0;
				  
		  }
		}
		return chunkContainer;
	}
}