package worker;

import java.io.BufferedWriter;
import java.io.File;
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
		boolean result= false;
		int index=0;
		for(int i=filename.length()-1; i>=0;i--){
			if(filename.charAt(i) == '/'){
				    index = i;
					break;
		    }
		}
	
		String chunkFileName = filename.substring(index+1, filename.length()-4);
		String newChunkDirectory = filename.substring(0, index)+  "/chunks" + chunkFileName;
		
		File dir = new File(newChunkDirectory);
		if (!dir.exists()) {
            result = dir.mkdirs();

            if (result) {
                System.out.println("Folder is created");
                
            } 
        }
		String newChunkName = newChunkDirectory + "/" + chunkFileName  + chunkNumber + ".txt";
		int chunkID = chunkNumber;
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
				newChunkName = newChunkDirectory +"/" +chunkFileName + chunkNumber + ".txt" ;
				numberofLines = 0;
				  
		  }
		}
		return chunkContainer;
	}
}