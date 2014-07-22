package worker;

import generics.Archiver;
import generics.ChunkProperties;
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
import java.io.InputStream;
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
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RemoteSplitterImpl extends UnicastRemoteObject implements SlaveRemoteInterface {


	private static final long serialVersionUID = 1L;
	public ConcurrentHashMap<String, ArrayList<String>> chunkTracker = new ConcurrentHashMap<String, ArrayList<String>>();

	public RemoteSplitterImpl() throws RemoteException {

	}

	@SuppressWarnings("resource")
	public ConcurrentHashMap<String, ChunkProperties > splitFileIntoChunks(String filename , MapReduceConfiguration config, Set<String> workerIps , String splitIp) throws RemoteException,IOException{
		
		System.out.println(" Splitting files into chunks.....Please wait");
	    ConcurrentHashMap<String, ChunkProperties> chunkContainer = new ConcurrentHashMap<String, ChunkProperties>();
		ConcurrentHashMap<String,ArrayList<String>> chunkTracker = new ConcurrentHashMap<String,ArrayList<String>>();
		fakeDistributedFile fdf = new fakeDistributedFile();
		//ChunkProperties cp = new ChunkProperties();
	
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
		//String newChunkDirectory = filename.substring(0, index)+ "/.." + "/chunks" + chunkFileName;
		String newChunkDirectory = ".."+File.separator+"dfs"+File.separator+"chunks";
		String newContainer = ".."+File.separator+"dfs"+File.separator+"chunks";
		File dir = new File(newChunkDirectory);
		if (!dir.exists()) {
			result = dir.mkdirs();

			if (result) {
				System.out.println("Folder is created");

			} 
		}
		String newChunkName = newChunkDirectory + File.separator + chunkFileName  + chunkNumber + ".txt";
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

			}finally{
				bw.close();
			}
			if(numberofLines == chunkSize){

				chunkNumber++;
				System.out.println("Chunk Number created..." + chunkNumber);
				try {
					
					fdf.setIpAddress(InetAddress.getLocalHost().getHostAddress());
					fdf.setFilename(filename);
					fdf.setReplicas(0);
					fdf.setChunkID(uniquechunkName);
					fdf.setChunkPath(newChunkName);
					//cp.setFdf(fdf);
					
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
		File directory = new File (config.getUserPackagePath());  
		String jarName = config.getUserProgramPackageName()+".jar";
		File newJarCreated = new File("."+File.separator+"src"+File.separator+jarName);
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
			String ip = s;
			SlaveRemoteInterface jarObj = null;
			try {
				jarObj = (SlaveRemoteInterface) Naming.lookup("//"+ ip +":9876/Remote");
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			jarObj.transferJar(jarName,JarFileByteArray);

		}
		try {
			chunkTracker = fetchChunks(newContainer, workerIps , splitIp);
			for(String s:chunkTracker.keySet()){
				String jobStatus = "AVAILABLE";
				String jobMachineHolder = "";
				ChunkProperties cp = new ChunkProperties();
				cp.setJobMachineHolder(jobMachineHolder);
				cp.setJobStatus(jobStatus);
				ArrayList<String> al = new ArrayList<String>();
				al = chunkTracker.get(s);
				System.out.println("Array List "+s+" "+al);
				cp.setCHUNK_IP_LIST(al);
				chunkContainer.put(s,cp);
			}
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return chunkContainer;
	}

	public ConcurrentHashMap<String,ArrayList<String>> fetchChunks(String chunkDirectory, Set<String> workerIps, String splitIp) throws IOException, NotBoundException{


		
		File folder = new File(chunkDirectory);
		System.out.println(folder.getCanonicalPath());
		File[] listOfFiles = folder.listFiles();
		System.out.println(listOfFiles.length + " chunks found");
		Queue<String> fileNames = new LinkedList<String>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				if(!file.getName().equals(".DS_Store"))
					fileNames.add(file.getName());
			}
		}
		int numberofChunks = fileNames.size();
		System.out.println("Chunks found " + numberofChunks);
		int numberofSlaves = workerIps.size();
		int partitionSize = numberofChunks/numberofSlaves;
		System.out.println("Partition size  " + partitionSize);
		SlaveRemoteInterface obj;
		//String chunkName = fileNames.peek();
		System.out.println("Total chunks..." + fileNames);
		
		//chunkTracker.put(chunkName,al);
		for(String s:workerIps){
			String ipAddress= null;
			if(!s.equals(splitIp)){
                ipAddress = s;
                ArrayList<String> al = new ArrayList<String>();
                al.add(ipAddress);
                if(!al.contains(splitIp))
                	al.add(splitIp);
				for(int i=0;i< partitionSize;i++){
                    
					String Name = fileNames.remove();
					if(chunkTracker.contains(Name)){
						//chunkTracker.get(Name).add(ipAddress);
					}
					else {
						chunkTracker.put(Name, al);
						System.out.println("chunk Map "+ Name + " " + chunkTracker.get(Name));
					}
					String fileName = chunkDirectory + File.separator + Name;
					System.out.println("File found at location " + folder.getAbsolutePath());
					File file = new File(fileName);
					byte buffer[] = new byte[(int)file.length()];
					BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
					input.read(buffer,0,buffer.length);
					input.close();  	
					System.out.println("Transferring...." + Name);
					obj = (SlaveRemoteInterface) Naming.lookup("//"+ ipAddress +":9876/Remote");
					obj.transferChunks(Name, buffer);


				}

			}
			else{
				ArrayList<String> al = new ArrayList<String>();
				al.add(splitIp);
				if(numberofSlaves == 1){
				for(int i=0;i<partitionSize;i++){

					String Name = fileNames.remove();
					if(chunkTracker.contains(Name)){
						//chunkTracker.get(Name).add(ipAddress);
					}
					else {
						chunkTracker.put(Name, al);
					}
			  }
				}
				for(String name:fileNames){
					chunkTracker.put(name, al);
				}
			}
		}
		System.out.println(" Chunks on parent machine" + fileNames);
		return chunkTracker;
	}

	public ConcurrentHashMap<String, ArrayList<String>> transferChunks(String Name, byte buffer[]) throws IOException{

		String path = ".."+File.separator+"dfs"+File.separator+"chunks";
		File dir = new File(path);
		if (!dir.exists()) {
			boolean result = dir.mkdirs();

			if (result) {
				System.out.println("Folder is created");

			} 
		}
		String newChunkName = path + File.separator + Name;
		File file = new File(newChunkName);


		byte temp[] = buffer;
		System.out.println(file.getCanonicalPath() + " ..." + file.getName());
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(newChunkName));
		output.write(temp,0,temp.length);
		output.flush();
		output.close();
		System.out.println(Name + " chunk tranferred");
		return chunkTracker;

	}

	public void transferJar(String jarName, byte buffer[]) throws IOException{
		
		File file = new File("src"+File.separator+jarName);
		byte temp[] = buffer;
		System.out.println("Absolute path of jar file: "+file.getAbsolutePath());
		FileOutputStream output =new FileOutputStream(file.getAbsolutePath());
		output.write(temp,0,temp.length);
		output.flush();
		output.close();
		System.out.println(jarName + " Jar copied " );
		System.out.println("Extracting Jar....");
		File jarfile = new File("src"+File.separator+jarName);
		System.out.println(jarfile.getAbsolutePath());  //remove

		JarFile jar = new JarFile(jarfile);
		System.out.println("jar name "+jar.getName());


		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();

			String fileName = "src"+File.separator+jarName.substring(0, jarName.length()-4)+File.separator +entry.getName();
			String binFile = "bin"+File.separator+jarName.substring(0, jarName.length()-4)+File.separator +entry.getName();
			
			File dir = new File("src"+File.separator+jarName.substring(0, jarName.length()-4));
			File bin = new File("bin"+File.separator+jarName.substring(0, jarName.length()-4));
			if (!dir.exists()) {
				Boolean result = dir.mkdirs();

				if (result) {
					System.out.println("Folder is created");

				} 
			}
			
			if (!bin.exists()) {
				Boolean result = bin.mkdirs();

				if (result) {
					System.out.println("Folder is created");

				} 
			}
			
			
			File f = new File(fileName);
            File b = new File(binFile);
			if (!fileName.endsWith("/")) {
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);

				// write contents of 'is' to 'fos'
				while (is.available() > 0) {
					fos.write(is.read());
				}

				fos.close();
				is.close();
			}
			if (!binFile.endsWith("/")) {
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(b);

				// write contents of 'is' to 'fos'
				while (is.available() > 0) {
					fos.write(is.read());
				}

				fos.close();
				is.close();
			}
		}
	}



	
	public boolean transferChunkOnRequest(String Name, byte[] buffer) throws IOException{
		
		boolean transfer = false;
		String path = ".."+File.separator+"dfs"+File.separator+"chunks";
		File dir = new File(path);
		if (!dir.exists()) {
			boolean result = dir.mkdirs();

			if (result) {
				System.out.println("Folder is created");

			} 
		}
		String newChunkName = path + File.separator + Name;
		File file = new File(newChunkName);
	
		byte temp[] = buffer;
		System.out.println(file.getCanonicalPath() + " ..." + file.getName());
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(newChunkName));
		output.write(temp,0,temp.length);
		output.flush();
		output.close();
		System.out.println(Name + " chunk tranferred");
		transfer = true;
	    return transfer;
	}
	
	public String transferChunktoSlave(String newChunkName, String fileName, ArrayList<String> visitedIPs , Set<String> workerIps, String splitIp) throws FileNotFoundException, IOException, NotBoundException{
		String ipAddresstoTransfer = null;
		
		for(String v:visitedIPs){
			for(String w:workerIps ){
				if(!v.equals(w)){
					ipAddresstoTransfer = w;
					break;
				}
			}
		}
		if(ipAddresstoTransfer == null){
			ipAddresstoTransfer = visitedIPs.get(0);
		    System.out.println("Transferred machine IP" + ipAddresstoTransfer);
		}
		
		
        String path = ".." + File.separator + "dfs" + File.separator + "chunks";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        System.out.println("Total files found..." + listOfFiles.length);
        File file = null;
        
        for(File f:listOfFiles){
        	if(!f.getName().equals(newChunkName))
        		continue;
        	else if(f.getName().equals(newChunkName)){
        		System.out.println("\n File to be transferred found in the DFS...\n Transferring now.... ");
        		file = new File(newChunkName);
        		break;
        	}
            
        		
        }
        
        byte buffer[] = new byte[(int)file.length()];
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(path+File.separator+newChunkName));
		input.read(buffer,0,buffer.length);
		input.close();  	
		SlaveRemoteInterface obj = null;
		obj = (SlaveRemoteInterface) Naming.lookup("//"+ ipAddresstoTransfer +":9876/Remote");
		boolean flag = obj.transferChunkOnRequest(newChunkName, buffer);
		if(flag == true)
			System.out.println("Chunk " + newChunkName + " transferred from the machine with IP ADDRESS " + ipAddresstoTransfer);
		else
			System.out.println("Chunk Transfer failed");
		
		return ipAddresstoTransfer;
	}
	
}