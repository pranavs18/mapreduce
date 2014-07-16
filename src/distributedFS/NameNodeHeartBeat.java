package distributedFS;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class NameNodeHeartBeat implements Runnable{

	String MasterIp;
	int MasterPort;
	
	
	NameNodeHeartBeat(String MasterIp,int MasterPort){
		
		this.MasterIp = MasterIp;
		this.MasterPort = MasterPort;
		
	}
	
	
public void startHeartBeat(String MasterIp, int MasterPort) throws UnknownHostException, IOException{
		
		System.out.println("Connection to master established - Sending heart beat");
		
		Socket heartBeatSocket = new Socket(MasterIp, MasterPort);
		/* Keeps sending heartbeat with process map and its own server port number */
		
		PrintStream out = new PrintStream(heartBeatSocket.getOutputStream());
		
		InputStreamReader input = new InputStreamReader(heartBeatSocket.getInputStream());
		BufferedReader in = new BufferedReader(input);
		
		String readS = "";
		out.println("Hello ");
		System.out.println("Hello ");
		out.flush();
		
		
		while((readS = in.readLine())!=null){
			System.out.println(readS);
			
			/* Worker acting as client */
			out.println("Hello ");
			out.flush();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
		/* Closing all opened streams */
	    out.close();
	    heartBeatSocket.close();
	}
	
	@Override
	public void run() {
		NameNodeHeartBeat hb = new NameNodeHeartBeat(MasterIp, MasterPort);
		try {
			
			hb.startHeartBeat(MasterIp, MasterPort);
		} catch (UnknownHostException e) {
			System.out.println("Host not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Read error");
			e.printStackTrace();
		}
		
	}

}