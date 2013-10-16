package policy;
import java.io.*;
import java.util.*;

import statespace.*;

import java.util.Random;

public class QLearning implements Policy {
	
	/* reduced statespace state[i][j][5][5]
	 * where predator[i][j] prey[5][5]
	 */
	
	/* gamma = discount factor (0.8)
	 * alpha = small positive number; learning rate
	 */
    protected HashMap<State, Double> qtable;
    //protected HashMap<String, String> stateactions;
    protected double gamma, alpha;
    //private static EGreedyPolicyTD policy;
    //private static SoftMax policy;
    private static PolicySelect policy;
    private static ArrayList<String> actions = PolicySelect.getAllActions();
    protected String agent;
    double initValue;
    
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearning(double g, double a, PolicySelect p, int nrPred, String entity){
		
		// policy could be e-greedy or softmax
	    //policy = (EGreedyPolicyTD) p;
	    //policy = (SoftMax) p;
		policy = p;
	    /* qtable init
	     * qtable consist of all possible states+actions
	     * we consider the reduced q(s,a) where the statespace is reduced;
	     * predator[i][j]prey[5][5]moves[k]
	     */
		qtable = new HashMap<State, Double>();
		// initializes Q(s,a) with input:value
		//initQ(15.0, nrPred);
		initValue = 15.0;
	    gamma = g;
	    alpha = a;
	    agent = entity;
	}
	
	// initializes Qtable: Q(s,a) arbitrarily with input:value
	public void initQ(double value, int nrPred) {
	    // prey fixed at (5,5)
	    Position prey = new Position(5, 5);
	    if(nrPred==1) {
	    	for(int i = 0; i < 11; i++) 
	    		for(int j = 0; j < 11; j++) 
	    			for (String action : actions){
	    				State s = new State(prey, action);
	    				s.addPred(new Position(i,j));
	    				// init Q(s,a) = 0 
	    				if(s.endState()==(1|-1))
	    					qtable.put(s, 0.0);
	    				else
	    					qtable.put(s, value);	
	    			}	    			    	
	    }
	    
	    if(nrPred==2) {
	    	for(int i = 0; i < 11; i++) 
	    		for(int j = 0; j < 11; j++) 
	    			for(int k = 0; k < 11; k++) 
	    				for(int l = 0; l < 11; l++) 
	    					for (String action : actions){
	    	    				State s = new State(prey, action);
	    	    				s.addPred(new Position(i,j));
	    	    				s.addPred(new Position(k,l));
	    	    				//s.sortPreds();
	    	    				// init Q(s,a) = 0 
	    	    				if(s.endState()==(1|-1))
	    	    					qtable.put(s, 0.0);
	    	    				else
	    	    					qtable.put(s, value);	
	    	    			}	    			    			    	
	    }
	    
	    if(nrPred==3) {
	    	// x
	    	for(int i = 0; i < 11; i++) 
	    		for(int j = 0; j < 11; j++) 
	    			for(int k = 0; k < 11; k++) 
	    				for(int l = 0; l < 11; l++) 
	    					for(int m = 0; m < 11;m++) 
	    						for(int n = 0; n < 11;n++) 
	    							for (String action : actions){
	    								State s = new State(prey, action);
	    								s.addPred(new Position(i,j));
	    								s.addPred(new Position(k,l));
	    								s.addPred(new Position(m,n));
	    								//s.sortPreds();
	    								// init Q(s,a) = 0 
	    								if(s.endState()==(1|-1))
	    									qtable.put(s, 0.0);
	    								else
	    									qtable.put(s, value);
	    							}	    								    						    					    				    			  
	    }
	    
	    if(nrPred==4) {
	    	for(int i = 0; i < 11; i++) 
	    		for(int j = 0; j < 11; j++) 
	    			for(int k = 0; k < 11; k++) 
	    				for(int l = 0; l < 11; l++) 
	    					for(int m = 0; m < 11;m++) 
	    						for(int n = 0; n < 11;n++) 
	    							for(int o = 0; o < 11;o++) 
	    								for(int p = 0; p < 11;p++) 
	    									for (String action : actions){
	    										State s = new State(prey, action);
	    										s.addPred(new Position(i,j));
	    										s.addPred(new Position(k,l));
	    										s.addPred(new Position(m,n));
	    										s.addPred(new Position(o,p));
	    										//s.sortPreds();
	    										// init Q(s,a) = 0 
	    												if(s.endState()==(1|-1))
	    													qtable.put(s, 0.0);
	    												else
	    													qtable.put(s, value);
	    									}
	    }
	}
	
	/* initializes with values from valueiteration
	public void initQVI() {
		VIPolicyReduced vip = new VIPolicyReduced(0.9, 1.0E-20);
		vip.multisweep();
	    Position prey = new Position(5, 5);
	    Hashtable statevalues = vip.getSV();
	    double value;
		for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
		    		State s = new State(new Position(i, j), prey, action);
		    		if(action.equals(vip.getAction(s))&&!s.endState()) {
		    				value = (Double)statevalues.get(s.toString());
		    				qtable.put(s, value);
		    		} else
			    	// init Q(s,a) = 0 
			    	qtable.put(s, 0.0);	
		    	}
		    }
		}
	}*/ 
	
	public String getAction(State s){
		// get action according to policy derived from Q
		if(agent.equals("prey")){
			double trip = Math.random();
			if (trip < 0.2){
				//show("prey tripped!\n");
				return "wait";
			}
		}
		State stateproj = s.projectState();
		String action = policy.getAction(stateproj, qtable);
		return action;
	}
	
	public void updateQ(State cs, State nextS) {			
		State currentState = cs.projectState();
		State nextState = nextS.projectState();
		double[] rewards = getReward(nextState);
		double reward;
		if(!(currentState.endState()==(-1|1))) {					
			Double currentQ = (Double) qtable.get(currentState);
			if(currentQ == null){
				qtable.put(currentState, initValue);
				currentQ = initValue;
			}
			if(agent.equals("prey"))
				reward = rewards[1];
			else
				reward = rewards[0];
			double qUpdated = currentQ + alpha*(reward 
					+ gamma*argmaxQ(nextState) - currentQ);
			qtable.put(currentState, qUpdated);
		}
	}
	
	// argmax_a' of Q(s',a') output: value
	public double argmaxQ(State nextState) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		State key;
		Double temp; 
		double max = 0;
		for(String action : actions) {
			key = new State(nextState, action);
			temp = (Double) qtable.get(key);
			if(temp == null){
				qtable.put(key, initValue);
				temp = initValue;
			}
    		if(temp>max) {
    			max = temp;
    		}
    	}
		return max;
	}
	
	// argmax_a' of Q(s',a') output: action
	public String argmaxQaction(State state) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		State key;
		Double temp; 
		double max = 0;
		String maxAction = "wait";
		for(String action : actions) {
			key = new State(state, action);
			temp = (Double) qtable.get(key);
			if(temp == null){
				qtable.put(key, initValue);
				temp = initValue;
			}
    		if(temp>max) {
    			max = temp;
    			maxAction = action;
    		}
    	}
		return maxAction;
	}
	
    // implement reward function: 
	// first check if predators got confused then immediate award = -10
	// else if captured then immediate award=10, else 0
    public double[] getReward(State s) {
        if(s.endState() == -1){
        	double[] ret = {-10.00,10.00};
        	return ret;
        }
        else if(s.endState() == 1){
        	double[] ret = {10.00,-10.00};
        	return ret;
        }
     double[] ret = {0.0,0.0};
	return ret;
    }
    
    
    // set action selection policy
    public void setSelectPolicy(PolicySelect p) {
    	PolicySelect policy = p;
    }
	
    public static void show(String s) {
        System.out.print(s);
    }
      
    /*
     *  Print methods for table and list of statevalues
     *  ONLY for 1 predator!
     */    
    public void printTable(Position prey){

    	// outputs the values of all states where state:predator[i][j]prey[5][5] in a grid
    	show("\n======max state values in grid around prey"+prey.toString()+"======\n");
    	int nextline = 0;
    	State s;
    	double qValue;
    	for(int i = 0; i < 11; i++) {
    		if(nextline < i) {show("\n");}
    		for(int j = 0; j < 11; j++) {
			    	s = new State(prey);
			    	s.addPred(new Position(j, i));
			    	qValue = argmaxQ(s);
			    	show(String.format( "%.3f\t", qValue));
    		}
    		nextline = i;
    	}
    }
    
    public void printList(Position prey){
    	for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
			    	State s = new State(prey, action);
			    	s.addPred(new Position(j, i));
			    	show('\n'+s.toString()+s.getAction()+ " statevalue: " +(double)qtable.get(s));
		    	}
		    }
		}
    }
    
    public void printActionsTable(Position prey) {
    State key;
    int nextline = 0;
    State s;
    String action;
    show("\n======stateactions in grid around prey"+prey.toString()+"======\n");
    for(int i = 0; i < 11; i++) {
    	if(nextline < i) {show("\n");}
    	for(int j = 0; j < 11; j++) {
    		s = new State(prey);
    		s.addPred(new Position(j, i));
    		action = argmaxQaction(s);
    		show(String.format( "%s\t", action));
    	}
    	nextline = i;
    }
	}

    
    /*
     *  IO methods, for writing the state actions into a file, 
     *  which can be used to fill up a lookup table when the policy 
     *  is executed within the simulator 
     */
    
    public static void write(File file, String string, boolean append) throws Exception
    {
	if(append==false)
	{
	    file.delete();
	    file.createNewFile();
	}

	FileOutputStream WriteFile = new FileOutputStream(file, true);
	OutputStreamWriter WriteBuff = new OutputStreamWriter(WriteFile, "UTF8");
	WriteBuff.write(string);
	WriteBuff.close();
	WriteFile.close();
    }     
    
    // outputs the state actions into a file policy.data: Only works for 1 predator
    public void output() throws Exception {
	File policyfile = new File("policy.data");
	policyfile.delete();
	policyfile.createNewFile();
	State state;
	String action;
	if(!qtable.isEmpty()) {
	    for(int i = 0; i < 11; i++) {
	    	for(int j = 0; j < 11; j++) {
	    		state = new State(new Position(5,5));
	    		state.addPred(new Position(j, i));
	    		action = argmaxQaction(state);
	    		try {
	    		    write(policyfile, state.toString()+"=>"+action+"\n", true);
	    		} catch (Exception e) {
	    		    // TODO Auto-generated catch block
	    		    e.printStackTrace();
	    		    System.out.println("Cannot write to file");
	    		}
	    	}
	    }
	} else
	    show("State actions table is empty!!");
    }
		
	public static void setPolicyA(PolicySelect policy) {
		QLearning.policy = policy;
	}

	public HashMap<State, Double> getQtable() {
		return qtable;
	}

	public void setQtable(HashMap<State, Double> qtable) {
		this.qtable = qtable;
	} 
	
	public static void main(String[] args) {
	    double gamma = 0.5;
	    double alpha = 0.1;
	    // egreedy with epsilon = 0.1
	    EGreedyPolicyTD policy = new EGreedyPolicyTD(0.1, 15.0);
	    // SoftMax with temperature tau = 0.1
	    //SoftMax policy = new SoftMax(0.1);
	    // qlearning with policy
	    QLearning predPolicy = new QLearning(gamma, alpha, policy, 4, "prey");
	    show("qtable size: "+predPolicy.getQtable().size());
	    //predPolicy.printTable(new Position(5,5));
	}
}