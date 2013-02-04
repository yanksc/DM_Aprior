import java.util.ArrayList;


public class Aprior {
	String strt;
	public Aprior(ArrayList tranSet, ArrayList<String> wordList,  Double MIN_SUP, boolean CandidateList){
		Validator validator = new Validator();
		SubsetGenerator sg = new SubsetGenerator();
		ArrayList<ArrayList> CanList = new ArrayList<ArrayList>();
		ArrayList<ArrayList> L_List = new ArrayList<ArrayList>();
		strt= new String();
		long begintime = System.currentTimeMillis();
		
		for(int i=0;i<wordList.size();i++){
			for(int j=i+1;j<wordList.size();j++){
				ArrayList<String> a = new ArrayList<String>();
				a.add(wordList.get(i));
				a.add(wordList.get(j));
				CanList.add(a);
			}
		}
		// generate L list 
		validator.valid_candList(CanList, L_List, tranSet, MIN_SUP);
		System.out.print("L1:" + wordList.size());
		strt = strt.concat("L1:" + wordList.size()+"\n");
		if(CandidateList)
			System.out.print("\tc2:" + CanList.size());
		System.out.println();
		System.out.print("L2:" + L_List.size());
		strt = strt.concat("L2:" + L_List.size()+"\n");
		
		for(int iter=3;	CanList.size() > 0 ;	iter++){
			CanList.clear();
			for(ArrayList llist : L_List ){
					for(int k=0;k<wordList.size();k++){
						ArrayList<String> tmpList = new ArrayList<String>();
						tmpList.addAll(llist);
						tmpList.add(wordList.get(k));
						if(L_List.containsAll( sg.getList(tmpList) ) )
							CanList.add(tmpList);
					}
			}
			L_List.clear();
			validator.valid_candList(CanList, L_List, tranSet, MIN_SUP);
			if(CandidateList)
				System.out.print("\tc"+iter+":"+ CanList.size());
			System.out.println();
			if(CandidateList)
				System.out.print("L"+iter+":"+ L_List.size());
			strt = strt.concat("L"+iter+":"+ L_List.size() + "\n");
		}
		if(CandidateList){
			System.out.println("\n terminate!");
			System.out.println("Aprior Algorithm Costs" + (System.currentTimeMillis()-begintime) + "ms");
		}
	}
	public String getTimeString(){
		return strt;
	}
}
