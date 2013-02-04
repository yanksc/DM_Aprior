import java.util.ArrayList;
import java.util.TreeMap;


public class Node{
	Node parent, nextItem;
	ArrayList<Node> childList;
	String itemName;
	int weight;
	int nodeID;
	int id = 0;
	boolean canSup;
	public TreeMap itemWeightMap;
	public Node(Node p, String name, int w, int ID){
		this.parent = p;
		this.itemName = name;
		this.weight = w;
		this.nodeID = ID;
		canSup = true;
		childList = new ArrayList<Node>();
		TreeMap itemWeightMap = new TreeMap<ArrayList, Integer>();
	}
	public void addChild(Node n){
		childList.add(n);
	}
	public String getName(){
		return itemName;
	}
	public ArrayList<Node> getChildren(){
		return childList;
	}
	public ArrayList<String> getChildNameList(){
		ArrayList<String> childNameList = new ArrayList<String>();
		for(Node c : childList){
			childNameList.add(c.getName());
		}
		return childNameList;
	}
	public Node getChild(Node n){
		if(this.hasChild(n)){
			for(Node c : childList){
				if(n.getName().compareTo(c.getName())==0)
					return c;
			}	
		}
		return null;
			
	}
	public Node getChildByName(String name){
		for(Node c : childList){
			if(name.compareTo(c.getName())==0)
				return c;
		}
		return null;
	}
	
	public void setWeight(int w){
		this.weight = w;
	}
	
	public Boolean hasChild(Node n){
		return this.getChildNameList().contains(n.getName());
	}
	public void setNextItem(Node n){
		this.nextItem = n;
	}
	
	public String toString(){
		return ("#"+nodeID+itemName +"("+ weight+")");
	}
	
	public Boolean equals(Node n){
		if(n.nodeID == this.nodeID)
			return true;
		return false;
	}
	public void setWeightMap(TreeMap tm){
		this.itemWeightMap = tm;
	}
	
}
