
package client;

import java.util.ArrayList;

import worker.Mapper;


public class WordCount extends Mapper  {
	
	public WordCount(String fileChunkName) {
		super(fileChunkName);
		
	}


	private static final long serialVersionUID = 1L;
   
	public void map(String key, String value)  {
        String frequency = "1";
        String[] words = value.split("\\s+");
        for (String k : words) {
            if (k.trim().length() > 0) {
                mapResult.writeToFile(k.trim(), frequency);
            }
        }
        
        try {
        	System.out.println( "MAPPER LAUNCHED....WELCOME...distributedtest");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
	
	 public void reduce(String key, ArrayList<String> values) {
         
         int sum = 0;
         for (String value : values) {
            
                 sum += Integer.parseInt(value);
             
         }
         //reducerResult.add(key, sum.toString()));
     }
    
	

}