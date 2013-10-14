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
    protected HashMap<String, String> stateactions;
    protected double gamma, alpha;
    //private static EGreedyPolicyTD policy;
    //private static SoftMax policy;
    private static PolicySelect policy;
    private static ArrayList<String> actions = ArbitraryPolicy.getAllActions();
    
    /*
     *  Constructors; inherits from policy evaluation
     */	

	public QLearning(double g, double a, PolicySelect p, int nrPred){
		
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
		initQ(-10.0, nrPred);
	    gamma = g;
	    alpha = a;	      
	}
	
	// initializes Qtable: Q(s,a) arbitrarily with input:value
	public void initQ(double value, int nrPred) {
	    // prey fixed at (5,5)
	    Position prey = new Position(5, 5);
	    if(nrPred==1) {
	    	for(int i = 0; i < 11; i++) {
	    		for(int j = 0; j < 11; j++) {
	    			for (String action : actions){
	    				State s = new State(prey, action);
	    				s.addPred(new Position(i,j));
	    				// init Q(s,a) = 0 
	    				if(s.endState()==1)
	    					qtable.put(s, 0.0);
	    				else
	    					qtable.put(s, value);	
	    			}
	    		}
	    	}
	    }
	    
	    if(nrPred==2) {
	    	// x
	    	for(int i = 0; i < 11; i++) {
	    		for(int j = i+1; j < 11; j++) {
	    			// y
	    			for(int k = 0; k < 11; k++) {
	    				for(int l = k+1; l < 11; l++) {
	    					for (String action : actions){
	    	    				State s = new State(prey, action);
	    	    				s.addPred(new Position(i,k));
	    	    				s.addPred(new Position(j,l));
	    	    				// init Q(s,a) = 0 
	    	    				if(s.endState()==1)
	    	    					qtable.put(s, 0.0);
	    	    				else
	    	    					qtable.put(s, value);	
	    	    			}
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    if(nrPred==3) {
	    	// x
	    	for(int i = 0; i < 11; i++) {
	    		for(int j = i+1; j < 11; j++) {
	    			for(int k = j+1; k < 11; k++) {
	    				// y
	    				for(int l = 0; l < 11; l++) {
	    					for(int m = l+1; m < 11;m++) {
	    						for(int n = m+1; n < 11;n++) {
	    							for (String action : actions){
	    								State s = new State(prey, action);
	    								s.addPred(new Position(i,l));
	    								s.addPred(new Position(j,m));
	    								s.addPred(new Position(k,n));
	    								// init Q(s,a) = 0 
	    								if(s.endState()==1)
	    									qtable.put(s, 0.0);
	    								else
	    									qtable.put(s, value);
	    							}
	    						}	
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    if(nrPred==4) {
	    	// x
	    	for(int i = 0; i < 11; i++) {
	    		for(int j = i+1; j < 11; j++) {
	    			for(int k = j+1; k < 11; k++) {
	    				for(int l = k+1; l < 11; l++) {
	    					// y
	    					for(int m = 0; m < 11;m++) {
	    						for(int n = m+1; n < 11;n++) {
	    							for(int o = n+1; o < 11;o++) {
	    								for(int p = o+1; p < 11;p++) {
	    									for (String action : actions){
	    										State s = new State(prey, action);
	    										s.addPred(new Position(i,m));
	    										s.addPred(new Position(j,n));
	    										s.addPred(new Position(k,o));
	    										s.addPred(new Position(l,p));
	    										// init Q(s,a) = 0 
	    												if(s.endState()==1)
	    													qtable.put(s, 0.0);
	    												else
	    													qtable.put(s, value);
	    									}
	    								}
	    							}	
	    						}
	    					}
	    				}
	    			}
	    		}
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
		if(!currentState.endState()) {					
			double currentQ = (Double) qtable.get(currentState);
			double qUpdated = currentQ + alpha*(getReward(nextState) 
					+ gamma*argmaxQ(nextState) - currentQ);
			qtable.put(currentState, qUpdated);
		}
	}
	
	// argmax_a' of Q(s',a') output: value
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
	
	// argmax_a' of Q(s',a') output: action
	public String argmaxQaction(State state) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		State key;
		double temp, max = 0;
		String maxAction = "wait";
		for(String action : actions) {
			key = new State(state, action);
			temp = (Double) qtable.get(key);
    		if(temp>max) {
    			max = temp;
    			maxAction = action;
    		}
    	}
		return maxAction;
	}
	
    // implement reward function: only when captured the immediate award=10, else 0
    public double getReward(State s) {
        if(s.endState())
            return 10.0;
	return 0.0;
    }
    
    // set action selection policy
    public void setSelectPolicy(PolicySelect p) {
    	PolicySelect policy = p;
    }*/
	
    public static void show(String s) {
        System.out.print(s);
    }
      
    /*
     *  Print methods for table and list of statevalues
     *    
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
	}
    
    /*
     *  IO methods, for writing the state actions into a file, 
     *  which can be used to fill up a lookup table when the policy 
     *  is executed within the simulator 
     *
    
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
    
    public HashMap<String, String> getStateactions() {
    	stateactions = new HashMap<String, String>();
    	State key;
        State s;
        String action;
        Position prey = new Position(5,5);
        for(int i = 0; i < 11; i++) {
        	for(int j = 0; j < 11; j++) {
        		s = new State(new Position(j, i), prey);
        		action = argmaxQaction(s);
        		stateactions.put(s.toString(), action);
        	}
        }
    	return stateactions;
    }
    
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
    }*/
		
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
	    EGreedyPolicyTD policy = new EGreedyPolicyTD(0.1);
	    // SoftMax with temperature tau = 0.1
	    //SoftMax policy = new SoftMax(0.1);
	    // qlearning with policy
	    QLearning predPolicy = new QLearning(gamma, alpha, policy, 4);
	    show("qtable size: "+predPolicy.getQtable().size());
	    //predPolicy.printTable(new Position(5,5));
	}
}