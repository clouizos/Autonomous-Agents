package policy;
import java.util.Hashtable;
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
    protected State[][] statespace;
    protected Hashtable stateactions, statevalues;
    protected double gamma, delta, theta;
    private static Policy policy;
    private static double e;
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearning(double g, double t, Policy p, double e){
		
	    policy = p;
	    this.e = e;
	    	// statespace init
		statespace = new State[11][11];
		stateactions = new Hashtable<>();
	    statevalues = new Hashtable<>();
	    String action;
	    RandomPolicyPredator rpp = new RandomPolicyPredator();
		for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	State s = new State(new Position(i, j), new Position(5, 5));
		    	action = rpp.getAction(s);
		    	statespace[i][j] = s;
		    	stateactions.put((String)s.toString(), (String)action);
				statevalues.put((String)s.toString(), (Double)0.0);
		    }
		}
	        gamma = g;
	        theta = t;
	      
	}
	
	public String eGreedy(double e, State currentState){
		String action;
		double value = 0.0;
		String[] actions = {"north","south","east","west","wait"};
		Random generator = new Random();
		double takeGreedyAction = generator.nextDouble();
		//take greedy action
		if(takeGreedyAction > e){
			for (int i=0;i<actions.length;i++){
			
			}
		}
		//else explore
		int randomAction = generator.nextInt(5);
		return actions[randomAction];
	}
	
	public void doQlearning(){
		//for each episode
	
	}
	public String getAction(State dummyState){
		return "wait";
	}

}
