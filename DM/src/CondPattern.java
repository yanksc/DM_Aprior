import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


public class CondPattern {
//	Entry entry;
	FPTree fptree; 
	
	public CondPattern(FPTree fptree){
		this.fptree = fptree;  
	}
	
	// 把屬於這條 condition tree 的數字
	// 個別記錄在 Node 裡，但是Entry本身記錄一條 ArrayList<ArrayList> 記錄每一條路徑 當做是用來作樹。
	// FP-Tree中找到所有的節點，向上trace它的祖先節點	
	public void constructAll(FPTree fptree){
		Node curNode;
		ArrayList<Node> path = null;
		TreeMap NodeWeightMap = null; 
		Entry itemEntry;
		for(Object e :  fptree.entry2Node.keySet() ){
			itemEntry = (Entry) e; 
			itemEntry.trackTree = new HashMap<ArrayList, Integer>();
			
			// 對 item ->nodeArray 的每個node  存起來此 Node trace 回 root 的路徑
			for(Node n : (LinkedList<Node>) fptree.entry2Node.get(itemEntry) ){
				curNode = n;
				path = new ArrayList<Node>();
				// 第一次用看到先新增
				while(curNode.nodeID!=0){
					if(curNode.itemWeightMap == null)
						curNode.itemWeightMap = new TreeMap<Entry, Integer>();
//					if(curNode != n)
						path.add(curNode);
					curNode = curNode.parent;
				}
				// 把每個 path 的 weight 計起來
				if(path!=null){
					Collections.reverse(path);
					itemEntry.trackTree.put(path, n.weight);
				}
			}
			System.out.println(itemEntry.eName + ":\t" + itemEntry.trackTree);

		}
	}
	public HashMap trimbySup(HashMap pathMap, Double threshold, HashMap<String, Integer> L_result){
		HashMap item2sup = new HashMap<String, Integer>();
		HashMap trimed_item2sup = new HashMap<String, Integer>();
		HashMap newpathMap = new HashMap<ArrayList, Integer>();
		Integer tmp, lnum;
		String itemstr;
		// 把每條 path 加起來
		for(Object p : pathMap.keySet()){
			ArrayList<Node> path = (ArrayList<Node>) p;
			for(Node n : path){
				if(item2sup.containsKey(n.getName())){
					tmp = (Integer) item2sup.get(n.getName())+1;
					item2sup.put(n.getName(), tmp);
				}
				else{
					item2sup.put(n.getName(), pathMap.get(p));
				}
			}
		}
		
		// 如果這個 item 不合格，就掃描 path 把它全部砍掉
		// 對於 path 全部名字掃一次，如果等於的話就砍掉
		for(Object p : pathMap.keySet()){
			ArrayList<Node> path = (ArrayList<Node>) p;
			ArrayList<String> newpath = new ArrayList<String>();
			for(Node n : path){
				for(Object item : item2sup.keySet()){
					itemstr= (String)item;
//					if( (Integer) item2sup.get(itemstr) >= threshold){
						if(n.getName().compareTo(itemstr)==0){
							newpath.add(n.getName());
						}
//					}
				}
			}
			newpathMap.put(newpath, pathMap.get(p));
		}
		
		return newpathMap;
	}
	
	
	
	
	public void combCheck(FPTree fptree, HashMap<String, Integer> L_result, Double supNum){
		// 先建立 L1 
		TreeMap item2freq = new TreeMap<String, Integer>();
		TreeMap cand2freq = new TreeMap<ArrayList, Integer>();
		ArrayList<HashSet> CanList = new ArrayList<HashSet>();
		ArrayList<TreeSet> newCanList = new ArrayList<TreeSet>();
		ArrayList<String> itemList= new ArrayList<String>();
		HashMap<ArrayList, Integer> entrypathList;
		HashSet<String> tempList;
		HashSet<String> F1 = new HashSet<String>(); 
		int tmp;
		// 對每一個 header Set
		for(Object e :  fptree.entry2Node.keySet() ){
			Entry itemEntry = (Entry) e;
			itemEntry.trackTree = this.trimbySup(itemEntry.trackTree, supNum, L_result);
			System.out.println(itemEntry.getName() + "'s trackTree:" + itemEntry.trackTree);
//			this.pattern2tree(itemEntry.trackTree, itemEntry);
			
			cand2freq.clear();
			item2freq.clear();
			CanList.clear();
			itemList.clear();
			entrypathList = itemEntry.trackTree; 
			for(ArrayList<String> path : entrypathList.keySet()){
				for(String str : path){
					// 對某一個 trackTree 裡面的某個元素
					if(item2freq.keySet().contains(str)){
						tmp = (Integer) item2freq.get(str);
						item2freq.put(str, tmp+entrypathList.get(path)  );
					}else
						item2freq.put(str, entrypathList.get(path) );
				}
			}
//			System.out.println(item2freq);
			for(Object itemobj : item2freq.keySet()){
				String item = (String) itemobj;
				if( (Integer) item2freq.get(item) > supNum ){
					tempList = new HashSet<String>();
					tempList.add(item);
					F1.add(item);
					itemList.add(item);
					CanList.add(tempList);
				}
			}
//			System.out.println("CanList: \t " + CanList);
//			System.out.println("itemList: \t " + itemList);
			for(HashSet candSet : CanList){
				for(String item : itemList){
					
					if(!candSet.contains(item)){
						int support =0;
//						System.out.print(candSet + " x "+ item);
						// 做一個 cand Array x item 添加一個 Set 然後檢查是否還包含在裡面
						TreeSet newSet = new TreeSet<String>();
						for(Object obj : candSet)
							newSet.add(obj);
						newSet.add(item);
						// 如果有在這棵 entry tree 裡面
//						System.out.println(" = "+newSet);
						for(Object path : entrypathList.keySet()){
//							System.out.println("element in entrypathList.keyset " + path);
								if(  AcontainB(((ArrayList)path), newSet)){
									support+=entrypathList.get(path);
//									System.out.println(candArray+"的支持變多了!!" + support);
								}
						}

						if( support > supNum ){
							
							for(TreeSet ts : newCanList)
								if(!ts.equals(newSet)){
									newCanList.add((TreeSet) newSet);
//									System.out.println("newcanList 加入了" + newSet);
									break;
								}
						}
					}
				}
			}
//			System.out.println("new CanList : " + newCanList);
		}	
//		System.out.println(F1);
		L_result.put("L1", F1.size());
	}
	public boolean AcontainB(ArrayList A, TreeSet B){
		for(Object obj : B){
			if(!A.contains(obj))
				return false;
		}
		return true;
	}
	public void pattern2tree(HashMap pathMap, Entry e){
		e.condFPTree = new ArrayList<ArrayList<Node>>();
		int nodeId = -1;
		Node croot = new Node(null, "r", 0, nodeId--);
		Node curNode, nextNode;
		
		for(Object p : pathMap.keySet()){
			ArrayList<Node> path = (ArrayList<Node>) p;
			curNode = croot;
			for(Node n : path){
				// 如果已經是孩子的話
				if(curNode.getChildNameList().contains(n.getName())){
					nextNode = curNode.getChildByName(n.getName());
					System.err.println(curNode +"->"+n + nextNode.weight);
				}else{
					nextNode = new Node(curNode, n.getName(), 0, nodeId--);
					curNode.addChild(nextNode);
					System.err.println(curNode +"=>"+n + nextNode.weight);
				}
				curNode = nextNode;			
				nextNode.weight++;
			}
		}
	}
}

class StringComparator implements Comparator<String> {
    Map<String, Double> base;
    public StringComparator() {
        this.base = base;
    }
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
}