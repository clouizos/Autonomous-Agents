package policy;
import java.util.HashMap;
import java.util.Vector;

import statespace.*;

import java.util.Random;

public class QLearning implements Policy {
	
	/* max statespace state[i][j][k][l]
	 * where predator[i][j] prey[k][l]
	 */
	
	/* gamma = discount factor (0.8)
	 * theta = small positive number; threshold for continueing evaluation
	 * delta = change in state value  
	 */
    protected HashMap qtable;
    protected double gamma, alpha;
    private static Policy policy;
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearning(double g, double a, Policy p){
		
		// policy could be e-greedy or softmax
	    policy = p;
	    /* qtable init
	     * qtable consist of all possible states+actions
	     * we consider the reduced q(s,a) where the statespace is reduced;
	     * predator[i][j]prey[5][5]moves[k]
	     * 
	     * q(s,a) gets an arbitrarily value of 0
	     */
		qtable = new HashMap<State, Double>();
	    String action;
	    String[] moves = {"north", "south", "east", "west", "wait"};
		for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for(int k = 0; k < 5; k++) {
			    	State s = new State(new Position(i, j), new Position(5, 5), moves[k]);
			    	// init Q(s,a) = 0 
			    	qtable.put(s, 0.0);	
		    	}
		    }
		}
	    gamma = g;
	    alpha = a;	      
	}
	
	public static void main(String[] args) {
		QLearning q = new QLearning(0.8, 0.002, new RandomPolicyPredator());
		show(q.initS().toString());
	}
	
	/*
	 * initializes S; choose a start state from the qtable
	 * 
	 */
	public State initS() {
		Random generator = new Random();
		Object[] qstates = qtable.keySet().toArray();
		State start = (State) qstates[generator.nextInt(qstates.length)];
		return start;
	}
	
	public String getAction(State s){
		// get action according to policy derived from Q
		String action = policy(s, qtable).getAction;
		return action;
	}
	
	public void updateQ(State currentState, State nextState) {
		double currentQ = (Double) qtable.get(currentState);
		double qUpdated = currentQ + alpha*(getReward(nextState) 
				+ gamma*argmaxQ() - currentQ);
	}
	
	public double argmaxQ() {
		double max = 0.0;
		return max;
	}
    // implement reward function: only when captured the immediate award=10, else 0
    public double getReward(State s) {
        if(s.endState())
            return 10.0;
	return 0.0;
    }
	
    public static void show(String s) {
        System.out.print(s);
    }
}
