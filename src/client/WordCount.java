
package client;

import java.util.ArrayList;

import worker.Mapper;


public class WordCount extends Mapper  {
	

	private static final long serialVersionUID = 1L;
   
	
	public void map(String key, String value, Mapper mapper)  {
        String frequency = "1";
        String[] words = value.split("\\s+");
        for (String k : words) {
        	k = k.trim();
            if (k.length() > 0) {
                mapper.writeToFile(k, frequency);
            }
        }
        
        	//System.out.println( "MAPPER LAUNCHED....WELCOME...distributedtest");
			
    }
	
	
	 public void reduce(String key, ArrayList<String> values) {
         
         int sum = 0;
         for (String value : values) {
            
                 sum += Integer.parseInt(value);
             
         }
         //reducerResult.add(key, sum.toString()));
     }
    
	

}