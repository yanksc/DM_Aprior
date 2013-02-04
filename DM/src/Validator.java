import java.util.ArrayList;

public class Validator {
	Double ratio;
	int itemcount;
	public Validator(){		
	}
	public void valid_candList(ArrayList<ArrayList> candList, 
								ArrayList<ArrayList> targetList,
								ArrayList<ArrayList> corpus, 
								Double min_support){
		for(ArrayList cand_item : candList){
			int itemcount = 0;
			// 對 corpus 的全部 transaction 做 validate
			for(ArrayList trans : corpus){
				if(trans.containsAll(cand_item))
					itemcount++;
			}
			ratio = (double) ((double)itemcount/(double)corpus.size());
			if(  ratio >= min_support )
				targetList.add(cand_item);
		}
	}
	
}