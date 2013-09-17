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
		for (int i = 0; i < 11; i++) {
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
			if(cS.endState())
    			continue;
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


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolicyIter PI = new PolicyIter(0.9, 1.0E-20);
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
	
	
	public int doPolicyEvaluationIteration(){
        return multisweep_iteration();   	
    }
	
	public int multisweep_iteration() {
        int k = 0;
        //int depth = 0;
        do {
            delta = 0;
            delta = sweep_iteration();
            //show("delta: " + delta+'\n');
            //show("theta: " + theta+'\n');
            k++;
            //--k;
        } //while (k>0);
        while(delta > theta);
        show("\nnr of iterations: "+k);
        return k;
    }
	
	public double sweep_iteration() {
        double v;
        double vUpdate;
        //loop: for every state/node
        for(int i=0;i<11;i++) {
            for(int j=0;j<11;j++) {
            	for(int k = 0; k < 11; k++) {
    	    		for(int l = 0; l < 11; l++) {
    	    			State currentState = statespace[i][j][k][l];
    	    			v = (double) statevalues.get(currentState.toString());
    	    			//show("current value: "+v+'\n');
    	    			vUpdate = updateValue_iteration(currentState);
    	    			//show("updated value: "+vUpdate+'\n');
    	    			// put the statevalue for currentState in the look up table
    	    	        statevalues.put(currentState.toString(), vUpdate);
    	    			//show("check updated value: "+currentState.getValue()+'\n');
    	    			delta = Math.max(delta, Math.abs(v - vUpdate));
    	    		}
            	}
            }
        }
        //show("booya delta: " + delta);
        return delta;
    }
	
	 public double updateValue_iteration(State cS) {
	    	String action = "wait"; // action
	    	State currentState = cS;
	    	State nextState;
	    	String[] moves = {"north", "south", "east", "west", "wait"};
	    	// records the right part: sum_s': Pss'a(Rss'a+gamma*V(s'))
	    	double[] actions = {0,0,0,0,0};
	    	double nextStateValue;
	    	Vector nextStates;
	    	//for(int i=0;i<moves.length;i++) {
	    		//action = moves[i];
	    		action = (String)stateactions.get(cS.toString());
	    		if(!cS.endState()){
	    			nextStates = currentState.nextStates(action);
	    			for(int j=0; j<nextStates.size();j++) {
	    				nextState = (State) nextStates.elementAt(j);
	    				nextStateValue = (double)statevalues.get(nextState.toString());
	    				actions[j] += getP(nextStates.size(), nextState) *
	    					(getReward(nextState) + (gamma * nextStateValue));
	    			}
	    		}
	    	//}
	    	double valueUpdate = actions[0];
	    	//double valueUpdate = 0.0;
	    	//action = moves[0];
	    	for(int i=1;i<actions.length;i++) {
	    		valueUpdate+=actions[i];
	    	}
	    	
	    	//statevalues.put(currentState.toString(), action);
	    	//return max;
	    	return valueUpdate;
	    }
    
}
