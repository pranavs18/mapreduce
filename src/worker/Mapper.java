package worker;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Mapper <KeyIn, ValueIn, KeyOut, ValueOut> implements Serializable {


	private static final long serialVersionUID = 1L;
	public ArrayList<Pair<KeyOut, ValueOut>> mapResult;
	
	public Mapper(){
		mapResult = new ArrayList<Pair<KeyOut, ValueOut>>();
	}
	
	public abstract void map(KeyIn key, ValueIn value);
	
	public ArrayList<Pair<KeyOut, ValueOut>> MapperResult() {
        return mapResult;
    }
}