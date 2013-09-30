package policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
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
    private static EGreedyPolicyTD policy;
    private static ArrayList<String> actions = ArbitraryPolicy.getAllActions();
    
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearning(double g, double a, Policy p){
		
		// policy could be e-greedy or softmax
	    policy = (EGreedyPolicyTD) p;
	    /* qtable init
	     * qtable consist of all possible states+actions
	     * we consider the reduced q(s,a) where the statespace is reduced;
	     * predator[i][j]prey[5][5]moves[k]
	     * 
	     * q(s,a) gets an arbitrarily value of 0
	     */
		qtable = new HashMap<State, Double>();
	    // prey fixed at (5,5)
	    Position prey = new Position(5, 5);
		for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
			    	State s = new State(new Position(i, j), prey, action);
			    	// init Q(s,a) = 0 
			    	qtable.put(s, 0.0);	
		    	}
		    }
		}
	    gamma = g;
	    alpha = a;	      
	}
	
	public static void main(String[] args) {
	    double gamma = 0.5;
	    double alpha = 0.1;
	    // egreedy with epsilon = 0.1
	    EGreedyPolicyTD egreedy = new EGreedyPolicyTD(0.1);
	    // qlearning with policy
	    QLearning predPolicy = new QLearning(gamma, alpha, egreedy);
	    predPolicy.printList(new Position(5,5));
	}
	
	public String getAction(State s){
		// get action according to policy derived from Q
		State stateproj = s.projectState();
		String action = policy.getAction(stateproj, qtable);
		return action;
	}
	
	public void updateQ(State cs, State nextS) {			
		State currentState = cs.projectState();
		State nextState = nextS.projectState();
		double currentQ = (Double) qtable.get(currentState);
		double qUpdated = currentQ + alpha*(getReward(nextState) 
				+ gamma*argmaxQ(nextState) - currentQ);
		qtable.put(currentState, qUpdated);
	}
	
	// argmax_a' of Q(s',a')
	public double argmaxQ(State nextState) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		State key;
		double temp, max = 0;
		for(String action : actions) {
			key = new State(nextState, action);
			temp = (Double) qtable.get(key);
    		if(temp>max) {
    			max = temp;
    		}
    	}
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
    
    public void printList(Position prey){
    	for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
			    	State s = new State(new Position(i, j), prey, action);
			    	show('\n'+s.toString()+s.getAction()+ " statevalue: " +(double)qtable.get(s));
		    	}
		    }
		}
    }
}
