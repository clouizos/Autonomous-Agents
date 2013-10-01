package policy;

import statespace.State;

public class Sarsa extends QLearning{

	public Sarsa(double g, double a, Policy p) {
		super(g, a, p);
	}
	
	@Override
	// TODO: change to Sarsa
	public void updateQ(State cs, State nextS) {			
		State currentState = cs.projectState();
		State nextState = nextS.projectState();
		double currentQ = (Double) qtable.get(currentState);
		double qUpdated = currentQ + alpha*(getReward(nextState) 
				+ gamma*argmaxQ(nextState) - currentQ);
		qtable.put(currentState, qUpdated);
	}

}
