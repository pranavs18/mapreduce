package worker;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Worker implements Runnable {

	private String MasterIp ;
	static int workerServerPort = 23333;

	public Worker(String MasterIp){

		this.MasterIp = MasterIp;	
	}

	public Worker() {	

	}

	public void startWorkerHost(String MasterIp) throws UnknownHostException, IOException{
		@SuppressWarnings("resource")
		ServerSocket workerServer = new ServerSocket(workerServerPort);

		while(true){

			Socket masterClientSocket = workerServer.accept();	
			try {
				saveFile(masterClientSocket);
			} catch (Exception e) {
				System.out.println("Something wrong with transferring file chunks");
			}
			InputStreamReader input = new InputStreamReader(masterClientSocket.getInputStream());
			BufferedReader in = new BufferedReader(input);

			String[] arguments;

			String readString = "";
			while(( readString = in.readLine()) != null){
				arguments = readString.split(" ");

				/* Start a new thread to perform operations as required by the 
				 * message sent by master
				 */	  
			}
		}

	}
	
	private void saveFile(Socket socket) throws Exception {  
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  
        FileOutputStream fos = null;  
        byte [] buffer = new byte[1024];  
  
        // 1. Read file name.  
        Object o = ois.readObject();  
  
        if (o instanceof String) {  
            fos = new FileOutputStream(o.toString());  
        } else {  
            System.out.println("Something is wrong with file transfer"); 
        }  
  
        // 2. Read file to the end.  
        Integer bytesRead = 0;  
  
        do {  
            o = ois.readObject();  
  
            if (!(o instanceof Integer)) {  
            	System.out.println("Something is wrong with file transfer");
            }  
  
            bytesRead = (Integer)o;  
  
            o = ois.readObject();  
  
            if (!(o instanceof byte[])) {  
            	System.out.println("Something is wrong with file transfer");
            }  
  
            buffer = (byte[])o;  
  
            // 3. Write data to output file.  
            fos.write(buffer, 0, bytesRead);  
            
        } while (bytesRead == 1024);  
          
        System.out.println("File transfer success");           
        fos.close();  
        ois.close();  
        oos.close();  
    }  

	@Override
	public void run() {
		try {
			/* This might not be required when using an RMI */
			startWorkerHost(MasterIp);
		} catch (UnknownHostException e) {	
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws RemoteException{
		if(args.length != 1){
			System.out.println("Please enter the Arguments of the form - MasterIp");

		}


		MasterInformation.setMasterHost(args[0]);
		String MasterIp = MasterInformation.getMasterHost(); 
		System.out.println("Master Ip: "+MasterIp);



		System.setProperty("java.security.policy","C:/Users/PRANAV/Documents/mapreduce/policy.txt");
		
	    System.setProperty("java.rmi.server.hostname", "128.237.186.178");
		System.setSecurityManager(new java.rmi.RMISecurityManager());
		RemoteSplitterImpl remote = new RemoteSplitterImpl();
		
		WorkerJobLauncerImpl jobLauncher = new WorkerJobLauncerImpl();
		
		
		try {

			Registry registry = LocateRegistry.createRegistry(9876);
			registry.rebind("Remote", remote);
			System.out.println("Remote bounded" + remote);
			registry.rebind("job", jobLauncher);
			System.out.println("job bound" + remote);
		} catch (RemoteException e) {

		}


		WorkerRegisterHeartBeat heartBeat = new WorkerRegisterHeartBeat(MasterIp);

		// Start heart beat thread
		new Thread(heartBeat).start();
	}

}