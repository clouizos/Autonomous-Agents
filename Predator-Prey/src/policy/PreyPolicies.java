package policy;

import java.util.Random;

public class PreyPolicies {

	
	public String dummyPreyPolicy() {
		String[] actions = {"north","south","west","east","wait"};
		int index = 4;
		
		Random generator = new Random();
		
		int randomindex = generator.nextInt(100);
		
		if(randomindex >= 80 && randomindex < 85){
			return actions[0];
		}
		else if(randomindex >= 85 && randomindex < 90){
			return actions[1];
		}
		else if(randomindex >= 90 && randomindex < 95){
			return actions[2];
		}
		else if(randomindex >= 95 && randomindex < 100){
			return actions[3];
		}
		
		return actions[index];
	}
}
