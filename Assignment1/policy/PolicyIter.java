package policy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Random;

import statespace.*;
import policy.PolicyEval;
import policy.RandomPolicyPredator;

public class PolicyIter extends PolicyEval{

	/*
	 * max statespace state[i][j][k][l] where predator[i][j] prey[k][l]
	 */
	//private static State[][][][] statespace;
	//private static Hashtable stateactions, statevalues;
	/*
	 * gamma = discount factor (0.8) theta = small positive number; threshold
	 * for continueing evaluation delta = change in state value
	 */
	//private double gamma, delta, theta;
	private int evaluation_runs = 0;
	private int improvement_runs = 0;
	//private static PolicyEval polEval;

	public PolicyIter(double g, double t) {
		super(g, t, new RandomPolicyPredator());
		// statespace init
		//polEval = new PolicyEval(g, t, rPolpred);
//		statespace = new State[11][11][11][11];
//		for (int i = 0; i < 11; i++) {
//			for (int j = 0; j < 11; j++) {
//				for (int k = 0; k < 11; k++) {
//					for (int l = 0; l < 11; l++) {
//						State s = new State(new Position(i, j), new Position(k,
//								l), 0);
//						statespace[i][j][k][l] = s;
//					}
//				}
//
//			}
//		}
//		statespace = polEval.getStatespace();
//		gamma = polEval.getGamma();
//		theta = polEval.getTheta();
//		stateactions = polEval.getStateactions();
//		statevalues = polEval.getStatevalues();

	}
	
	public PolicyIter() {}

	public String getAction(State currentState) {
		show("inside get action: "+currentState);

		return (String) stateactions.get(currentState.toString());
	}
	


	public void doIteration() {
		
		//show("finished evaluation!");
		boolean stop_while = true;
		boolean noChange;
		//initPolicy(statespace);
		while(stop_while){
			//doPolicyEvaluation(gamma, PolicyIter.statespace);
			doPolicyEvaluation();
			show("Beginning Improvement");
			noChange = doPolicyImprovement();
			System.out.println("noChange: "+noChange);
			//System.out.println(stateactions);
			if(noChange){
				stop_while = false;
				show("Finished!");
				show("Evaluation runs:" + evaluation_runs);
				show("Improvement runs:" + improvement_runs);
			}
			//System.out.println(stop);
		}
	}


	public boolean doPolicyImprovement() {
		boolean noChange = true;
		String beta;
		boolean stop = false;
		String actions[] = { "north", "south", "east", "west", "wait" };
		String newPolicyaction;
		improvement_runs++;
improvement:for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				for (int k = 0; k < 11; k++) {
					for (int l = 0; l < 11; l++) {
						State currentState = statespace[i][j][k][l];
						//show((String)stateactions.get(PolicyIter.statespace[0][0][0][0]));
						beta = (String) stateactions.get(currentState.toString());
						//show("\nold policy action for "+currentState+": "+beta);
						newPolicyaction = argmaxupdateValue(currentState);
						//show("new policy action: "+newPolicyaction);
						if (!beta.equals(newPolicyaction)) {
							//show("found a better action for "+state);
							noChange = false;
							stateactions.put(currentState.toString(), newPolicyaction);
							
							//break improvement;
						}
					}
				}

			}
		}
		//show("out of improvement");
		//if (!noChange) {
			//show("improvement runs:" + improvement_runs);
			//doPolicyEvaluation(gamma, statespace);
			//stop = false;
		//}else
			//stop = true;
		return noChange;

	}
	
	
	//Dummy method to get probability taking action a in current state
	//based on current policy
	public double getActionProb(State cs, String a){
		return 0.2;
	}
		
	

	public String argmaxupdateValue(State cS) {
		String action = "wait"; // action
		State currentState = cS;
		State nextState;
		String[] moves = { "north", "south", "east", "west", "wait" };
		double[] actions = { 0, 0, 0, 0, 0 };
		Vector nextStates;
		int predX, predY, preyX, preyY;
		for (int i = 0; i < moves.length; i++) {
			action = moves[i];
			nextStates = currentState.nextStates(action);
			for (int j = 0; j < nextStates.size(); j++) {
				nextState = (State) nextStates.elementAt(j);

				predX = nextState.getPredator().getX();
				predY = nextState.getPredator().getY();
				preyX = nextState.getPrey().getX();
				preyY = nextState.getPrey().getY();
				actions[i] += getP(nextStates.size(), nextState)
						* (getReward(nextState) + (gamma * statespace[predX][predY][preyX][preyY]
								.getValue()));

			}
		}

		double max = actions[0];
		action = moves[0];
		for (int i = 1; i < actions.length; i++) {
			if (actions[i] > max) {
				max = actions[i];
				action = moves[i];
			}
		}
		statevalues.put(currentState.toString(), max);
		return action;
	}

	public double getP(int nrnextstates, State next) {
		if (next.getPreyaction().compareTo("wait") == 0)
			return (0.8);
		else
			return (0.2 / nrnextstates);
	}

	// implement reward function
	public double getReward(State s) {
		if (s.endState())
			return 10.0;
		return 0.0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolicyIter PI = new PolicyIter(0.8, 0.001);
		PI.doIteration();
		//printTable();
		PI.printTable(new Position(5,5));
		
        try {
	    PI.output();
        } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
        }
	}
}
