
package client;

import java.util.ArrayList;

import worker.MapReduce;


public class WordCount extends MapReduce {
	

	private static final long serialVersionUID = 1L;
   
	
	public void map(String key, String value, MapReduce mapper)  {
        String frequency = "1";
        String[] words = value.split("\\s+");
        for (String k : words) {
        	k = k.trim();
            if (k.length() > 0) {
                mapper.writeToFile(k, frequency);
            }
        }
			
    }
	
	
	 public void reduce(String key, ArrayList<String> values, MapReduce reducerResult) {
         
         Integer sum = 0;
         for (String value : values) {
            
                 sum += Integer.parseInt(value);
             
         }
         reducerResult.writeReducerOutput(key, sum.toString());
     }
    
	

}