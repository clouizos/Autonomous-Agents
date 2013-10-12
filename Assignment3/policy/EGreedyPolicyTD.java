package policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import statespace.State;

public class EGreedyPolicyTD extends PolicySelect {
	
	public EGreedyPolicyTD(double epsilon){
		super(epsilon);
	}
	
	@Override
	public String getAction(State s, Map<String, Double> qtable){
		ArrayList<String> actionS = ArbitraryPolicy.getAllActions();
		// randomized list, so no move will get selected by default/preference at init stage
		long seed = System.nanoTime();
		Collections.shuffle(actionS, new Random(seed));
		double epsilon = super.parameter;
		//State key;
		String key;
		double qVal;
		double maxQ=-10;
		String maxAction="wait";
		for (String action : actionS){
			//key = new State(s, action);
			key = s.toString()+" "+action;
			System.out.println(key);
			qVal = qtable.get(key);
			if (maxQ<qVal){
				maxQ = qVal;
				maxAction = action;
			}
		}
		Random rand = new Random();
		int index;
		if (Math.random()<epsilon){
			index = rand.nextInt(4);
			actionS.remove(maxAction);
			return actionS.get(index);
		}
		return maxAction;
	}
}
