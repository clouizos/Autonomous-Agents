package policy;
import java.io.*;
import java.util.*;

import statespace.*;

import java.util.Random;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import scpsolver.util.SparseVector;

/*import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
*/
public class MinimaxQLearning implements Policy {
	
	/* reduced statespace state[i][j][5][5]
	 * where predator[i][j] prey[5][5]
	 */
	
	/* gamma = discount factor (0.8)
	 * alpha = small positive number; learning rate
	 */
    protected HashMap<MinimaxState, Double> qtable;
    protected HashMap<State, Double> vtable;
    protected HashMap<String, String> stateactions;
    protected double gamma, alpha;
    private double initQValue;
    //private static EGreedyPolicyTD policy;
    //private static SoftMax policy;
    //private static PolicySelect policy;
    private static EGreedyMN policy;
    private static ArrayList<String> actions = PolicySelect.getAllActions();
    protected String agent;
    private int maxRuns; //if pred can't catch until this runs, he is defeated
    LinearProgramSolver solver;
    
    /*
     *  Constructors; inherits from policy evaluation
     */	
    
    public void updateV(MinimaxState s, double v){
    	State st= new State(s.getPredators(), s.getPrey(), "wait");
    	vtable.put(st, v);
    }

	public MinimaxQLearning(double g, double a, EGreedyMN p, int maxRuns, int nrPred, String entity){
		
		// policy could be e-greedy or softmax
	    //policy = (EGreedyPolicyTD) p;
	    //policy = (SoftMax) p;
		policy = p;
	    /* qtable init
	     * qtable consist of all possible states+actions
	     * we consider the reduced q(s,a) where the statespace is reduced;
	     * predator[i][j]prey[5][5]moves[k]
	     */
		qtable = new HashMap<MinimaxState, Double>();
		vtable = new HashMap<State, Double>();
		// initializes Q(s,a) with input:value
		//initQ(-10.0, nrPred);
		//initQValue = -10.0;
		
		// in the paper it says to initialize Q's with 1
		// but i changed it to 10 since it should be a zero-sum
		// game hence the initial value should be the maximum 
		// reward we can have, and that's 10
		initQValue = 10;
	    gamma = g;
	    alpha = a;
	    agent = entity;
	    solver  = SolverFactory.getSolver("LPSOLVE");
	    this.maxRuns = maxRuns;
	}
	
	// initializes Qtable: Q(s,a) arbitrarily with input:value
	public void initQ(double value, int nrPred) {
	    // prey fixed at (5,5)
	    Position prey = new Position(5, 5);
	    if(nrPred==1) {
	    	for(int i = 0; i < 11; i++) 
	    		for(int j = 0; j < 11; j++) {
	    			State st = new State(prey);
	    			st.addPred(new Position(i,j));
	    			vtable.put(st, 1.0);
	    			for (String action : actions){
	    				for (String action_opp : actions){
	    					MinimaxState s = new MinimaxState(prey, action, action_opp);
	    					s.addPred(new Position(i,j));
	    					// init Q(s,a) = 0 
	    					if(s.endState()==(1|-1))
	    						qtable.put(s, 0.0);
	    					else
	    						qtable.put(s, value);
	    				}
	    			}
	    		}
	    }
	}
	
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
		//String action = policy.getAction(stateproj, qtable);
		String action = policy.getActionMM(stateproj);
		return action;
	}
	
	public void updateQ(MinimaxState cs, MinimaxState nextS, int runs) {			
		MinimaxState currentState = (MinimaxState) cs.projectState();
		MinimaxState nextState = (MinimaxState) nextS.projectState();
		double[] rewards = getReward(nextState, runs);
		double reward;
		if(!(currentState.endState()==(-1|1))) {					
			Double currentQ = (Double) qtable.get(currentState);
			if(currentQ == null){
				currentQ = initQValue;
				qtable.put(currentState,initQValue);
			}
			if(agent.equals("prey"))
				reward = rewards[1];
			else
				reward = rewards[0];
			Double upd = vtable.get(nextState.toState());
			if(upd == null){
				vtable.put(nextState.toState(), initQValue);
				upd = initQValue;
			}
			double qUpdated = currentQ + alpha*(reward 
					+ gamma*upd - currentQ);
			qtable.put(currentState, qUpdated);
		}
	}
	
	// argmax_a' of Q(s',a') output: value
	public double argmaxQ(MinimaxState nextState) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		MinimaxState key;
		Double temp; 
		double max = 0;
		for(String action : actions) {
			for(String opponent_action : actions){
				key = new MinimaxState(nextState, action, opponent_action);
				temp = (Double) qtable.get(key);
				if(temp == null){
					temp = initQValue;
					qtable.put(key, initQValue);
				}
				if(temp>max) {
					max = temp;
				}
			}
    	}
		return max;
	}
	
	// argmax_a' of Q(s',a') output: action
	public String argmaxQaction(MinimaxState state) {
		long seed = System.nanoTime();
		Collections.shuffle(actions, new Random(seed));
		MinimaxState key;
		Double temp;
		double max = 0;
		String maxAction = "wait";
		for(String action : actions) {
			for (String opponent_action : actions){
				key = new MinimaxState(state, action, opponent_action);
				temp = (Double) qtable.get(key);
				if(temp == null){
					temp = initQValue;
					qtable.put(key, initQValue);
				}
				if(temp>max) {
					max = temp;
					maxAction = action;
				}
			}
    	}
		return maxAction;
	}
	
    // implement reward function: 
	// first check if predators got confused then immediate award = -10
	// else if captured then immediate award=10, else 0
    public double[] getReward(State s, int runs) {
        if(runs == maxRuns){
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
    
    public double[] LinearProgramPi(MinimaxState s){
    	
    	double[][] Q = new double[5][5];
    	for(int i=0; i<actions.size();i++){
    		for (int j=0; j<actions.size(); j++){
    			MinimaxState key = new MinimaxState(s, actions.get(i), actions.get(j));
    			Double temp = qtable.get(key);
    			if (temp == null){
    				temp = initQValue;
    				qtable.put(key, temp);
    			}
    			Q[j][i] = temp;
    		}
    	}
    	/*
    	LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {0, 0, 0, 0, 0, -1}, 0);
    	Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
    	constraints.add(new LinearConstraint(new double[] {Q[0][0], Q[0][1], Q[0][2], Q[0][3], Q[0][4]}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {Q[1][0], Q[1][1], Q[1][2], Q[1][3], Q[1][4]}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {Q[2][0], Q[2][1], Q[2][2], Q[2][3], Q[2][4]}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {Q[3][0], Q[3][1], Q[3][2], Q[3][3], Q[3][4]}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {Q[4][0], Q[4][1], Q[4][2], Q[4][3], Q[4][4]}, Relationship.GEQ, 0));
    	
    	constraints.add(new LinearConstraint(new double[] {1,1,1,1,1}, Relationship.EQ, 1));
    	constraints.add(new LinearConstraint(new double[] {1,0,0,0,0}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {0,1,0,0,0}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {0,0,1,0,0}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {0,0,0,1,0}, Relationship.GEQ, 0));
    	constraints.add(new LinearConstraint(new double[] {0,0,0,0,1}, Relationship.GEQ, 0));
    	
    	RealPointValuePair solution = null;
    	
    	try{
    		solution = new SimplexSolver().optimize(f, constraints, GoalType.MINIMIZE, true);
    	}catch(Exception e){
    		System.out.println(e);
    	}
    	
    	double[] sol = new double[5];
    	sol[0] = solution.getPoint()[0];
    	sol[1] = solution.getPoint()[1];
    	sol[2] = solution.getPoint()[2];
    	sol[3] = solution.getPoint()[3];
    	sol[4] = solution.getPoint()[4];
    	//sol[5] = solution.getPoint()[5];
    	 * 
    	 */
    	
    	LinearProgram f = new LinearProgram(new double[] {0, 0, 0, 0, 0, - 1});
    	
    	f.setMinProblem(true);
    	
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {Q[0][0], Q[0][1], Q[0][2], Q[0][3], Q[0][4], -1}, 0, "c1"));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {Q[1][0], Q[1][1], Q[1][2], Q[1][3], Q[1][1], -1}, 0, "c2"));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {Q[2][0], Q[2][1], Q[2][2], Q[2][3], Q[2][2], -1}, 0, "c3"));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {Q[3][0], Q[3][1], Q[3][2], Q[3][3], Q[3][3], -1}, 0, "c4"));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {Q[4][0], Q[4][1], Q[4][2], Q[4][3], Q[4][4], -1}, 0, "c5"));
    	
    	f.addConstraint(new LinearEqualsConstraint(new double[] {1,1,1,1,1,0}, 1, "c6" ));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {1,0,0,0,0, 0}, 0, "c7" ));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {0,1,0,0,0, 0}, 0, "c8" ));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {0,0,1,0,0, 0}, 0, "c9" ));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {0,0,0,1,0, 0}, 0, "c10" ));
    	f.addConstraint(new LinearBiggerThanEqualsConstraint(new double[] {0,0,0,0,1, 0}, 0, "c11" ));
    	
    	
    	double[] sol = solver.solve(f);
    	
    	//for (double sols : sol){
    	//	System.out.println(sols);
    	//}
    	
    	return sol;

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
*/
    
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
    
    /*
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
		
	//public static void setPolicyA(PolicySelect policy) {
	//	MinimaxQLearning.policy = policy;
	//}

	public HashMap<MinimaxState, Double> getQtable() {
		return qtable;
	}

	public void setQtable(HashMap<MinimaxState, Double> qtable) {
		this.qtable = qtable;
	} 
	
	public static void main(String[] args) {
	    double gamma = 0.5;
	    double alpha = 0.1;
	    // egreedy with epsilon = 0.1
	    //EGreedyPolicyTD policy = new EGreedyPolicyTD(0.1,15.0);
	    // SoftMax with temperature tau = 0.1
	    //SoftMax policy = new SoftMax(0.1);
	    // qlearning with policy
	    //MinimaxQLearning predPolicy = new MinimaxQLearning(gamma, alpha, policy, 3, "prey");
	    //show("qtable size: "+predPolicy.getQtable().size());
	    //predPolicy.printTable(new Position(5,5));
	}
}