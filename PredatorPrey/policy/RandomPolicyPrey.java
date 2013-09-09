package policy;

import java.util.ArrayList;
import java.util.Random;
import statespace.*;

public class RandomPolicyPrey implements Policy{

	public RandomPolicyPrey() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAction(State cs){
		String[] actions = {"north","south","west","east","wait"};
		Position prey = cs.getPrey();
		ArrayList<String> possibleActions = new ArrayList<String>();
		for(int i=0;i<4;i++){
			Position newPos = prey.preymove(actions[i]);
			if(newPos.getX() != 0 || newPos.getY() != 0){
				possibleActions.add(actions[i]);
			}
		}
		
		Random generator = new Random();
		
		int randomIndex = generator.nextInt(possibleActions.size());
		return possibleActions.get(randomIndex);
		

	}

}
