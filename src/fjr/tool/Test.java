package fjr.tool;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args){
		ArrayList<Integer> list = new ArrayList<>(); 
		list.add(1); 
		
		ArrayList list2 = list; 
		list2.add("test... ");
		
		for(int i=0; i< list2.size(); i++){
			System.out.println(list2.get(i)); 
		}
 	}
	
}
