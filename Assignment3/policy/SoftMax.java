package policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import statespace.State;

public class SoftMax extends PolicySelect {
	/* tau is the temperature. Sutton&Bratko:
	 * High temperatures cause the actions to be all (nearly) equiprobable. 
	 * Low temperatures cause a greater difference in selection probability 
	 * for actions that differ in their value estimates. In the limit as, 
	 * softmax action selection becomes the same as greedy action selection.
	 */
	
	public SoftMax(double tau) {
		super(tau);
	}
	
	@Override
	public String getAction(State s, Map<State, Double> qtable) {
		ArrayList<String> actionS = PolicySelect.getAllActions();
		// randomized list, so no move will get selected by default/preference at init stage
		long seed = System.nanoTime();
		Collections.shuffle(actionS, new Random(seed));
		double tau = super.parameter;
		State key;
		double qVal, temp = 0;
		double[] eQs = {0,0,0,0,0};
		for (String action : actionS){
			key = new State(s, action);
			qVal = qtable.get(key);
			// we distribute the values to [0-1] after normalizing with the sum
			eQs[actionS.indexOf(action)] = temp + Math.exp(qVal/tau);
			temp = eQs[actionS.indexOf(action)];
		}
		// last temp value is the sum over all actions
		Random rand = new Random(seed);
		
		int index = 0;
		double sample = rand.nextDouble();
		// returns the corresponding action according to the sample from the B. distr.
		while((eQs[index] / temp) < sample) index++;
		return actionS.get(index);
	}
}
