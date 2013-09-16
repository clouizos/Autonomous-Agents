package policy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import statespace.*;

public class PolicyEvalAlt {
	private static State[][][][] statespace;
	public PolicyEvalAlt(){
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
	
	
	//Dummy method getTransProb
	
	public double getTransProb(int numberOfNextStates, String action, State nextState) {
		if (nextState.endState())
			return 1;
		if(action.equals("wait"))
		    return (0.8);
		else
		    return (0.2/(numberOfNextStates-1));
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
		return 0.2;
	}
		
	public void doEvaluation(double gamma, State[][][][] statespace) {
        // statespace init
		double delta;
		double theta = 0.001;
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
		Vector allNextStates;
		State currentState, nextState;
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
			    				allNextStates = currentState.nextStates(action);
			    				rightSideV = 0;
			    				//for (State nextState : allNextStates){
			    				for (int ii=0;ii<allNextStates.size();ii++){
			    					nextState = (State) allNextStates.elementAt(ii);
			    					String preyaction = nextState.getPreyaction();
			    					int predX = nextState.getPredator().getX();
			    		        	int predY = nextState.getPredator().getY();
			            			int preyX = nextState.getPrey().getX();
			            			int preyY = nextState.getPrey().getY();
			    					nextState = statespace[predX][predY][preyX][preyY];
			            			VNextState = nextState.getValue();
			            			transProb = getTransProb(allNextStates.size(), preyaction, nextState);
			    					transReward = getTransReward(currentState, action,nextState);
			    					nextState.toString();
			    					if (transReward==1055){
			    						System.out.println("reward "+transReward);
			    						System.out.println("trans prob "+transProb);
			    						//System.out.println("TransReward"+transReward);
			    					}
			    					rightSideV += transProb*(transReward + gamma*VNextState);
			    				}
			    				V += actionProb*rightSideV;
			    			}
			    			//System.out.println("Value of V "+V);
			    			statespace[i][j][k][l].setValue(V);
			    			delta = Math.max(delta, Math.abs(v-V));
			    		}
			System.out.println("Value of delta "+delta);
		} while (delta>theta);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolicyEvalAlt PE = new PolicyEvalAlt();
		PE.doEvaluation(0.8, statespace);		
	}

}
