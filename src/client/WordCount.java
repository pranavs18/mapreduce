
package client;

import java.util.ArrayList;
import worker.Mapper;
import worker.Reducer;
import worker.Pair;


public class WordCount   {
	
	public void map(Integer key, String value) {
        
        String[] words = value.split("\\s+");
        for (String keys : words) {
            if (keys.trim().length() > 0) {
              //  mapResult.add(new Pair<String, String>(keys.trim(), "1"));
            }
        }
    }
	
	
	 public void reduce(String key, ArrayList<String> values) {
         
         Integer sum = 0;
         for (String value : values) {
            
                 sum += Integer.parseInt(value);
             
         }
         //reducerResult.add(new Pair<String, String>(key, sum.toString()));
     }
    
	

}