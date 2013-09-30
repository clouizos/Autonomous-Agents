package policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import statespace.State;

public class EGreedyPolicyTD implements Policy{
	private double epsilon;
	
	public EGreedyPolicyTD(double e){
		epsilon = e;
	}
	
	// dummymethod
	public String getAction(State dummystate) {
		return "wait";
	}
	
	public String getAction(State s, Map<State, Double> qtable){
		ArrayList<String> actionS = ArbitraryPolicy.getAllActions();
		// randomized list, so no move will get selected by default at init stage
		long seed = System.nanoTime();
		Collections.shuffle(actionS, new Random(seed));
		State key;
		double qVal;
		double maxQ=-10;
		String maxAction=null;
		for (String action : actionS){
			key = new State(s, action);
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
