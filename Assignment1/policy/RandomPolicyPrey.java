package policy;

import java.util.ArrayList;
import java.util.Random;
import statespace.*;

public class RandomPolicyPrey implements Policy{

	public RandomPolicyPrey() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAction(State cs){
		String[] actions = {"north","south","west","east"};
		Position prey = cs.getPrey();
		ArrayList<String> possibleActions = new ArrayList<String>();
		for(int i=0;i<actions.length;i++){
			State nextState = cs.nextStatePrey(actions[i]);
			// prey can't move to an occupied position
			if(!nextState.endState()){
				possibleActions.add(actions[i]);
			}
		}
		
		Random generator = new Random();
		double probability = generator.nextDouble();
		//System.out.println("prob:"+probability);
		if(probability > 0.8){
			int randomIndex = generator.nextInt(possibleActions.size());
			return possibleActions.get(randomIndex);
		}else{
			return "wait";
		}

	}

}
