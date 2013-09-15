package policy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Random;

import statespace.*;

public class PolicyIter implements Policy {

	/*
	 * max statespace state[i][j][k][l] where predator[i][j] prey[k][l]
	 */
	private static State[][][][] statespace;
	private static Hashtable stateactions, statevalues;
	/*
	 * gamma = discount factor (0.8) theta = small positive number; threshold
	 * for continueing evaluation delta = change in state value
	 */
	private double gamma, delta, theta;
	private int evaluation_runs = 0;
	private int improvement_runs = 0;

	public PolicyIter(double g, double t) {
		// statespace init
		statespace = new State[11][11][11][11];
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				for (int k = 0; k < 11; k++) {
					for (int l = 0; l < 11; l++) {
						State s = new State(new Position(i, j), new Position(k,
								l), 0);
						statespace[i][j][k][l] = s;
					}
				}

			}
		}
		gamma = g;
		theta = t;
		stateactions = new Hashtable<>();
		statevalues = new Hashtable<>();

	}

	public Hashtable getSA() {
		return stateactions;
	}

	public Hashtable getSV() {
		return statevalues;
	}

	public void show(String s) {
		System.out.println(s);
	}

	public String getAction(State currentState) {
		show("inside get action: "+currentState);

		return (String) stateactions.get(currentState);
	}
	
	public void initPolicy(State[][][][] statespace){
		State currentState;
		Random generator = new Random();
		ArrayList<String> actions = getActions();
		String action;
		for(int i=0; i < 11; i++)
			for(int j=0; j< 11; j++)
				for(int k=0; k<11; k++)
					for(int l=0;l<11;l++){
						currentState = statespace[i][j][k][l];
						action = actions.get(generator.nextInt(5)); 
						stateactions.put(currentState, action);
						statevalues.put(currentState, 0.0);
					}	
	//System.out.println(stateactions.get(statespace[0][0][0][0]));
	}

	public void doIteration(double gamma, State[][][][] statespace) {
		
		//show("finished evaluation!");
		boolean stop_while = true;
		boolean noChange;
		initPolicy(statespace);
		while(stop_while){
			doPolicyEvaluation(gamma, PolicyIter.statespace);
			show("Beginning Improvement");
			noChange = doPolicyImprovement(gamma, PolicyIter.statespace);
			System.out.println("noChange: "+noChange);
			//System.out.println(stateactions);
			if(noChange){
				stop_while = false;
				show("Finished!");
				show("Evaluation runs:" + evaluation_runs);
				show("Improvement runs:" + improvement_runs);
			}
			//System.out.println(stop);
		}
	}

//	public void doPolicyEvaluation() {
//		double delta;
//		double v = 0;
//		double newValue = 0;
//		double maxruns = 0;
//		do {
//			evaluation_runs++;
//			delta = 0;
//			for (int i = 0; i < 11; i++) {
//				for (int j = 0; j < 11; j++) {
//					for (int k = 0; k < 11; k++) {
//						for (int l = 0; l < 11; l++) {
//							State currentState = statespace[i][j][k][l];
//							v = currentState.getValue();
//							newValue = updateValue(currentState);
//							//currentState.setValue(newValue);
//							statespace[i][j][k][l].setValue(newValue); //= currentState;
//							delta = Math.max(newValue, Math.abs(v - newValue));
//						}
//					}
//
//				}
//			}
//			maxruns++;
//			show("delta: " + delta);
//			show("policy evaluation run:" + evaluation_runs);
//		} while (delta > theta || maxruns < 2);
//	}

	public boolean doPolicyImprovement(double gamma, State[][][][] statespace) {
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
						State currentState = PolicyIter.statespace[i][j][k][l];
						//show((String)stateactions.get(PolicyIter.statespace[0][0][0][0]));
						beta = (String) stateactions.get(currentState);
						//show("\nold policy action for "+currentState+": "+beta);
						newPolicyaction = argmaxupdateValue(currentState);
						//show("new policy action: "+newPolicyaction);
						if (!beta.equals(newPolicyaction)) {
							//show("found a better action for "+state);
							noChange = false;
							stateactions.put(currentState, newPolicyaction);
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
	
	public ArrayList<String> getActionList(State s){
		ArrayList<String> allAction = new ArrayList<String>();
		allAction.add("north");
		allAction.add("south");
		allAction.add("west");
		allAction.add("east");
		allAction.add("wait");
		return allAction;
	}
	
	public ArrayList<String> getActions(){
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
		
	public void doPolicyEvaluation(double gamma, State[][][][] statespace) {
        // statespace init
		double delta;
		int maxruns = 0;
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
		ArrayList<Double> allActionValues = new ArrayList<Double>();
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
			    			allActionValues.clear();
			    			v = (Double)statevalues.get(currentState);
			    			//v = currentState.getValue();
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
			            			VNextState =(Double) statevalues.get(nextState);
			    					//VNextState = nextState.getValue();
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
			    				try{
			    					allActionValues.add(V - allActionValues.get(allActionValues.size()-1));
			    				}catch(Exception e){
			    					allActionValues.add(V);
			    				}
			    			}
			    			//System.out.println("Value of V "+V);
			    			/*int indexOfMax = 0;
			    			double initVal = Double.MIN_VALUE;
			    			for(int m = 0; m< allActionValues.size(); m++){
			    				if(allActionValues.get(m) > initVal){
			    					initVal = allActionValues.get(m);
			    					indexOfMax = m;
			    				}
			    			}*/
			    			//System.out.println(allActionValues);
			    			//show("index of max is:"+indexOfMax);
			    			//statespace[i][j][k][l].setValue(V);
			    			statevalues.put(currentState, V);
			    			//stateactions.put(statespace[i][j][k][l].toString(), allAction.get(indexOfMax));
			    			delta = Math.max(delta, Math.abs(v-V));
			    		}
			System.out.println("Value of delta "+delta);
			System.out.println("Value of theta "+theta);
			maxruns++; 
		} while (delta>theta /*&& maxruns < 1*/);
		show("out of loop!");
		evaluation_runs++;
		//PolicyIter.statespace =  statespace; 
		//return statespace;
	}

//	public double updateValue(State cS) {
//		String action = "wait"; // action
//		State currentState = cS;
//		State nextState;
//		double sum = 0;
//		String[] moves = { "north", "south", "east", "west", "wait" };
//		double[] actions = { 0, 0, 0, 0, 0 };
//		Vector nextStates;
//		for (int i = 0; i < moves.length; i++) {
//			action = moves[i];
//			nextStates = currentState.nextStates(action);
//			for (int j = 0; j < nextStates.size(); j++) {
//				nextState = (State) nextStates.elementAt(j);
//
//				int predX = nextState.getPredator().getX();
//				int predY = nextState.getPredator().getY();
//				int preyX = nextState.getPrey().getX();
//				int preyY = nextState.getPrey().getY();
//				actions[i] += getP(nextStates.size(), nextState)
//						* (getReward(nextState) + (gamma * statespace[predX][predY][preyX][preyY]
//								.getValue()));
//
//			}
//		}
//
//		for (double actionValue : actions) {
//			
//			sum += actionValue;
//		}
//		stateactions.put(currentState.toString(), action);
//
//		return sum;
//	}

	public String argmaxupdateValue(State cS) {
		String action = "wait"; // action
		State currentState = cS;
		State nextState;
		String[] moves = { "north", "south", "east", "west", "wait" };
		double[] actions = { 0, 0, 0, 0, 0 };
		Vector nextStates;
		for (int i = 0; i < moves.length; i++) {
			action = moves[i];
			nextStates = currentState.nextStates(action);
			for (int j = 0; j < nextStates.size(); j++) {
				nextState = (State) nextStates.elementAt(j);

				int predX = nextState.getPredator().getX();
				int predY = nextState.getPredator().getY();
				int preyX = nextState.getPrey().getX();
				int preyY = nextState.getPrey().getY();
				actions[i] += getP(nextStates.size(), nextState)
						* (getReward(nextState) + (gamma * statespace[predX][predY][preyX][preyY]
								.getValue()));

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
		//stateactions.put(currentState.toString(), action);

		return action;
	}

	public double getP(int nrnextstates, State next) {
		if (next.getPreyaction().compareTo("wait") == 0)
			return (0.8);
		else
			return (0.2 / nrnextstates);
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
		PI.doIteration(0.8, statespace);

	}

}
