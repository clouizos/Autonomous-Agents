package policy;

import statespace.State;

public class Sarsa extends QLearning{

	public Sarsa(double g, double a, PolicySelect p, int nrPred, String entity) {
		super(g, a, p, nrPred, entity);
	}
	
	@Override
	// TODO: change to Sarsa
	public void updateQ(State cs, State nextS) {			
		State currentState = cs.projectState();
		State nextState = nextS.projectState();
		double[] rewards = getReward(nextState);
		double reward;
		if(!(currentState.endState()==(-1|1))) {
			double currentQ = (Double) qtable.get(currentState);
			double nextQ = (Double) qtable.get(nextState);
			if(super.agent.equals("prey"))
				reward = rewards[1];
			else
				reward = rewards[0];
			double qUpdated = currentQ + alpha*(reward 
					+ gamma*nextQ - currentQ);
			qtable.put(currentState, qUpdated);
		}
	}

}