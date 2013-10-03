package policy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

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
    protected HashMap<String, String> stateactions = new HashMap<String, String>();
    protected double gamma, alpha;
    //private static EGreedyPolicyTD policy;
    private static SoftMax policy;
    private static ArrayList<String> actions = ArbitraryPolicy.getAllActions();
    
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearning(double g, double a, Policy p){
		
		// policy could be e-greedy or softmax
	    //policy = (EGreedyPolicyTD) p;
	    policy = (SoftMax) p;
	    /* qtable init
	     * qtable consist of all possible states+actions
	     * we consider the reduced q(s,a) where the statespace is reduced;
	     * predator[i][j]prey[5][5]moves[k]
	     */
		qtable = new HashMap<State, Double>();
		// initializes Q(s,a) with input:value
		initQ(0.0);
	    gamma = g;
	    alpha = a;	      
	}
	
	public static void main(String[] args) {
	    double gamma = 0.5;
	    double alpha = 0.1;
	    // egreedy with epsilon = 0.1
	    // EGreedyPolicyTD policy = new EGreedyPolicyTD(0.1);
	    // SoftMax with temperature tau = 0.1
	    SoftMax policy = new SoftMax(0.1);
	    // qlearning with policy
	    QLearning predPolicy = new QLearning(gamma, alpha, policy);
	    predPolicy.printTable(new Position(5,5));
	}
	
	// initializes Qtable: Q(s,a) arbitrarily with input:value
	public void initQ(double value) {
	    // prey fixed at (5,5)
	    Position prey = new Position(5, 5);
		for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
			    	State s = new State(new Position(i, j), prey, action);
			    	// init Q(s,a) = 0 
			    	qtable.put(s, value);	
		    	}
		    }
		}
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
    
    /*
     *  Print methods for table and list of statevalues
     */    
    public void printTable(Position prey){

    	// outputs the values of all states where state:predator[i][j]prey[5][5] in a grid
    	show("\n======statevalues in grid around prey"+prey.toString()+"======\n");
    	int nextline = 0;
    	for(int i = 0; i < 11; i++) {
    		if(nextline < i) {show("\n");}
    		for(int j = 0; j < 11; j++) {
    			for (String action : actions){
			    	State s = new State(new Position(j, i), prey, action);
			    	show(String.format( "%.3f", (double)qtable.get(s))+ " ");
    			}	
    		}
    		nextline = i;
    	}
    }
    
    public void printList(Position prey){
    	for(int i = 0; i < 11; i++) {
		    for(int j = 0; j < 11; j++) {
		    	for (String action : actions){
			    	State s = new State(new Position(j, i), prey, action);
			    	show('\n'+s.toString()+s.getAction()+ " statevalue: " +(double)qtable.get(s));
		    	}
		    }
		}
    }
    
    public void printActionsTable() {
    State key;
    int y = 0;
    State state;
    String action;
    if(!qtable.isEmpty()) {
	    Enumeration enu = Collections.enumeration(qtable.keySet());
	    while(enu.hasMoreElements()) {
		state = (State) enu.nextElement();
		if(state.getAction()==null) show("NULLL");
		action = state.getAction();
		stateactions.put(state.toString(), action);
	    }
	    for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				key = new State(new Position(j,i), new Position(5,5));
				String action2 = stateactions.get(key.toString());
				//State keyQ = new State(new Position(j,i), new Position(5,5), action);
				System.out.printf("%s\t", action2);
			}
			System.out.println();	
			}
	} else
	    show("Qtable table is empty!!");
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
    
    // outputs the state actions into a file policy.data
    public void output() throws Exception {
	File policyfile = new File("policy.data");
	policyfile.delete();
	policyfile.createNewFile();
	String state;
	String action;
	if(!stateactions.isEmpty()) {
	    Enumeration enu = Collections.enumeration(stateactions.keySet());
	    while(enu.hasMoreElements()) {
		state = (String) enu.nextElement();
		action = (String) stateactions.get(state);
		try {
		    write(policyfile, state+"=>"+action+"\n", true);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    System.out.println("Cannot write to file");
		}
	    }
	} else
	    show("State actions table is empty!!");
    }
}