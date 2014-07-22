package worker;

import generics.MapReduceConfiguration;
import generics.SlaveRemoteInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.HashSet;

public class FileTransferAfterSort implements Runnable{

	private String ip;
	private HashSet<String> fileExtensionInformation = new HashSet<String>();
	private MapReduceConfiguration config =null;
	public FileTransferAfterSort(String ip,
			HashSet<String> fileExtensionInformation,MapReduceConfiguration config) {
		this.ip = ip;
		this.fileExtensionInformation = fileExtensionInformation;
		this.config = config;
	}

	@Override
	public void run() {
		
		try {
			transfer();
		} catch (IOException | NotBoundException e) {
			System.out.println("Exception occured while transfering sorted files");
		}
	}

	void transfer() throws IOException, NotBoundException{
		System.out.println("Begin transfer ");
		String filePath = config.getInputPath();
		int index = filePath.lastIndexOf("/");
		String fileName = filePath.substring(index+1, filePath.length()-4);
		String directoryPath = ".." + File.separator + "dfs" + File.separator + "intermediate" + File.separator + fileName;
		System.out.println("File location ... " + directoryPath);
		File folder = new File(directoryPath);
		File[] files = folder.listFiles();

		for(String str : fileExtensionInformation){
			for(int i = 0; i<files.length;i++){
				if(files[i].getName().contains(str)){
					/* RMI to transfer File */
					System.out.println("File found at location " + folder.getAbsolutePath());
					File file = new File(directoryPath+File.separator+files[i].getName());
					System.out.println("File path : "+directoryPath+File.separator+files[i].getName());
					byte buffer[] = new byte[(int)file.length()];
					BufferedInputStream input = new BufferedInputStream(new FileInputStream(directoryPath+File.separator+files[i].getName()));
					input.read(buffer,0,buffer.length);
					input.close();  	
					System.out.println("Transferring...." + files[i].getName());
					SlaveRemoteInterface obj = (SlaveRemoteInterface) Naming.lookup("//"+ ip +":9876/Remote");
					obj.ReceiveChunks(".."+File.separator+"dfs"+File.separator+"intermediate", fileName, buffer);
				}
			}
		}
		WorkerTasksStatus.getAddedWorkerAfterSortCount();
		
	}


}
