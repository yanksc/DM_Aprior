import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
public class main {	
	public static void main(String[] args) throws FileNotFoundException {
		Map wordmap = new HashMap<String, Double>();
//		ValueComparator bvc = new ValueComparator(wordmap);
		Map item2freq = new TreeMap<String, Double>();
//		ArrayList<ArrayList> sortCorpus = new ArrayList(bvc);
		Double MIN_SUP = 0.4; 
		Double tranSize = 0.0;
		ArrayList<ArrayList> tranSet = new ArrayList<ArrayList>();
		ArrayList<ArrayList> sortSet = new ArrayList<ArrayList>();
		ArrayList<String> wordList = new ArrayList();
		HashMap<String, Integer> L_result = new HashMap<String, Integer>();
		L_init(L_result);
		String timeRecord = "";
		HashSet<String> F1 = new HashSet<String>();
		File f = new File("./data/1000.txt");
		getWordmap(f, wordmap, tranSet);
		Aprior algorithm;
		getTermfreq(wordmap, item2freq, MIN_SUP);
		tranSize = (Double) wordmap.get("!EOD");
		wordList.addAll(item2freq.keySet());
		
		
//		System.out.println("tranSet:\n"+tranSet);
//		System.out.println("item2freq:\n" + item2freq);
		
		
		
		ArrayList<ArrayList> FreqItemSet = getFreqItemSet(MIN_SUP, tranSet, sortSet, item2freq, tranSize);
		long begintime = System.currentTimeMillis();
		FPTree fptree = new FPTree(FreqItemSet);
		CondPattern cp = new CondPattern(fptree);
		cp.constructAll(fptree);
		cp.combCheck(fptree, L_result, (Double)(MIN_SUP*tranSize));
//		System.out.println(L_result);
//		System.out.println("\n"+ fptree.getItemLink());
		
		
		System.out.println();
		System.out.println();
		long endtime = System.currentTimeMillis();
		algorithm = new Aprior(tranSet, wordList, MIN_SUP, true);
		
		
		
		
		// result showing !!!
		System.err.println(algorithm.getTimeString());
		System.err.println("fp-tree construction costs " + (endtime-begintime) + "ms");
		
		
		System.out.println();
		
	}
	
	
	
	
	public static void getWordmap(File f, Map wordmap, ArrayList<ArrayList> corpus) throws FileNotFoundException{
		Scanner read = new Scanner(f);
		String word;
		ArrayList<String> basket= new ArrayList<String>();
		Double term_count;
		while(read.hasNext()){
			word = read.next();
			if( word.compareTo("!EOD")!=0)
				basket.add(word);
			else{
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.addAll(basket);
				corpus.add(tmp);
				basket.clear();
			}
			// count the term frequency
			if(!wordmap.containsKey(word))
				wordmap.put(word, 1.0);
			else{
				term_count = (Double) wordmap.get(word);
				wordmap.put(word, term_count+1);
			}
		}
		read.close();
	}
	
	public static void getTermfreq(Map wordmap, Map s2freq, Double min_sup){		
		Double check_sup, MIN_SUP = min_sup;
		Double tranSize = (Double) wordmap.get("!EOD");
		for(Object en : wordmap.keySet() ){
			check_sup = (Double)( (Double)wordmap.get(en) / tranSize);
			 if(check_sup >= MIN_SUP){
				 String a = (String) en;
				 if(a.compareTo("!EOD")!=0)
					 s2freq.put(en, wordmap.get(en));
			 }
		}
		
	}
	
	public static ArrayList getFreqItemSet(Double min_sup, ArrayList<ArrayList> tranSet, ArrayList sortSet, Map item2freq, Double tranSize){
		ArrayList<ArrayList> tempList = new ArrayList();
		
		for(ArrayList trans : tranSet){
			ArrayList<Entry> EntrySet = new ArrayList<Entry>(); 
			for(Object item : trans){
				if(item2freq.containsKey((String)item) ){
					Double sup = (Double) item2freq.get((String) item);
					if( (sup/tranSize) > min_sup)
						EntrySet.add( new Entry( (String) item, sup)   );
				}
			}
			Collections.sort(EntrySet, new Compa());
			tempList.add(EntrySet);
		}
		return tempList;
	}
	
	public static void L_init(HashMap<String, Integer> L_result){
		for(int i  = 0; i< 15; i++){
			String str = "L" + i;
			L_result.put(str, 0);
		}
	}
}

class Compa implements Comparator<Entry>{
	public Compa(){
	}
	public int compare(Entry e1, Entry e2){
		if(e1.count<e2.count)
			return 1;
		else 
			return -1;
	}
}

class ValueComparator implements Comparator<String> {
    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}