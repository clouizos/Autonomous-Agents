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
			Position newPos = prey.preymove(actions[i]);
			if(newPos.getX() != 0 || newPos.getY() != 0){
				possibleActions.add(actions[i]);
			}
		}
		
		Random generator = new Random();
		double probability = 100*generator.nextDouble();
		System.out.println("probability:"+probability);
		if(probability > 80){
			int randomIndex = generator.nextInt(possibleActions.size());
			return possibleActions.get(randomIndex);
		}else{
			return "wait";
		}

	}

}
