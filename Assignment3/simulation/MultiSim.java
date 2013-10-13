package simulation;

import java.util.*;
import java.io.*;

import policy.*;
import statespace.*;
/*
 * Implementation of the prey predator domain
 * Each policy can be executed for 100 runs and it will outputs it's preformance.
 * Run the specific policy first for output of a policy.data file; then run the simulator
 */
public class MultiSim {
	// init 
	static int runs = 0;
	static ArrayList<Integer> allRuns = new ArrayList<Integer>();
	static ArrayList<Integer> allRunsconf = new ArrayList<Integer>();
	static double averageRuns = 0;
	static int timesRun = 0;
	static double delta;
	//static ArrayList<Double> optimalities = new ArrayList<Double>();

	static boolean resetGrid = false;
	//static boolean discount = true;
	//static double parameter;
	//static String arg = "softmax";
    //static String arg = "egreedy";
	static HashMap<Position, Policy> agentPolicies;
	 
    public MultiSim() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    // State which policies the simulator is run
       
    // egreedy with epsilon
    /*
    double epsilon = 0.1;
    double alpha = 0.45;
    double gamma = 0.45;
    double tau = 0.0001;
    parameter = epsilon;
    EGreedyPolicyTD policy = new EGreedyPolicyTD(epsilon);
    */
    // SoftMax with temperature tau
    //SoftMax policy = new SoftMax(tau);
    // qlearning with input:policy
    //QLearning predPolicy = new QLearning(gamma, alpha, policy);
    //Sarsa predPolicy = new Sarsa(gamma, alpha, policy);
    //Policy preyPolicy = new RandomPolicyPrey();
	
	/* fill look up table if Value iteration Policy is run
	File file = new File("policy.data");
	try {
		predPolicy.filltable(file);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
    boolean verbose=true;
    int nrRuns = 20000;
    int nrPred = 3;
    testRandom(verbose, nrRuns, nrPred);
    //testSarsa(predPolicy, preyPolicy, verbose, nrRuns);
    //predPolicy.printTable(new Position(5,5));
    //predPolicy.printActionsTable(new Position(5,5));
	/*
    try {
	    output(parameter, alpha, gamma, arg);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}*/
    }
       
    public static void testRandom(boolean verbose, int nrRuns, int nrPred) {
    	State currentState = reset(nrPred);
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			runs = 0;
    			//show("\nResetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			currentState = reset(nrPred);
    			resetGrid = false;
    			//pauseProg();
    		}
    		for (Map.Entry<Position, Policy> entry : agentPolicies.entrySet()) {
    		    Position agent = entry.getKey();
    		    Policy policy = entry.getValue();
    		    String move = policy.getAction(currentState);
    		    agent.move(move);
    		}    		
    		
     		if(currentState.endState() == -1 || currentState.endState() == 1){
    			if (currentState.endState() == -1){
    				show("predators confused after "+runs+" runs!");
    				allRunsconf.add(runs);
    				show(""+timesRun);
    			}
    			else{
    				show("prey captured after "+runs+" runs!");
    				allRuns.add(runs);
    				show(""+timesRun);
    			}
    			timesRun++;
    			resetGrid = true;
    		}else{
    			runs++;
    				if(verbose)
    				show("new iteration");
    		}
    		//pauseProg();
    	}
	
    //((QLearning)predPolicy).printTable(new Position(5,5));
    //((QLearning)predPolicy).printList(new Position(5,5));
	//System.out.println("\nAll runs overview:");
	//System.out.println(allRuns);
	
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation is: "+stdDev);
	show("\nRuns: "+timesRun);
    }
    
	/*
	 * initializes S; choose a start state from the qtable
	 * 
	 */
	static State reset(int nrPred) {
		agentPolicies.clear();
		State start = new State();
		Policy predPolicy = new RandomPolicyPredator();
		Policy preyPolicy = new RandomPolicyPrey();
		for (int i=0; i< nrPred; i++){
			if (i==0){
				agentPolicies.put(new Position(0,0), predPolicy);
				start.addAgent(new Position(0,0));
			}else if(i==1){
				agentPolicies.put(new Position(10,0), predPolicy);
				start.addAgent(new Position(10,0));
			}else if(i==2){
				agentPolicies.put(new Position(0,10), predPolicy);
				start.addAgent(new Position(0,10));
			}else if (i==3){
				agentPolicies.put(new Position(10,10), predPolicy);
				start.addAgent(new Position(10,10));
			}
		}
		agentPolicies.put(new Position(5,5), preyPolicy);
		start.addAgent(new Position(5,5));
		return start;
	}
	
	/*
	public static PolicySelect discounted(double nrRuns) {
		double stepsize = parameter/Math.pow(timesRun,2);
		show("stepsize: "+stepsize);
		if(arg.equals("softmax"))
			return new SoftMax(stepsize);
		else
			return new EGreedyPolicyTD(stepsize);
		
	}*/
    
    static double getAverage(ArrayList<Integer> allRuns){
    	double average = 0.0;
    	for (int i=0;i<allRuns.size();i++){
    		average += allRuns.get(i);
    	}
    	average = average/allRuns.size();
    	System.out.println("Average time for the predator to catch the prey is: "+ average+" !");
    	return average;
    }
    
    static double getVariance(ArrayList<Integer> allRuns)
    {
        double average = getAverage(allRuns);
        double temp = 0;
        for(int a :allRuns)
            temp += (average-a)*(average-a);
        	return temp/allRuns.size();
    }

    static double getStdDev(ArrayList<Integer> allRuns)
    {
        return Math.sqrt(getVariance(allRuns));
    }

    public static void show(String s) {
        System.out.println(s);
    }
    
    public static void pauseProg(){
    	System.out.println("Press enter to continue...");
    	Scanner keyboard = new Scanner(System.in);
    	keyboard.nextLine();
    }
    
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
    public static void output(double parameter, double a, double g, String arg) throws Exception {
	File policyfile = new File("convS_"+parameter+'_'+a+'_'+g+arg+".data");
	policyfile.delete();
	policyfile.createNewFile();
	File optfile = new File("optimalityS_"+parameter+'_'+a+'_'+g+arg+".data");
	optfile.delete();
	optfile.createNewFile();
	//System.out.println("trying to write");
	if(!allRuns.isEmpty()) {
		//System.out.println(allRuns);
		for(int times : allRuns){
			try {
				write(policyfile, String.valueOf(times)+"\n", true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Cannot write to file");
			}
		}
	} else
	    show("\nRuns each episode is empty!!");
    }
}