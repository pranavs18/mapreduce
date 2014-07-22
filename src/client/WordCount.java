
package client;

import java.util.ArrayList;

import worker.Mapper;
import worker.Reducer;

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
    } 
	
	
public void reduce(String key, ArrayList<String> values, Reducer reducerResult) {
         
         Integer totalCount = 0;
         for (String count : values) {
            
                 totalCount += Integer.parseInt(count);
             
         }
         reducerResult.writeToFile(key, totalCount.toString());
     }
    
	

}