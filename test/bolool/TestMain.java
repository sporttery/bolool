package bolool;

import java.util.ArrayList;
import java.util.List;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> list = new ArrayList<String>();
		for(int i=0;i<20;i++) {
			list.add(""+i);
		}
		int topN = 30;
		List<String> subList = list.subList(0, topN > list.size()? list.size():topN);
		
		System.out.println(subList);

	}

}
