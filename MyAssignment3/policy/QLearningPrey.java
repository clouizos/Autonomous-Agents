package policy;
import java.io.*;
import java.util.*;

import statespace.*;

import java.util.Random;

public class QLearningPrey implements Policy {
	
	/* reduced statespace state[i][j][5][5]
	 * where predator[i][j] prey[5][5]
	 */
	
	/* gamma = discount factor (0.8)
	 * alpha = small positive number; learning rate
	 */
    protected HashMap<String, Double> qtable;
    protected HashMap<String, String> stateactions;
    protected double gamma, alpha;
    //private static EGreedyPolicyTD policy;
    //private static SoftMax policy;
    private static PolicySelect policy;
    private static ArrayList<String> actions = ArbitraryPolicy.getAllActions();
    
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearningPrey(double g, double a, PolicySelect p, int nrPred){
		
		// policy could be e-greedy or softmax
	    //policy = (EGreedyPolicyTD) p;
	    //policy = (SoftMax) p;
		policy = p;
	    /* qtable init
	     * qtable consist of all possible states+actions
	     * we consider the reduced q(s,a) where the statespace is reduced;
	     * predator[i][j]prey[5][5]moves[k]
	     */
		qtable = new HashMap<String, Double>();
		// initializes Q(s,a) with input:value
		initQ(-10.0, nrPred);
	    gamma = g;
	    alpha = a;	      
	}
	
	// initializes Qtable: Q(s,a) arbitrarily with input:value
	public void initQ(double value, int nrPred) {
	    // prey fixed at (5,5)
	    Position prey = new Position(5, 5, "prey");
	    if (nrPred == 4)
	    	for(int i = 0; i < 11; i++) 
			    for(int j = 0; j < 11; j++) 
			    	for(int k=0;k<11;k++)
			    		for(int l=0;l<11;l++)
			    			for(int m=0;m<11;m++)
			    				for(int n=0;n<11;n++)
			    					for(int o=0;o<11;o++)
			    						for(int p=0;p<11;p++){
			    							ArrayList<Position> agents = new ArrayList<Position>();
			    							agents.add(new Position(i,j, "predator"));
			    							agents.add(new Position(k,l, "predator"));
			    							agents.add(new Position(m,n, "predator"));
			    							agents.add(new Position(o,p, "predator"));
			    							agents.add(prey);
			    							for (String action : actions){
			    								State s = new State(agents);
			    								// do we need to put different value according to the type of end
			    								// state here?
			    								if(s.endState() == 1 || s.endState() == 2)
			    									qtable.put(s.toString()+" "+action, 0.0);
			    								else
			    									qtable.put(s.toString()+" "+action, value);
			    							}
			    						}
			 
	    if(nrPred == 3)
	    	for(int i = 0; i < 11; i++) 
			    for(int j = 0; j < 11; j++) 
			    	for(int k=0;k<11;k++)
			    		for(int l=0;l<11;l++)
			    			for(int m=0;m<11;m++)
			    				for(int n=0;n<11;n++){
			    							ArrayList<Position> agents = new ArrayList<Position>();
			    							agents.add(new Position(i,j, "predator"));
			    							agents.add(new Position(k,l, "predator"));
			    							agents.add(new Position(m,n, "predator"));
			    							agents.add(prey);
			    							for (String action : actions){
			    								State s = new State(agents);
			    								// do we need to put different value according to the type of end
			    								// state here?
			    								if(s.endState() == 1 || s.endState() == 2)
			    									qtable.put(s.toString()+" "+action, 0.0);
			    								else
			    									qtable.put(s.toString()+" "+action, value);
			    							}
			    						}
	    if(nrPred == 2)
	    	for(int i = 0; i < 11; i++) 
			    for(int j = 0; j < 11; j++) 
			    	for(int k=0;k<11;k++)
			    		for(int l=0;l<11;l++){
			    							ArrayList<Position> agents = new ArrayList<Position>();
			    							agents.add(new Position(i,j, "predator"));
			    							agents.add(new Position(k,l, "predator"));
			    							agents.add(prey);
			    							for (String action : actions){
			    								State s = new State(agents);
			    								// do we need to put different value according to the type of end
			    								// state here?
			    								if(s.endState() == 1 || s.endState() == 2)
			    									qtable.put(s.toString()+" "+action, 0.0);
			    								else
			    									qtable.put(s.toString()+" "+action, value);
			    							}
			    						}
	    if(nrPred == 1)
	    	for(int i = 0; i < 11; i++) 
			    for(int j = 0; j < 11; j++) {
			    							ArrayList<Position> agents = new ArrayList<Position>();
			    							agents.add(new Position(i,j, "predator"));
			    							agents.add(prey);
			    							for (String action : actions){
			    								State s = new State(agents);
			    								// do we need to put different value according to the type of end
			    								// state here?
			    								if(s.endState() == 1 || s.endState() == 2)
			    									qtable.put(s.toString()+" "+action, 0.0);
			    								else
			    									qtable.put(s.toString()+" "+action, value);
			    							}
			    						}
	    }
		
	/*
	// initializes with values from valueiteration
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
	}
	*/
	public String getAction(State s){
		// get action according to policy derived from Q
		double trip = Math.random();
		if (trip < 0.2){
			//show("prey tripped!\n");
			return "wait";
		}
		State stateproj = s.projectState();
		//show(stateproj.toString()+"\n");
		String action = policy.getAction(stateproj, qtable);
		return action;
	}
	
	public void updateQ(State cs, String action, State nextS) {			
		State currentState = cs.projectState();
		State nextState = nextS.projectState();
		if(currentState.endState() == 0) {					
			double currentQ = (Double) qtable.get(currentState.toString()+" "+action);
			double qUpdated = currentQ + alpha*(getReward(nextState) 
					+ gamma*argmaxQ(nextState) - currentQ);
			qtable.put(currentState.toString()+" "+action, qUpdated);
		}
	}
	
	// argmax_a' of Q(s',a') output: value
	public double argmaxQ(State nextState) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		//State key;
		String key;
		double temp, max = 0;
		for(String action : actions) {
			//key = new State(nextState, action);
			key = nextState.toString()+" "+action; 
			temp = (Double) qtable.get(key);
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
		//State key;
		String key;
		double temp, max = 0;
		String maxAction = "wait";
		for(String action : actions) {
			//key = new State(state, action);
			key = state.toString()+" "+action; 
			temp = (Double) qtable.get(key);
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
    public double getReward(State s) {
        if(s.endState() == 1)
            return 10.0;
        else if(s.endState() == 2)
        	return -10.0;
	return 0.0;
    }
    
    // set action selection policy
    public void setSelectPolicy(PolicySelect p) {
    	PolicySelect policy = p;
    }
	
    public static void show(String s) {
        System.out.print(s);
    }
    
	public static void main(String[] args) {
	    double gamma = 0.5;
	    double alpha = 0.1;
	    // egreedy with epsilon = 0.1
	    //EGreedyPolicyTD policy = new EGreedyPolicyTD(0.1);
	    // SoftMax with temperature tau = 0.1
	    SoftMax policy = new SoftMax(0.1);
	    // qlearning with policy and nrPred
	    QLearningPrey predPolicy = new QLearningPrey(gamma, alpha, policy, 2);
	    //predPolicy.printTable(new Position(5,5,"prey"));
	}
    
    /*
     *  Print methods for table and list of statevalues
     */
	
	//todo: create new print functions according to each predator
	/*
    public void printTable(Position prey){

    	// outputs the values of all states where state:predator[i][j]prey[5][5] in a grid
    	show("\n======max state values in grid around prey"+prey.toString()+"======\n");
    	int nextline = 0;
    	State s;
    	double qValue;
    	for(int i = 0; i < 11; i++) {
    		if(nextline < i) {show("\n");}
    		for(int j = 0; j < 11; j++) {
			    	s = new State(new Position(j, i), prey);
			    	qValue = argmaxQ(s);
			    	show(String.format( "%.3f\t", qValue));
    		}
    		nextline = i;
    	}
    }*/
    /*
    public void printList(Position prey){
    	for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
			    	State s = new State(new Position(j, i), prey, action);
			    	show('\n'+s.toString()+s.getAction()+ " statevalue: " +(double)qtable.get(s));
		    	}
		    }
		}
    }*/
    /*
    public void printActionsTable(Position prey) {
    State key;
    int nextline = 0;
    State s;
    String action;
    show("\n======stateactions in grid around prey"+prey.toString()+"======\n");
    for(int i = 0; i < 11; i++) {
    	if(nextline < i) {show("\n");}
    	for(int j = 0; j < 11; j++) {
    		s = new State(new Position(j, i), prey);
    		action = argmaxQaction(s);
    		show(String.format( "%s\t", action));
    	}
    	nextline = i;
    }
	}*/
    
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
    //todo : stateactions according to the position of other predators too
    /*
    public HashMap<String, String> getStateactions() {
    	stateactions = new HashMap<String, String>();
    	State key;
        State s;
        String action;
        Position prey = new Position(5,5, "prey");
        for(int i = 0; i < 11; i++) {
        	for(int j = 0; j < 11; j++) {
        		s = new State(new Position(j, i), prey);
        		action = argmaxQaction(s);
        		stateactions.put(s.toString(), action);
        	}
        }
    	return stateactions;
    }*/
    
    //todo: according to all agents
    /*
    // outputs the state actions into a file policy.data
    public void output() throws Exception {
	File policyfile = new File("policy.data");
	policyfile.delete();
	policyfile.createNewFile();
	State state;
	String action;
	if(!qtable.isEmpty()) {
	    for(int i = 0; i < 11; i++) {
	    	for(int j = 0; j < 11; j++) {
	    		state = new State(new Position(j, i), new Position(5,5));
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
*/
	public static void setPolicyA(PolicySelect policy) {
		QLearningPrey.policy = policy;
	}    
}