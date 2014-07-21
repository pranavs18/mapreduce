package worker;

import java.io.Serializable;
import java.util.ArrayList;

public class Reducer  implements Serializable {


	private static final long serialVersionUID = 1L;
	protected Reducer reducerResult;
	protected String mapperOutPutFile;
	
	protected Reducer(String mapperOutPutFile){
		this.mapperOutPutFile = mapperOutPutFile;
	}
	
	public Reducer(){
		
	}
}