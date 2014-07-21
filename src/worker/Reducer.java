package worker;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Reducer <KeyIn, ValueIn, KeyOut, ValueOut> implements Serializable {


	private static final long serialVersionUID = 1L;
	public ArrayList<Pair<KeyOut, ValueOut>> reducerResult;
	
	public Reducer(){
		reducerResult = new ArrayList<Pair<KeyOut, ValueOut>>();
	}
	
	public abstract void reduce(KeyIn key, ArrayList<ValueIn> value);
	
	public ArrayList<Pair<KeyOut, ValueOut>> ReducerResult() {
        return reducerResult;
    }
}