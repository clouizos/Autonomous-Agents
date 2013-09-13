package policy;

import java.util.Hashtable;

import statespace.*;

public class policyEval {
	
	/* max statespace state[i][j][k][l]
	 * where predator[i][j] prey[k][l]
	 */
	private State[][][][] statespace;
	/* gamma = discount factor (0.8)
	 * theta = small positive number; threshold for continueing evaluation
	 * delta = change in state value  
	 */
	private double gamma, delta, theta;
	
	public policyEval(double g, double t, Policy p) {
        // statespace init
	statespace = new State[11][11][11][11];
	for(int i = 0; i < 11; i++) {
	    for(int j = 0; j < 11; j++) {
	    	for(int k = 0; k < 11; k++) {
	    		for(int l = 0; l < 11; l++) {
	    			State s = new State(new Position(i, j), new Position(k, l), 0);
	    			statespace[i][j][k][l] = s;
	    		}
	    	}

	    }
	}
        gamma = g;
        theta = t;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
