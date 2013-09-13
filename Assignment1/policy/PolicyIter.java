package policy;

import java.util.Hashtable;
import java.util.Vector;

import statespace.*;

public class PolicyIter implements Policy{

	/* max statespace state[i][j][k][l]
	 * where predator[i][j] prey[k][l]
	 */
	private static State[][][][] statespace;
	private static Hashtable stateactions, statevalues;
	/* gamma = discount factor (0.8)
	 * theta = small positive number; threshold for continueing evaluation
	 * delta = change in state value  
	 */
	private double gamma, delta, theta;
	private int evaluation_runs = 0;
	private int improvement_runs = 0;
	
	public PolicyIter(double g, double t) {
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
    
    public String getAction(State currentState){
        show(currentState.toString());
       
	return (String)stateactions.get(currentState.toString());
   }
	
	public void doIteration(double gamma,State[][][][] statespace){
		doPolicyEvaluation();
		boolean stop = doPolicyImprovement();
		if(stop){
			show("Finished!");
			show("Evaluation runs:"+ evaluation_runs);
			show("Improvement runs:"+ improvement_runs);
		}
	}
	
	public void doPolicyEvaluation(){
		double delta = 0;
		double v = 0;
		double newValue = 0;
		double maxruns = 0;
		do{
			evaluation_runs++;
		for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for(int k = 0; k < 11; k++) {
		    		for(int l = 0; l < 11; l++) {
		    			State currentState = statespace[i][j][k][l];
		    			v = currentState.getValue();
		    			newValue = updateValue(currentState);
		    			currentState.setValue(newValue);
		    			statespace[i][j][k][l] = currentState;
		    			delta = Math.max(newValue, Math.abs(v - newValue));
		    		}
		    	}

		    }
		}
		maxruns ++;
		show("delta: "+delta);
		}while(delta > theta || maxruns < 100);
	}
	
	public boolean doPolicyImprovement(){
		boolean noChange = true;
		String beta;
		boolean stop = false;
		String actions[] = {"north","south","east","west","wait"};
		String newPolicyaction;
		improvement_runs++;
		for(int i = 0; i < 111; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for(int k = 0; k < 11; k++) {
		    		for(int l = 0; l < 11; l++) {
		    			State currentState = statespace[i][j][k][l];
		    			beta = currentState.getPreyaction();
		    			newPolicyaction = argmaxupdateValue(currentState);
		    			if(beta.compareTo(newPolicyaction) == 1){
		    				noChange = false;
		    			}
		    		}
		    	}

		    }
		}
		if(!noChange){
			doPolicyEvaluation();
		}
		stop = true;
		return stop;
		
	}
	public double updateValue(State cS) {
		String action = "wait"; // action
		State currentState = cS;
		State nextState;
		double sum = 0;
	        String[] moves = {"north", "south", "east", "west", "wait"};
	        double[] actions = {0,0,0,0,0};
	        Vector nextStates;
	        for(int i=0;i<moves.length;i++) {
	            action = moves[i];
	            nextStates = currentState.nextStates(action);
	            for(int j=0; j<nextStates.size();j++) {
	            	nextState = (State) nextStates.elementAt(j);
	            	
			        	int predX = nextState.getPredator().getX();
			        	int predY = nextState.getPredator().getY();
	        			int preyX = nextState.getPrey().getX();
	        			int preyY = nextState.getPrey().getY();
	                	actions[i] += getP(nextStates.size(), nextState) *
	                        (getReward(nextState) +
	                         (gamma * statespace[predX][predY][preyX][preyY].getValue()));
	        	    
	            }
	        }
	        
	        for (double actionValue : actions){
	        	sum += actionValue;
	        }
	            stateactions.put(currentState.toString(), action);
	      
	        return sum;
	    }
	
	public String argmaxupdateValue(State cS) {
		String action = "wait"; // action
		State currentState = cS;
		State nextState;;
	        String[] moves = {"north", "south", "east", "west", "wait"};
	        double[] actions = {0,0,0,0,0};
	        Vector nextStates;
	        for(int i=0;i<moves.length;i++) {
	            action = moves[i];
	            nextStates = currentState.nextStates(action);
	            for(int j=0; j<nextStates.size();j++) {
	            	nextState = (State) nextStates.elementAt(j);
	            	
			        	int predX = nextState.getPredator().getX();
			        	int predY = nextState.getPredator().getY();
	        			int preyX = nextState.getPrey().getX();
	        			int preyY = nextState.getPrey().getY();
	                	actions[i] += getP(nextStates.size(), nextState) *
	                        (getReward(nextState) +
	                         (gamma * statespace[predX][predY][preyX][preyY].getValue()));
	        	    
	            }
	        }
	        
	        double max = actions[0];
	        action = moves[0];
	        for(int i=1;i<actions.length;i++) {
	            if(actions[i]>max) {
	            	max = actions[i];
	            	action = moves[i];
	            }
	        }
	            stateactions.put(currentState.toString(), action);
	      
	        return action;
	    }

	public double getP(int nrnextstates, State next) {
		if(next.getPreyaction().compareTo("wait")==0)
		    return (0.8);
		else
		    return (0.2/nrnextstates);
	    }

	    // implement reward function
	    public double getReward(State s) {
	        if(s.endState())
	            return 10.0;
		return 0.0;
	    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolicyIter PI = new PolicyIter(0.8, 0.001);
		PI.doIteration(0.8, statespace);

	}

}
