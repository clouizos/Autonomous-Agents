package policy;

import java.util.ArrayList;
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
	
	//Dummy method getActionList
	public ArrayList<String> getActionList(Policy p, State s){
		ArrayList<String> allAction = new ArrayList<String>();
		allAction.add("north");
		allAction.add("south");
		allAction.add("west");
		allAction.add("east");
		allAction.add("wait");
		return allAction;
	}
	
	//Dummy method getAllNextState
	public ArrayList<State> getAllNextState(State[][][][] allState, State s, String a){
		/*this method receives statespace naming allState, current state, and action as parameter
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
		case "north" : 	currPredY -=1; 
						break;
		case "south" : 	currPredY +=1; 
						break;
		case "west" : 	currPredX -=1; 
						break;
		case "east" : 	currPredX +=1; 
						break;
		}
		
		nextState = allState[currPredX][currPredY][currPreyX][currPreyY];
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
		double transReward=1;
		return transReward;
	}
	
	//Dummy method to get probability taking action a in current state
	//based on current policy
	public double getActionProb(State cs, String a){
		return 0.5;
	}
		
	public policyEval(double gamma, Policy p, State[][][][] allState) {
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
		do{
			delta = 0;
			for(int i = 0; i < 11; i++) 
			    for(int j = 0; j < 11; j++) 
			    	for(int k = 0; k < 11; k++) 
			    		for(int l = 0; l < 11; l++){
			    			currentState = allState[i][j][k][l]; 
			    			v = currentState.getValue();
			    			V = 0;
			    			//get All possible actions in state s
			    			allAction = getActionList(p,currentState);
			    			for (String action : allAction){
			    				actionProb = getActionProb(currentState, action);
			    				//get all possible next states (but in this problem only one possible next state)
			    				allNextState = getAllNextState(allState, currentState,action);
			    				rightSideV = 0;
			    				for (State nextState : allNextState){
			    					transProb = getTransProb(currentState, action,nextState);
			    					transReward = getTransReward(currentState, action,nextState);
			    					VNextState = nextState.getValue();
			    					rightSideV += transProb*(transReward + gamma*VNextState);
			    				}
			    				V += actionProb*rightSideV;
			    			}
			    			allState[i][j][k][l].setValue(V);
			    			delta = Math.max(delta, Math.abs(v-V));
			    		}
			
		} while (delta<theta);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
