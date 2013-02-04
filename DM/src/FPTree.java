import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


class FPTree {
	Node root, curNode, nextNode;
	TreeMap entry2Node;
	
	int nodeId;
	public FPTree(ArrayList<ArrayList> freqList){
		nodeId =0;
		root = new Node(null, "R", 0, nodeId++);
		entry2Node = new TreeMap<Entry, LinkedList<Node> >();
		LinkedList<Node> tmpList = null;
		// tree construction from tranSet 
		for(ArrayList<Entry> tran : freqList){
			curNode = root;
			for(Entry e : tran){
//				System.out.print( e + "  ");
				// 如果屬於掃描點的孩子 就他長出來
				if(curNode.getChildNameList().contains(e.getName())){
					nextNode = curNode.getChildByName(e.getName());
//					System.err.println(curNode +"->"+e.getName());
				}else{
					//不是的話就從此點長出一顆，並指向 Node
					nextNode = new Node(curNode, e.getName(), 0, nodeId++);
					curNode.addChild(nextNode);
//					System.err.println(curNode +"=>"+e.getName());
				}
				
				curNode = nextNode;				
				nextNode.weight++;
				
				// 如果第一次看到 item 就建立table name2Node  
				if( ! entry2Node.keySet().contains(e)  ){
					//  新增一個 linked List 並新增加上同個
					tmpList = new LinkedList<Node>();
					tmpList.add(curNode);
					entry2Node.put( e, tmpList); 
				}	// 如果之前有看過 那就要找到最尾巴看是不是，是就+1 不是就長一條虛線出去
				else{
					if( !((LinkedList<Node>) entry2Node.get(e) ).contains(curNode)){
						Node n = ((LinkedList<Node>) entry2Node.get(e) ).getLast();
						n.setNextItem(curNode);
						((LinkedList<Node>) entry2Node.get(e) ).add(curNode);
					}	
				}
				 
				
			}
		}
	}
	public String toString(){
		String strOut = "the tree";
		return strOut;
	}
	public Map getItemLink(){
		return entry2Node;
	}
}
class NodeArray extends LinkedList<Node>{
	@Override
	public boolean contains(Object obj){
		String str;
		Node n;
		for(Object name : this){
			str = (String) name;
			n = (Node) obj;
			if( str.compareTo( n.getName())==0)
				return true;
		}
		return false;
	}
	
}
