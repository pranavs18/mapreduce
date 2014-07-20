package worker;

import java.io.Serializable;

public class Pair<Key,Value> implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private Key key;
	private Value value;
	
	public Pair(Key k, Value v){
		this.setKey(k);
		this.setValue(v);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	
	
}