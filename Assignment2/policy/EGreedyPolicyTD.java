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
		ArrayList<String> Actions = ArbitraryPolicy.getAllActions();
		// randomized list so no move will get selected by default at init stage
		long seed = System.nanoTime();
		Collections.shuffle(Actions, new Random(seed));
		State key;
		double Qval;
		double maxQ=-10;
		String maxAction=null;
		for (String action : Actions){
			key = new State(s, action);
			Qval = qtable.get(key);
			if (maxQ<Qval){
				maxQ = Qval;
				maxAction = action;
			}
		}
		Random rand = new Random();
		int index;
		if (Math.random()<epsilon){
			index = rand.nextInt(4);
			Actions.remove(maxAction);
			return Actions.get(index);
		}
		return maxAction;
	}
}
