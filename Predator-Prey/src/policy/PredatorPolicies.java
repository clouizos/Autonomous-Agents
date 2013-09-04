package policy;

import java.util.Random;

public class PredatorPolicies {

	public String randomPolicy(){
		//String newpos = "";
		String[] actions = {"north","south","west","east","wait"};
		
		Random generator = new Random();
		
		int randomindex = generator.nextInt(5);
		//System.out.println("random index:"+randomindex);
		
		return actions[randomindex];
	}	
	
}
