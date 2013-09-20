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

    /*
     *  Constructors; inherits from policyEval
     */	
	public PolicyIter(double g, double t) {
		super(g, t, new RandomPolicyPredator());
	}
	
	public PolicyIter() {}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//init policy iteration
		PolicyIter PI = new PolicyIter(0.9, 1.0E-20);
		long startTime = System.nanoTime();
		PI.doIteration();
		//printTable();
		PI.printTable(new Position(5,5));
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println();
		System.out.println("time:"+estimatedTime+"ns");
		
        try {
	    PI.output();
        } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
        }
	}
	
    /*
     * Required method to implement; returns an action according to implemented policy
     */
	public String getAction(State currentState) {
		show("\ninside get action: "+currentState);

		return (String) stateactions.get(currentState.toString());
	}
	

	// function that performs the policy iteration
	public void doIteration() {
		
		//flag for stopping the while loop for evaluation and improvement
		boolean stop_while = true;
		//flag for the improvement, if noChange was true then there was no improvement on the policy
		boolean noChange;

		while(stop_while){
			//doPolicyEvaluation(gamma, PolicyIter.statespace);
			
			//perform policy evaluation and count the number of times the evaluation runs
			evaluation_runs+=doPolicyEvaluationIteration();
			//Position prey = new Position(5,5);
			//printTable(prey);
			//show("\nBeginning Improvement");
			//perform policy improvement and return if there was improvement or not
			noChange = doPolicyImprovement();
			//show("\nnoChange: "+noChange);
			//System.out.println(stateactions);
			
			//if there was no improvement then exit the while loop
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


	// policy improvement function
	public boolean doPolicyImprovement() {
		//flag for determining whether there was improvement or not
		boolean noChange = true;
		// old action according to the policy
		String beta;
		// new action according to the argmax update
		String newPolicyaction;
		// increment improvement runs
		improvement_runs++;
		
		// sweep the whole statespace
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				for (int k = 0; k < 11; k++) {
					for (int l = 0; l < 11; l++) {
						//record the state as the current state
						State currentState = statespace[i][j][k][l];
						//get the old action that was assigned on that state from the policy, from the hashmap
						beta = (String) stateactions.get(currentState.toString());
						// find the new action according to the argmax
						newPolicyaction = argmaxupdateValue(currentState);

						if (!beta.equals(newPolicyaction)) {
							//we found a better action hence we need to denote that there was a change in the policy
							noChange = false;
							// put the new action at the hashmap
							stateactions.put(currentState.toString(), newPolicyaction);

						}
					}
				}

			}
		}
		return noChange;

	}
	
//function that performs the argmax update
	public String argmaxupdateValue(State cS) {
		// init of action variable
		String action = "wait";
		// init current state
		State currentState = cS;
		State nextState;
		// possible moves 
		String[] moves = { "north", "south", "east", "west", "wait" };
		// vector that keeps the values for each action
		double[] actions = { 0, 0, 0, 0, 0 };
		double nextStateValue;
		// vector for all possible next states
		Vector nextStates;
		// for all possible moves
		for (int i = 0; i < moves.length; i++) {
			//get the move
			action = moves[i];
			// if it is the end state we don't want to calculate the reward
			if(cS.endState())
    			continue;
			// get the possible next states according to the current state and the action taken
			nextStates = currentState.nextStates(action);
			// for each next state
			for (int j = 0; j < nextStates.size(); j++) {
				nextState = (State) nextStates.elementAt(j);
				// calculate the next state value
				nextStateValue = (double)statevalues.get(nextState.toString());
				// and store it in the vector of values
				actions[i] += getP(nextStates.size(), nextState)
						* (getReward(nextState) + (gamma * nextStateValue));

			}
		}
		
		// find the action that maximizes the value, which is the best action
		double max = actions[0];
		action = moves[0];
		for (int i = 1; i < actions.length; i++) {
			if (actions[i] > max) {
				max = actions[i];
				action = moves[i];
			}
		}
		//put the new value in the hashmap and return the best action
		statevalues.put(currentState.toString(), max);
		return action;
	}
	
	public int doPolicyEvaluationIteration(){
		// init the multisweep for the policy evaluation
        return multisweep_iteration();   	
    }
	
	// the outer loop: repreat untill delta is smaller than theta
	public int multisweep_iteration() {
        int k = 0;
        //int depth = 0;
        do {
        	//init the delta
            delta = 0;
            // do the sweep
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
		//old value
        double v;
        // new value
        double vUpdate;
        //loop: for every state/node
        for(int i=0;i<11;i++) {
            for(int j=0;j<11;j++) {
            	for(int k = 0; k < 11; k++) {
    	    		for(int l = 0; l < 11; l++) {
    	    			State currentState = statespace[i][j][k][l];
    	    			v = (double) statevalues.get(currentState.toString());
    	    			//get the new value
    	    			vUpdate = updateValue_iteration(currentState);
    	    			//show("updated value: "+vUpdate+'\n');
    	    			// put the statevalue for currentState in the look up table
    	    	        statevalues.put(currentState.toString(), vUpdate);
    	    			// set delta as the maximum between the difference of the old and new values and the previous delta
    	    			delta = Math.max(delta, Math.abs(v - vUpdate));
    	    		}
            	}
            }
        }
        //show("booya delta: " + delta);
        return delta;
    }
	
	// perform the update of values of policy evaluation inside the policy iteration
	 public double updateValue_iteration(State cS) {
	    	String action = "wait"; // action
	    	State currentState = cS;
	    	State nextState;
	    	String[] moves = {"north", "south", "east", "west", "wait"};
	    	// records the right part: sum_s': Pss'a(Rss'a+gamma*V(s'))
	    	double[] actions = {0,0,0,0,0};
	    	double nextStateValue;
	    	Vector nextStates;
	    		action = (String)stateactions.get(cS.toString());
	    		// if we don't have an end state then we want to calculate the value
	    		if(!cS.endState()){
	    			nextStates = currentState.nextStates(action);
	    			for(int j=0; j<nextStates.size();j++) {
	    				nextState = (State) nextStates.elementAt(j);
	    				nextStateValue = (double)statevalues.get(nextState.toString());
	    				//insert the value into the vector of values according to actions
	    				actions[j] += getP(nextStates.size(), nextState) *
	    					(getReward(nextState) + (gamma * nextStateValue));
	    			}
	    		}
	    		
	    	// sum over all those values	
	    	double valueUpdate = actions[0];
	    	for(int i=1;i<actions.length;i++) {
	    		valueUpdate+=actions[i];
	    	}
	    	
	    	//return the new value
	    	return valueUpdate;
	    }
    
}
