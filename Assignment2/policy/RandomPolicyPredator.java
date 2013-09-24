package policy;

import java.util.Random;
import statespace.*;

public class RandomPolicyPredator implements Policy {
	

	
	public RandomPolicyPredator() {
		
		// TODO Auto-generated constructor stub
		
	}
	
	public String getAction(State dummyState){
		
		String[] actions = {"north","south","west","east","wait"};
		
		Random generator = new Random();
		
		int randomindex = generator.nextInt(5);
		//System.out.println("random index:"+randomindex);
		
		return actions[randomindex];
		
	}

}
