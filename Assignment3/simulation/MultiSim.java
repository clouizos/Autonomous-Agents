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
	static double parameter;
	static LinkedHashMap<Position, Policy> predPolicies;
	static Position prey;
	
	static double epsilon = 0.1;
	static double initQ = 15.0;
    static double alpha = 0.2;
    static double gamma = 0.1;
    static double tau = 0.0001;
    static EGreedyPolicyTD policy = new EGreedyPolicyTD(epsilon, initQ);
    static String method = "q";
    //static String method = "mQQ";
    
    public MultiSim() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    // State which policies the simulator is runpredPolicies.entrySet()
       
    // egreedy with epsilon
    
    
    // SoftMax with temperature tau
    //SoftMax policy = new SoftMax(tau);
    		
    boolean verbose=false;
    int nrRuns = 20000;
    int nrPred = 2;
    parameter = epsilon;
    //parameter = tau;
    String arg = "Q_egreedy_"+nrPred+'_'+nrRuns;
    //String arg = "minimaxQ_egreedy_"+nrPred+'_'+nrRuns;
    
    
    // qlearning with input:policy
    //QLearning predPolicy = new QLearning(gamma, alpha, policy, nrPred,"predator");
    QLearning preyPolicy = new QLearning(gamma, alpha, policy, nrPred,"prey");
    //Sarsa predPolicy = new Sarsa(gamma, alpha, policy);
    //testRandom(verbose, nrRuns, nrPred);
    testQ(verbose, nrRuns, nrPred, preyPolicy);
    //testSarsa(predPolicy, preyPolicy, verbose, nrRuns);
	
    try {
	    output(parameter, alpha, gamma, arg);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static void testQ(boolean verbose, int nrRuns, int nrPred, Policy preyPolicy) {
    	State currentState = initShared(nrPred, method);
    	ArrayList<String> predmoves = new ArrayList<String>();
    	//currentState.sortPreds();
    	State oldstate; // = new State(currentState.getPredators(), currentState.getPrey(), "wait");
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			runs = 0;
    			//show("\nResetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			reset(nrPred);
    			//currentState.sortPreds();
    			resetGrid = false;
    			//pauseProg();
    		}
    		if(verbose) {
    		show("\n===========\nAt beginloop: "+currentState.toString());
    		show("predators: "+currentState.getPredators().size());
    		}

	    	oldstate = new State(currentState.getPredators(), currentState.getPrey(), "wait");
	    	
    		// make every predator do a move and put in a list
    		for (Map.Entry<Position, Policy> entry : predPolicies.entrySet()) {
    		    Position predator = entry.getKey();
    		    Policy policy = entry.getValue();
    		    String predmove = policy.getAction(oldstate);
    		    predmoves.add(predmove);
        		if(verbose) {
            		show("predator move: " + predmove);
            	}
    		}
    		
    		// make prey do a move
    		String preymove = preyPolicy.getAction(oldstate);
    		prey.move(preymove);
    		
    		if(verbose) {
        		show("prey move: " + preymove);
        	}
    		
    		// doing actual move to update the currentState
    		int i = 0;
    		for(Position pred : predPolicies.keySet()) {
    			pred.move(predmoves.get(i++));
    		}
    		    		
    		// update qtable according to oldstate, currentState
    		i = 0;
    		for (Policy policy : predPolicies.values()) {
    			State oldState = new State(oldstate.getPredators(), oldstate.getPrey(), predmoves.get(i++));
    		    ((QLearning)policy).updateQ(oldState, currentState);
    		}
    		
    		// predmoves clear
    		predmoves.clear();
    		
    		if(verbose) {
    		show("\n===========\nAt endloop: "+currentState.toString());
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
	
    	System.out.println("confused:"+allRunsconf.size());
    	System.out.println("catched:"+allRuns.size());
        //((QLearning)predPolicy).printTable(new Position(5,5));
        //((QLearning)predPolicy).printList(new Position(5,5));
    	
    	double stdDev = getStdDev(allRuns);
    	System.out.println("The standard deviation for catching is: "+stdDev);
    	
    	double stdDev2 = getStdDev(allRunsconf);
    	System.out.println("The standard deviation for confused is: "+stdDev2);
    }
    /*   
    public static void testRandom(boolean verbose, int nrRuns, int nrPred) {
    	Policy preyPolicy = new RandomPolicyPrey();
    	Policy predPolicy = new RandomPolicyPredator();
    	//State currentState = init(nrPred, predPolicy);
    	State oldstate;
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			runs = 0;
    			//show("\nResetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			reset(nrPred);
    			resetGrid = false;
    			//pauseProg();
    		}
    		if(verbose) {
    		show("\n===========\nAt beginloop: "+currentState.toString());
    		show("predators: "+currentState.getPredators().size());
    		}
    		
    		// make every predator do a move
    		for (Map.Entry<Position, Policy> entry : predPolicies.entrySet()) {
    		    Position predator = entry.getKey();
    		    Policy policy = entry.getValue();
    		    String predmove = policy.getAction(currentState);
    		    predator.move(predmove);
        		if(verbose) {
            		show("predator move: " + predmove);
            	}
    		}
    		
    		// make prey do a move
    		String preymove = preyPolicy.getAction(currentState);
    		prey.move(preymove);
    		if(verbose) {
        		show("prey move: " + preymove);
        	}
    		// update the predator positions
    		ArrayList<Position> predators;
    		predators = new ArrayList<Position>(predPolicies.keySet());
    		currentState.setPredators(predators);
    		
    		if(verbose) {
    		show("\n===========\nAt endloop: "+currentState.toString());
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
    		pauseProg();
    	}
	
    //((QLearning)predPolicy).printTable(new Position(5,5));
    //((QLearning)predPolicy).printList(new Position(5,5));
	//System.out.println("\nAll runs overview:");
	//System.out.println(allRuns);
	
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation is: "+stdDev);
	show("\nRuns: "+timesRun);
    }*/
    
	/*
	 * initializes S; choose a start state from the qtable
	 * 
	 */
	static State init(int nrPred, String method) {
		predPolicies = new LinkedHashMap<Position, Policy>();
		// with independent learning every predator should have its own policy
		prey = new Position(5,5);
		State start = new State(prey);
		for (int i=0; i< nrPred; i++){
			if (i==0){
				Position pred1 = new Position(0,0);
				if(method.equals("q"))
					predPolicies.put(pred1, new QLearning(gamma, alpha, policy, nrPred,"predator"));
				else
					predPolicies.put(pred1, new MinimaxQLearning(gamma, alpha, policy, nrPred,"predator"));
				start.addPred(pred1);
			}else if(i==1){
				Position pred2 = new Position(0,10);
				if(method.equals("q"))
					predPolicies.put(pred2, new QLearning(gamma, alpha, policy, nrPred,"predator"));
				else
					predPolicies.put(pred2, new MinimaxQLearning(gamma, alpha, policy, nrPred,"predator"));
				start.addPred(pred2);
			}else if(i==2){
				Position pred3 = new Position(10,0);
				if(method.equals("q"))
					predPolicies.put(pred3, new QLearning(gamma, alpha, policy, nrPred,"predator"));
				else
					predPolicies.put(pred3, new MinimaxQLearning(gamma, alpha, policy, nrPred,"predator"));
				start.addPred(pred3);
			}else if (i==3){
				Position pred4 = new Position(10,10);
				if(method.equals("q"))
					predPolicies.put(pred4, new QLearning(gamma, alpha, policy, nrPred,"predator"));
				else
					predPolicies.put(pred4, new MinimaxQLearning(gamma, alpha, policy, nrPred,"predator"));
				start.addPred(pred4);
			}
		}
		return start;
	}
	
	/*
	 * initializes S; choose a start state from the qtable
	 * 
	 */
	static State initShared(int nrPred, String method) {
		predPolicies = new LinkedHashMap<Position, Policy>();
		// with independent learning every predator should have its own policy
		prey = new Position(5,5);
		State start = new State(prey);
		QLearning pQ = new QLearning(gamma, alpha, policy, nrPred,"predator");
		MinimaxQLearning pMMQ = new MinimaxQLearning(gamma, alpha, policy, nrPred,"predator");
		for (int i=0; i< nrPred; i++){
			if (i==0){
				Position pred1 = new Position(0,0);
				if(method.equals("q"))
					predPolicies.put(pred1, pQ);
				else
					predPolicies.put(pred1, pMMQ);
				start.addPred(pred1);
			}else if(i==1){
				Position pred2 = new Position(0,10);
				if(method.equals("q"))
					predPolicies.put(pred2, pQ);
				else
					predPolicies.put(pred2, pMMQ);
				start.addPred(pred2);
			}else if(i==2){
				Position pred3 = new Position(10,0);
				if(method.equals("q"))
					predPolicies.put(pred3, pQ);
				else
					predPolicies.put(pred3, pMMQ);
				start.addPred(pred3);
			}else if (i==3){
				Position pred4 = new Position(10,10);
				if(method.equals("q"))
					predPolicies.put(pred4, pQ);
				else
					predPolicies.put(pred4, pMMQ);
				start.addPred(pred4);
			}
		}
		return start;
	}
	
	static void reset(int nrPred) {
		prey.setPos(5, 5);
		int i = 0;
		for (Position key : predPolicies.keySet()) {
			if (i==0){
				key.setPos(0,0);
			}else if(i==1){
				key.setPos(0,10);
			}else if(i==2){
				key.setPos(10,0);
			}else if (i==3){
				key.setPos(10,10);
			}i++;
		}
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
    File dir = new File ("testoutput");
	File policyfile = new File(dir, arg+'_'+parameter+'_'+a+'_'+g+".data");
	policyfile.delete();
	policyfile.createNewFile();
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