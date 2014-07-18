package worker;

import generics.Archiver;
import generics.MapReduceConfiguration;
import generics.fakeDistributedFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteSplitterImpl extends UnicastRemoteObject implements SlaveRemoteInterface {
	
	
	private static final long serialVersionUID = 1L;

	public RemoteSplitterImpl() throws RemoteException {
		
	}
	
	@SuppressWarnings("resource")
	public ArrayList<fakeDistributedFile> splitFileIntoChunks(String filename , MapReduceConfiguration config) throws RemoteException,IOException{
		ArrayList<fakeDistributedFile> chunkContainer = new ArrayList<fakeDistributedFile>();
		
		
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
		String uniquechunkName = chunkFileName + chunkNumber;
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
					fakeDistributedFile fdf = new fakeDistributedFile();
					fdf.setIpAddress(InetAddress.getLocalHost().getHostAddress());
					fdf.setFilename(filename);
					fdf.setReplicas(0);
					fdf.setChunkID(uniquechunkName);
					fdf.setChunkPath(newChunkName);
					chunkContainer.add(fdf);
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				uniquechunkName = chunkFileName + chunkNumber;
				newChunkName = newChunkDirectory +"/" +chunkFileName + chunkNumber + ".txt" ;
				numberofLines = 0;
				  
		  }
			transferFileChunks(newChunkDirectory);
		}
// jar archive logic 
		
		
		Archiver jarMaker = new Archiver();
		File directory = new File (config.getUserJavaFilePath());  
		File newJarCreated = new File(config.getUserProgramPackageName()+".jar");
		File[] filesInDirectory = directory.listFiles();
		if (filesInDirectory != null) {

			jarMaker.createJar(newJarCreated, filesInDirectory);

		} else {
			System.out.println("No files found");
		}


		/*  Make RMI call to master here with MapReduceConfiguration object as parameter
		 *  Send the .class file in a jar to master
		 */
		byte[] JarFileByteArray = new byte[10240];
		FileInputStream fis = new FileInputStream(newJarCreated);
		while (true) {
			int bytesRead = fis.read(JarFileByteArray, 0, JarFileByteArray.length);
			if (bytesRead <= 0)
				break;
		}
		fis.close();
		return chunkContainer;
	}

	public void transferFileChunks(String chunkDirectory) throws IOException{
		
		// Make a RMI call to get the map of slave machines 
		ConcurrentHashMap<Integer, String> workerList = new ConcurrentHashMap<Integer,String>();
		//workerList = ()Naming.lookup("//127.0.0.1:23391/workerList");
		File folder = new File(chunkDirectory +"/");
		File[] listOfFiles = folder.listFiles();
        Queue<String> fileNames = new LinkedList<String>();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        fileNames.add(file.getName());
		    }
		}
		int numberofChunks = fileNames.size();
		int numberofSlaves = workerList.size();
		int partitionSize = numberofChunks/1;
		for(int j=0;j<numberofSlaves;j++){
		String ipAddress = workerList.get(j);
		  for(int i=0;i< partitionSize;i++){
			
			String fileName = fileNames.remove();
		    @SuppressWarnings("resource")
			Socket socket = new Socket("12", 23333);  
	        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  
	        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  
	        File file = new File(fileName);
	        oos.writeObject(file.getName());  
	  
	        FileInputStream fis = new FileInputStream(file);  
	        byte [] buffer = new byte[1024];  
	        Integer bytesRead = 0;  
	  
	        while ((bytesRead = fis.read(buffer)) > 0) {  
	            oos.writeObject(bytesRead);  
	            oos.writeObject(Arrays.copyOf(buffer, buffer.length));  
	        }  
	  
	        oos.close();  
	        ois.close();
		  }
	   }
	}
}