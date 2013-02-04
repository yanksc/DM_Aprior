import java.util.ArrayList;

public class Combination {
	Integer[] series;
	int range,space;
	public Combination(){
		
	}
	public void init(int range,int space){
		this.range = range;
		this.space = space;
		series = new Integer[space];
		if(range < space){
			System.err.println("error! range can't be less than space!");
			return;
		}
	}
	
	public ArrayList<Integer[]> getAll(int range, int space){
		ArrayList<Integer[]> seri = new ArrayList<Integer[]>();
		int end;
		Integer[] head = new Integer[space];
		Integer[] elem, tmp;
		end = space -1;
		// 將所有組合一個一個加進 series 裡面
		elem = new Integer[space];
		for(int i = 0;i<=end;i++)
			elem[i] = i;
		seri.add(elem);
		System.err.println(elem[2]);
		//如果還沒結束 has more
		
		return seri;
	} 
	public Integer[] getNext(Integer[] test, int range, int space){
		
		int end = space -1;
		Integer[] result = new Integer[space];
		if(test[0]>range-space){
			result[0] = -1;
			return result;
		}
		// hard copy test to result
		for(int i=0;i<=end;i++){
			if(test[i]<=range)
				result[i] = test[i];
		}
		// update the series
		for(int j=0;j<=end;j++){
			// 若是找到有還沒變成range的
			if( result[end-j] < (range-j) ){
				result[end-j]++;
				// 這項為標準項 往後格子的全部加一
				for(int k=0;k<=j;k++){
					result[end-j+k] = result[end-j]+k;
					if(result[end-j+k]>range){
						System.out.println("out of bound");
						return null;
					}
				}
				return result;
			}
		}
		
		return result;

	}
	
}
