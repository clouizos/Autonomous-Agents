package policy;

import statespace.State;

public class Sarsa extends QLearning{

	public Sarsa(double g, double a, PolicySelect p) {
		super(g, a, p);
	}
	
	@Override
	// TODO: change to Sarsa
	public void updateQ(State cs, State nextS) {			
		State currentState = cs.projectState();
		State nextState = nextS.projectState();
		if(!currentState.endState()) {
			double currentQ = (Double) qtable.get(currentState);
			double nextQ = (Double) qtable.get(nextState);
			double qUpdated = currentQ + alpha*(getReward(nextState) 
					+ gamma*nextQ - currentQ);
			qtable.put(currentState, qUpdated);
		}
	}

}