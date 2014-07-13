/**********************************************************************************************
@author - Pranav Saxena , Vaibhav Suresh Kumar

This class implements a hash function to shuffle the mapper outputs to reducers
We have kept a default partitioner in our framework instead of allowing the user to define his own partioning
policy.
************************************************************************************************/
package generics;

public class defaultPartitioner {
	 
	public static int createPartition(String mapKey, int Reducers){
		// From the data structures class, using the hashcode of the key is a good hash function as it is always unique
		// but can sometimes lead to negative values which we have handled here
		int code = mapKey.hashCode();
		int numberofPartitions = code % Reducers;
		if(numberofPartitions < 0)
			numberofPartitions = (-1) * numberofPartitions;
		return numberofPartitions;
	}
}