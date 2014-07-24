package client;

import java.util.ArrayList;

import worker.MapReduce;


public class SameSizeWords extends MapReduce {


	private static final long serialVersionUID = 1L;
   

	public void map(String key, String value, MapReduce mapper)  {
    
        String[] words = value.split("\\s+");
        for (String k : words) {
        	k = k.trim();
            if (k.length() > 0) {
                mapper.writeToFile(k.length()+"" , k);
            }
        }
    }


	 public void reduce(String key, ArrayList<String> values, MapReduce reducerResult) {
         
		 String output= "";
         for(String k:values){
        	output = output + k + " ";
         }
         reducerResult.writeReducerOutput(key, "[" + output + "]");
     }
}