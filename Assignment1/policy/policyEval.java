package policy;

import java.util.ArrayList;
import java.util.Hashtable;

import statespace.*;

public class policyEval {
	private static State[][][][] statespace;
	public policyEval(){
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
	}
	
	/* max statespace state[i][j][k][l]
	 * where predator[i][j] prey[k][l]
	 */
	
	/* gamma = discount factor (0.8)
	 * theta = small positive number; threshold for continueing evaluation
	 * delta = change in state value  
	 */
	private double gamma, delta, theta;
	//dfsf
	//Dummy method getActionList
	public ArrayList<String> getActionList(State s){
		ArrayList<String> allAction = new ArrayList<String>();
		allAction.add("north");
		allAction.add("south");
		allAction.add("west");
		allAction.add("east");
		allAction.add("wait");
		return allAction;
	}
	
	public int wrap(int i) {
		if ((i > 10))
		    return i -= 11;
		if ((i < 0))
		    return i += 11;
		return i;
	}
	//Dummy method getAllNextState
	public ArrayList<State> getAllNextState(State[][][][] statespace, State s, String a){
		/*this method receives statespace naming statespace, current state, and action as parameter
		and return all possible next state objects
		*/
		int currPredX, currPredY, currPreyX, currPreyY = 0;
		ArrayList<State> allNextState = new ArrayList<State>();
		State nextState;
		currPredX = s.getPredator().getX();
		currPredY = s.getPredator().getY();
		currPreyX = s.getPrey().getX();
		currPreyY = s.getPrey().getY();
		
		switch (a){
		case "north" : 	currPredY = wrap(currPredY-1); 
						break;
		case "south" : 	currPredY = wrap(currPredY+1); 
						break;
		case "west" : 	currPredX = wrap(currPredY-1); 
						break;
		case "east" : 	currPredX = wrap(currPredY+1); 
						break;
		}
		
		nextState = statespace[currPredX][currPredY][currPreyX][currPreyY];
		allNextState.add(nextState);
		return allNextState;
	}
	
	//Dummy method getTransProb
	public double getTransProb(State cs, String a, State ns){
		double transProb=1;
		return transProb;
	}
	
	//Dummy method getTransReward
	public double getTransReward(State cs, String a, State ns){
		double transReward=0;
		if (ns.endState())
			transReward = 10;
		return transReward;
	}
	
	//Dummy method to get probability taking action a in current state
	//based on current policy
	public double getActionProb(State cs, String a){
		return 0.5;
	}
		
	public void doEvaluation(double gamma, State[][][][] statespace) {
        // statespace init
		double delta;
		double theta = 0.0001;
		double V,v;
		double rightSideV; 	//variable to save the summation over 
							//all state prime(nextstate) in the equation 
		double transProb;	//probability go to state s prime if we take 
							//action a from current state s
		double transReward;	//expected immediate reward of going to state s prime
							//by taking action a from state s
		double VNextState;	//value of next state
		double actionProb;	//probability taking action a in state s
							//under current policy
		
		ArrayList<String> allAction = new ArrayList<String>();
		ArrayList<State> allNextState = new ArrayList<State>();
		State currentState;
		int counter = 0;
		do{
			counter ++;
			System.out.println("Iteration - "+counter);
			delta = 0;
			for(int i = 0; i < 11; i++) 
			    for(int j = 0; j < 11; j++) 
			    	for(int k = 0; k < 11; k++) 
			    		for(int l = 0; l < 11; l++){
			    			currentState = statespace[i][j][k][l]; 
			    			v = currentState.getValue();
			    			V = 0;
			    			//get All possible actions in state s
			    			allAction = getActionList(currentState);
			    			for (String action : allAction){
			    				actionProb = getActionProb(currentState, action);
			    				//get all possible next states (but in this problem only one possible next state)
			    				allNextState = getAllNextState(statespace, currentState,action);
			    				rightSideV = 0;
			    				for (State nextState : allNextState){
			    					transProb = getTransProb(currentState, action,nextState);
			    					transReward = getTransReward(currentState, action,nextState);
			    					VNextState = nextState.getValue();
			    					rightSideV += transProb*(transReward + gamma*VNextState);
			    				}
			    				V += actionProb*rightSideV;
			    			}
			    			statespace[i][j][k][l].setValue(V);
			    			delta = Math.max(delta, Math.abs(v-V));
			    		}
			System.out.println("Value of delta "+delta);
		} while (delta>theta);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		policyEval PE = new policyEval();
		PE.doEvaluation(0.8, statespace);
		
	}

}
