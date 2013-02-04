import java.util.ArrayList;


public class SubsetGenerator {
	ArrayList outputArray;
	public SubsetGenerator(){
	}
	public ArrayList getList(ArrayList inputArray){
		outputArray = new ArrayList<ArrayList>();
		for(int i =0; i<inputArray.size();i++){
			ArrayList subArray = new ArrayList();
			subArray.addAll(inputArray);
			subArray.remove(i);
			outputArray.add(subArray);
		}
		return outputArray;
	}
}
