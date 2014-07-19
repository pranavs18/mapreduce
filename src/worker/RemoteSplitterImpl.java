package worker;

import generics.Archiver;
import generics.MapReduceConfiguration;
import generics.fakeDistributedFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteSplitterImpl extends UnicastRemoteObject implements SlaveRemoteInterface {
	
	
	private static final long serialVersionUID = 1L;

	public RemoteSplitterImpl() throws RemoteException {
		
	}
	
	@SuppressWarnings("resource")
	public ArrayList<fakeDistributedFile> splitFileIntoChunks(String filename , MapReduceConfiguration config, Set<String> workerIps , String splitIp) throws RemoteException,IOException{
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
		String newChunkDirectory = filename.substring(0, index)+ "/.." + "/chunks" + chunkFileName;
		String newContainer = "chunks" + chunkFileName;
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
		  
		    bw = new BufferedWriter(new FileWriter(newChunkName,true));
		      
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
			
		}
		bw.close();
// jar archive logic 
		
		
		Archiver jarMaker = new Archiver();
		File directory = new File (config.getUserJavaFilePath());  
		String jarName = config.getUserProgramPackageName()+".jar";
		File newJarCreated = new File(jarName);
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
		for(String s:workerIps){
			if(!splitIp.equals(s)){
				String ip = s;
				SlaveRemoteInterface jarObj = null;
				try {
					jarObj = (SlaveRemoteInterface) Naming.lookup("//"+ ip +":9876/Remote");
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
	             jarObj.transferJar(jarName,JarFileByteArray);
			}
		}
		try {
			fetchChunks(newContainer, workerIps , splitIp);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return chunkContainer;
	}

	public void fetchChunks(String chunkDirectory, Set<String> workerIps, String splitIp) throws IOException, NotBoundException{
		
	    String path  = new File(".").getCanonicalPath();
	    int len = path.length();
	    if(path.contains("mapreduce")){
	    	path = path.substring(0,len-10);
	    }
	 
	    path = path+File.separator+ chunkDirectory;
		File folder = new File(path);
		System.out.println(folder.getCanonicalPath());
		File[] listOfFiles = folder.listFiles();
		System.out.println(listOfFiles.length + " chunks found");
        Queue<String> fileNames = new LinkedList<String>();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        fileNames.add(file.getName());
		    }
		}
		int numberofChunks = fileNames.size();
		int numberofSlaves = workerIps.size();
		int partitionSize = numberofChunks/numberofSlaves;
		SlaveRemoteInterface obj;
	for(String s:workerIps){
		String ipAddress = null;
    	if( ! s.equals(splitIp)){
    		ipAddress= s;
		for(int i=0;i< partitionSize;i++){
			
			String fileName = fileNames.remove();
			fileName = path + File.separator + fileName;
			System.out.println("File found at location " + fileName);
	        File file = new File(fileName);
	        byte buffer[] = new byte[(int)file.length()];
	        BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
            input.read(buffer,0,buffer.length);
            input.close();  	
             obj = (SlaveRemoteInterface) Naming.lookup("//"+ ipAddress +":9876/Remote");
             obj.transferChunks(fileName, buffer);
             
            
            }
		}
		}	
	}
	
	public void transferChunks(String fileName, byte buffer[]) throws IOException{
		
		File file = new File(fileName);
		//byte temp[] = new byte[(int)file.length()];
		byte temp[] = buffer;
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
        output.write(temp,0,temp.length);
        output.flush();
        output.close();
	    System.out.println(fileName + " chunk tranferred");
	}
	
  public void transferJar(String jarName, byte buffer[]) throws IOException{
		
		File file = new File(jarName);
		byte temp[] = buffer;
		
        FileOutputStream output =new FileOutputStream(file.getName());
        output.write(temp,0,temp.length);
        output.flush();
        output.close();
	    System.out.println(jarName + " Jar copied " );
	 }
	
	
	
}