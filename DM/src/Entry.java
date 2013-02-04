import java.util.ArrayList;
import java.util.HashMap;


public class Entry implements Comparable{
	String eName;
	Double count;
	HashMap trackTree;
	ArrayList condFPTree;
	public Entry(String name, Double count){
		this.eName = name; 
		this.count = count;
	}
	@Override
	public String toString(){
		return (eName+":"+ count);
	}
	public String getName(){
		return eName;
	}
	@Override
	public int compareTo(Object obj) {
		String str = ((Entry) obj).eName;
		return str.compareTo(eName);
	}
	public HashMap tracknameTree(){
		
		return null;
	}

}
