package master;

import generics.ChunkProperties;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class TestFile {
	
	public static ConcurrentHashMap<String, ChunkProperties> masterTestMap = new ConcurrentHashMap<String, ChunkProperties>();

	
	
	public static ConcurrentHashMap<String, ChunkProperties> getMasterTestMap() {
		return masterTestMap;
	}



	public static void fillMap(){
		ArrayList<String> a1 = new ArrayList<String>();
		a1.add("128.237.191.229");
		ArrayList<String> a2 = new ArrayList<String>();
		a2.add("128.237.191.229");
		ArrayList<String> a3 = new ArrayList<String>();
		a3.add("128.237.191.229");
		ArrayList<String> a4 = new ArrayList<String>();
		a4.add("128.237.191.229");
		ArrayList<String> a5 = new ArrayList<String>();
		a5.add("128.237.191.229");
		ArrayList<String> a6 = new ArrayList<String>();
		a6.add("128.237.191.229");
		ArrayList<String> a7 = new ArrayList<String>();
		a7.add("128.237.191.229");
		ArrayList<String> a8 = new ArrayList<String>();
		a8.add("128.237.191.229");
		ChunkProperties c1 = new ChunkProperties("AVAILABLE", "", a1, null);
		ChunkProperties c2 = new ChunkProperties("AVAILABLE", "", a2, null);
		ChunkProperties c3 = new ChunkProperties("AVAILABLE", "", a3, null);
		ChunkProperties c4 = new ChunkProperties("AVAILABLE", "", a4, null);
		ChunkProperties c5 = new ChunkProperties("AVAILABLE", "", a5, null);
		ChunkProperties c6 = new ChunkProperties("AVAILABLE", "", a6, null);
		ChunkProperties c7 = new ChunkProperties("AVAILABLE", "", a7, null);
		ChunkProperties c8 = new ChunkProperties("AVAILABLE", "", a8, null);
		masterTestMap.put("pranav1.txt", c1);
		masterTestMap.put("pranav2.txt", c2);
		masterTestMap.put("pranav3.txt", c3);
		masterTestMap.put("pranav4.txt", c4);
		masterTestMap.put("pranav5.txt", c5);
		masterTestMap.put("pranav6.txt", c6);
		masterTestMap.put("pranav7.txt", c7);
		masterTestMap.put("pranav8.txt", c8);
		
		
	}

	
	
	
}
