package policy;

import java.util.*;

import statespace.*;
import policy.RandomPolicyPredator;

public class PolicyIter extends PolicyEval{

	/*
	 * Inherited from superclass PolicyEval
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

	public PolicyIter(double g, double t) {
		super(g, t, new RandomPolicyPredator());
	}
	
	public PolicyIter() {}

	public String getAction(State currentState) {
		show("\ninside get action: "+currentState);

		return (String) stateactions.get(currentState.toString());
	}
	


	public void doIteration() {
		
		//show("finished evaluation!");
		boolean stop_while = true;
		boolean noChange;
		//initPolicy(statespace);
		while(stop_while){
			//doPolicyEvaluation(gamma, PolicyIter.statespace);
			evaluation_runs+=doPolicyEvaluationIteration();
			//Position prey = new Position(5,5);
			//printTable(prey);
			show("\nBeginning Improvement");
			noChange = doPolicyImprovement();
			show("\nnoChange: "+noChange);
			//System.out.println(stateactions);
			if(noChange){
				stop_while = false;
				show("\nFinished!");
				show("\nEvaluation runs:" + evaluation_runs);
				show("\nImprovement runs:" + improvement_runs);
			}
			//System.out.println(stop);
		}
		Position prey = new Position(5,5);
		printTable(prey);
		printList(prey);
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
		double nextStateValue;
		Vector nextStates;
		for (int i = 0; i < moves.length; i++) {
			action = moves[i];
			nextStates = currentState.nextStates(action);
			for (int j = 0; j < nextStates.size(); j++) {
				nextState = (State) nextStates.elementAt(j);
				nextStateValue = (double)statevalues.get(nextState.toString());
				actions[i] += getP(nextStates.size(), nextState)
						* (getReward(nextState) + (gamma * nextStateValue));

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
	    if(next.endState())	
	    	return 1.0;
	    else if(next.getPreyaction().equals("wait"))
		    return 0.8;
		else
		    return (0.2/nrnextstates);
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
	
	public void printTable(Position prey){

    	// outputs the values of all states where state:predator[i][j]prey[5][5] in a grid
    	show("\n======statevalues in grid around prey[5][5]======\n");
    	int nextline = 0;
    	for(int i = 0; i < 11; i++) {
    		if(nextline < i) {show("\n");}
    		for(int j = 0; j < 11; j++) {
    			State statePrey = new State(new Position(i,j), prey);
    			//polEval.show(String.format( "%.20f",(double)statevalues.get(statePrey55.toString())) + " ");
    			show(String.format( "%.3f",(double) statevalues.get(statePrey.toString())) + " ");
    		}
    		nextline = i;
    	}
    }
	
	public void printList(Position prey){
    	for(int i = 0; i < 11; i++) {
    		for(int j = 0; j < 11; j++) {
    			State statePrey = new State(new Position(i,j), prey);
    			show('\n' + statePrey.toString() +  " statevalue: " +(double)statevalues.get(statePrey.toString()));
    		}
    	}
    }
    
}
