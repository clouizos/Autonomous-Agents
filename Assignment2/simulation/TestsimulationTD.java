package simulation;

import java.util.*;
import java.io.*;
import policy.TestPolicy;

import policy.*;
import statespace.*;
/*
 * Implementation of the prey predator domain
 * Each policy can be executed for 100 runs and it will outputs it's preformance.
 * Run the specific policy first for output of a policy.data file; then run the simulator
 */
public class TestsimulationTD {
	// init 
	static int runs = 0;
	static ArrayList<Integer> allRuns = new ArrayList<Integer>();
	static double averageRuns = 0;
	static int timesRun = 0;

	static Position predator;
	static Position prey;
	static boolean resetGrid = false;
	
	 
    public TestsimulationTD() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    // State which policies the simulator is run
    double gamma = 0.8;
    double alpha = 0.1;
    // egreedy with epsilon = 0.1
    EGreedyPolicyTD policy = new EGreedyPolicyTD(0.1);
    // SoftMax with temperature tau = 0.1
    //SoftMax policy = new SoftMax(0.1);
    // qlearning with input:policy
    QLearning predPolicy = new QLearning(gamma, alpha, policy);
    //Sarsa predPolicy = new Sarsa(gamma, alpha, policy);
    Policy preyPolicy = new RandomPolicyPrey();
	
	/* fill look up table if Value iteration Policy is run
	File file = new File("policy.data");
	try {
		predPolicy.filltable(file);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
    boolean verbose=false;
    int nrRuns = 100000;
    testQ(predPolicy, preyPolicy, verbose, nrRuns);
    //testSarsa(predPolicy, preyPolicy, verbose, nrRuns);
    predPolicy.printTable(new Position(5,5));
    predPolicy.printActionsTable(new Position(5,5));
    }
    
    // TODO: Change to Sarsa
    public static void testSarsa(Policy predPolicy, Policy preyPolicy, boolean verbose, int nrRuns) {
    	State currentState = initS();
    	State oldState;
    	String predmove = predPolicy.getAction(currentState);
    	currentState.setAction(predmove);
    	String preymove;
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			runs = 0;
    			show("\nResetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			currentState = initS();
    			resetGrid = false;
    			predmove = predPolicy.getAction(currentState);
    	    	currentState.setAction(predmove);
    			//pauseProg();
    		}

    		//show(currentState.getPrey().toString()+" start rel coordinates of prey at begin loopbody");
    		if(verbose) {
    		show("\n===========\nAt beginloop: Predator "+ predator.toString());
    		show("At beginloop: Prey "+ prey.toString()+'\n');
    		}
    		
    		//predator move on new state(prey)
    		//updates the state according to predator move
    		oldState = new State(currentState, predmove);
    		predator = predator.move(predmove);
    		currentState.setPredator(predator);
    		if(verbose) {
    		show("\npredator moved: "+predmove);
    		show("Predator: " + predator.toString());
    		}
    		
    		//prey move
    		//updates the state upon the prey move
    		preymove = preyPolicy.getAction(currentState);
    		currentState.setAction(preymove);
    		prey = prey.move(preymove);
    		// update state after prey moves
    		currentState.setPrey(prey);
    		if(verbose) {
    		show("prey move: "+preymove);
    		show("Prey: " + prey.toString());
    		}
			((Sarsa)predPolicy).updateQ(oldState, currentState);
    		predmove = predPolicy.getAction(currentState);
    		if(currentState.endState()){
    			show("\nPredator catched the prey in "+runs+" runs!");
    			timesRun++;
    			allRuns.add(runs);
    			resetGrid = true;
    		}else{
    			runs++;
    		}
    		//pauseProg();
    	}
	
    //((QLearning)predPolicy).printTable(new Position(5,5));
    //((QLearning)predPolicy).printList(new Position(5,5));
	System.out.println("\nAll runs overview:");
	System.out.println(allRuns);
	
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation is: "+stdDev);
    }
    
    public static void testQ(Policy predPolicy, Policy preyPolicy, boolean verbose, int nrRuns) {
    	State currentState = initS();
    	State oldState;
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			runs = 0;
    			//show("\nResetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			currentState = initS();
    			resetGrid = false;
    			//pauseProg();
    		}

    		//show(currentState.getPrey().toString()+" start rel coordinates of prey at begin loopbody");
    		if(verbose) {
    		show("\n===========\nAt beginloop: Predator "+ predator.toString());
    		show("At beginloop: Prey "+ prey.toString()+'\n');
    		}
    		
    		//predator move on new state(prey)
    		//updates the state according to predator move
    		String move = predPolicy.getAction(currentState);
    		predator = predator.move(move);
    		oldState = new State(currentState, move);
    		currentState.setPredator(predator);
    		if(verbose) {
    		show("\npredator moved: "+move);
    		show("Predator: " + predator.toString());
    		}
    		
    		//prey move
    		//updates the state upon the prey move
    		move = preyPolicy.getAction(currentState);
    		prey = prey.move(move);
    		// update state after prey moves
    		currentState.setPrey(prey);
    		if(verbose) {
    		show("prey move: "+move);
    		show("Prey: " + prey.toString());
    		}
    		((QLearning)predPolicy).updateQ(oldState, currentState);
    		
    		
    		if(currentState.endState()){
    			//show("\nPredator catched the prey in "+runs+" runs!");
    			timesRun++;
    			allRuns.add(runs);
    			resetGrid = true;
    			if((timesRun%500)==0) {
    				TestPolicy optimal = new TestPolicy();
    				HashMap test = ((QLearning)predPolicy).getStateactions();
    				double delta = optimal.optimality(test);
    				show("\nRuns: "+timesRun+ " optimality: "+delta);
    				if(delta>0.80) {
    				((QLearning)predPolicy).printActionsTable(new Position(5,5));
    				}
    			}
    		}else{
    			runs++;
    		}
    		//pauseProg();
    	}
	
    //((QLearning)predPolicy).printTable(new Position(5,5));
    //((QLearning)predPolicy).printList(new Position(5,5));
	//System.out.println("\nAll runs overview:");
	//System.out.println(allRuns);
	try {
	    output();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation is: "+stdDev);
	TestPolicy optimal = new TestPolicy();
	HashMap test = ((QLearning)predPolicy).getStateactions();
	double delta = optimal.optimality(test);
	show("\nRuns: "+timesRun+ " optimality: "+delta);
    }
    
	/*
	 * initializes S; choose a start state from the qtable
	 * 
	 */
	static State initS() {
		Random gen = new Random();
		predator = new Position(gen.nextInt(11), gen.nextInt(11));
		prey = new Position(gen.nextInt(11), gen.nextInt(11));
		State start = new State(predator, prey);
		return start;
	}
    
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
    public static void output() throws Exception {
	File policyfile = new File("convergenceQLearning.data");
	policyfile.delete();
	policyfile.createNewFile();
	//System.out.println("trying to write");
	if(!allRuns.isEmpty()) {
		//System.out.println(allRuns);
		for(int times : allRuns){
	    //Enumeration enu = stateactions.keys();
	    //while(enu.hasMoreElements()) {
		//String state = (String) enu.nextElement();
		//String action = (String) stateactions.get(state);
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