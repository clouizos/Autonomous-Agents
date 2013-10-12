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
public class TestsimulationTD {
	// init 
	static int runs = 0;
	static ArrayList<Integer> allRuns = new ArrayList<Integer>();
	static ArrayList<Integer> allRunsconf = new ArrayList<Integer>();
	static double averageRuns = 0;
	static int timesRun = 0;
	static double delta;
	static ArrayList<Double> optimalities = new ArrayList<Double>();
	static ArrayList<QLearning> predpolicies = new ArrayList<QLearning>();

	//static Position predator;
	//static Position prey;
	//static ArrayList<Position> agents = new ArrayList<Position>();
	static boolean resetGrid = false;
	static boolean discount = true;
	static double parameter;
	//static String arg = "softmax";
    static String arg = "egreedy";
	 
    public TestsimulationTD() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    // State which policies the simulator is run
       
    // egreedy with epsilon
    double epsilon = 0.1;
    double alpha = 0.2;
    double gamma = 0.1;
    double tau = 0.0001;
    int nrPred = 3;
    parameter = epsilon;
    EGreedyPolicyTD policy = new EGreedyPolicyTD(epsilon);
    // SoftMax with temperature tau
    //SoftMax policy = new SoftMax(tau);
    // qlearning with input:policy
    /*for (int i=0;i<2;i++){
    	QLearning predpolicy = new QLearning(gamma, alpha, policy, nrPred);
    	predpolicies.add(predpolicy);
    }*/
    QLearning predpolicy = new QLearning(gamma, alpha, policy, nrPred);
    //QLearning predPolicy1 = new QLearning(gamma, alpha, policy, nrPred);
    //QLearning predPolicy2 = new QLearning(gamma, alpha, policy, nrPred);
    //Sarsa predPolicy = new Sarsa(gamma, alpha, policy);
    
    //todo: implement separate qlearning for the prey
    QLearning preyPolicy = new QLearning(gamma, alpha, policy, nrPred);
    
    //Policy preyPolicy = new RandomPolicyPrey();
	
	/* fill look up table if Value iteration Policy is run
	File file = new File("policy.data");
	try {
		predPolicy.filltable(file);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
    //boolean verbose=true;
    boolean verbose=false;
    //int nrRuns = 20000;
    int nrRuns = 20000;
    testQ(predpolicy, preyPolicy, verbose, nrRuns, nrPred);
    //testSarsa(predPolicy, preyPolicy, verbose, nrRuns);
    //predPolicy.printTable(new Position(5,5));
    //predPolicy.printActionsTable(new Position(5,5));
	try {
	    output(parameter, alpha, gamma, arg);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    /*// TODO: Change to Sarsa
    public static void testSarsa(Sarsa predPolicy, Policy preyPolicy, boolean verbose, int nrRuns) {
    	State currentState = initS();
    	State oldState;
    	String predmove = predPolicy.getAction(currentState);
    	currentState.setAction(predmove);
    	TestPolicy optimal = new TestPolicy();
    	String preymove;
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			if(discount) predPolicy.setSelectPolicy(discounted(nrRuns));
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
    			HashMap test = ((QLearning)predPolicy).getStateactions();
    			delta = optimal.optimality(test);
    			show("\nRuns: "+timesRun+ " optimality: "+delta);
    			optimalities.add(delta);
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
	HashMap test = ((QLearning)predPolicy).getStateactions();
	double delta = optimal.optimality(test);
	show("\nRuns: "+timesRun+ " optimality: "+delta);
    }*/
    
    public static void testQ(QLearning predpolicy, QLearning preyPolicy, boolean verbose, int nrRuns, int nrPred) {
    	ArrayList<Position> agents = initS(nrPred, predpolicy);
    	State currentState = new State(agents);
		//TestPolicy optimal = new TestPolicy();
    	ArrayList<Position> predators = new ArrayList<Position>();
		//ArrayList<Position> agents = currentState.getAgents();
		//ArrayList<Position> agents_new = new ArrayList<Position>();
		//show(agents.toString());
    	Position prey = null;//new Position(5,5, "prey");
    	for (Position agent : agents){
    		if (agent.getAgent().equals("prey")){
    			prey = agent;
    		}else
    			predators.add(agent);
    	}
    	while(timesRun < nrRuns) {
    		if(resetGrid){
    			//if(discount&&timesRun%20==0) predpolicy.setSelectPolicy(discounted(nrRuns));
    				//for (QLearning predPolicy : predpolicies)
    					
    			//}
    			runs = 0;
    			//show("\nResetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			//currentState = initS2(predators, prey);
    			initS2(predators, prey);
    			//show("currentState:"+currentState.toString());
    			//agents = currentState.getAgents();
    			//show("agents size:"+agents.size());
    			//predators = new ArrayList<Position>();
    			//predators.clear();
    			//show("inside reset:"+predators.toString());
    	    	//prey = null;
    	    	/*for (Position agent : agents){
    	    		if (agent.getAgent().equals("prey"))
    	    			prey = agent;
    	    		else
    	    			predators.add(agent);
    	    	}*/
    	    	//show("after reset:"+predators.toString());
    			resetGrid = false;
    			//pauseProg();
    		}

    		//show(currentState.getPrey().toString()+" start rel coordinates of prey at begin loopbody");
    		if(verbose) {
    		show("\n===========\nAt beginloop: Predators "+ predators.toString());
    		show("At beginloop: Prey "+ prey.toString()+'\n');
    		}
    		
    		//predator move on new state(prey)
    		//updates the state according to predator move
    		//agents.clear();
    		ArrayList<String> moves = new ArrayList<String>();
    		//show("pred before move:"+predators.toString());
    		State oldState = new State(currentState.getAgents());
    		//show("oldState:"+oldState.toString());
    		for(int i=0;i<predators.size();i++){
    			//System.out.println(currentState.toString());
    			//String move = predpolicies.get(i).getAction(currentState);
    			String move = predpolicy.getAction(currentState,predators.get(i).getQtable(),"predator");
    			predators.get(i).move(move);// = predator.move(move);
    			moves.add(move);
    			//if(agents_new.size() == nrPred+1){
    			//	agents_new.set(i,predators.get(i));
    			//}else
    			//	agents_new.add(predators.get(i));
    			//agents.set(predator);
    		}
    		if(verbose) {
        		show("\npredators moved: "+moves.toString());
        		show("predators: " + predators.toString());
        	}
    		//show("pred after move:"+predators.toString());
    		String preymove = preyPolicy.getAction(currentState, prey.getQtable(),"prey");
    		//show("prey move: "+preymove);
    		prey.move(preymove);
    		if(verbose) {
        		show("prey move: "+preymove);
        		show("Prey: " + prey.toString());
        	}
    		for(Position predator: predators){
    			predator.transformPrey55(prey);
    		}
    		prey.setX(5);
    		prey.setY(5);
    		/*
    		if(agents_new.size() == nrPred ){
    			for(int i=0;i<agents_new.size();i++){
    				//agents_new.set(i, agents_new.get(i).transformPrey55(prey));
    				agents_new.get(i).transformPrey55(prey);
    			}
    			prey.setX(5);// = new Position(5,5,"prey");
    			prey.setY(5);
    			prey.setAgent("prey");
    			agents_new.add(prey);
    		}
    		else{
    			for (int i=0;i<agents_new.size()-1;i++){
    				//agents_new.set(i, agents_new.get(i).transformPrey55(prey));
    				agents_new.get(i).transformPrey55(prey);
    			}
    			//prey = new Position(5,5,"prey");
    			prey
    			agents_new.set(agents_new.size()-1,prey);
    		}*/
    		//moves.add(move);
    		//String move = predPolicy.getAction(currentState);
    		//predator = predator.move(move);
    		//oldState = new State(currentState, move);
    		
    		//currentState.setPredator(predator);
    		currentState.setAgents(predators,prey);
    		//show("currentState:"+currentState.toString()+" oldState:"+oldState.toString());
    		/*if(verbose) {
    		show("\npredators moved: "+move);
    		show("Predator: " + predator.toString());
    		}*/
    		if(verbose) {
        		show("\npredators moved: "+moves.toString());
        		show("predators: " + predators.toString());
        	}
    		if(verbose) {
        		show("prey move: "+preymove);
        		show("Prey: " + prey.toString());
        	}
    		//prey move
    		//updates the state upon the prey move
    		/*
    		move = preyPolicy.getAction(currentState);
    		prey = prey.move(move);
    		// update state after prey moves
    		currentState.setPrey(prey);
    		if(verbose) {
    		show("prey move: "+move);
    		show("Prey: " + prey.toString());
    		}*/
    		//for(int j=0; j<predpolicies.size(); j++){
    			//((QLearning)predpolicies.get(j)).updateQ(oldState, moves.get(j), currentState);
    		//}
    		for(int j=0;j<predators.size();j++){
    			predpolicy.updateQ(oldState, moves.get(j), currentState, predators.get(j));
    		}
    		preyPolicy.updateQ(oldState, preymove, currentState, prey);
    		
    		
    		if(currentState.endState() == 1 || currentState.endState() == 2){
    			//show("\nPredator catched the prey in "+runs+" runs!");
    			/*for(int i=0; i< predators.size(); i++){
    				double oldRew = predators.get(i).getQtable().get(oldState.toString()+" "+moves.get(i));
    				double newRew = predators.get(i).getQtable().get(currentState.toString()+" "+moves.get(i));
    				
    				show("oldRew:"+oldRew);
    				show("newRew:"+newRew);
    			}*/
    			if (currentState.endState() == 1){
    				show("predators confused after "+runs+" runs!");
    				allRunsconf.add(runs);
    				show(""+timesRun);
    			}
    			else{
    				//show("final state:"+currentState.toString());
    				show("prey captured after "+runs+" runs!");
    				allRuns.add(runs);
    				show(""+timesRun);
    			}
    			timesRun++;
    			//allRuns.add(runs);
    			resetGrid = true;
    			//HashMap test = ((QLearning)predPolicy).getStateactions();
    			//delta = optimal.optimality(test);
    			//show("\nRuns: "+timesRun+ " optimality: "+delta);
    			//optimalities.add(delta);
    		}else{
    			runs++;
    			//show("currentState:"+currentState.toString());
    			//show("type something!");
    			//Scanner scan = new Scanner(System.in);
    			//if(scan.hasNext()){
    				if(verbose)
    				show("new iteration");
    			//}
    			
    			/*show("before: "+predators.toString());
    			show("bef agent: "+agents_new.toString());
    			for (int i=0;i<agents_new.size()-1;i++){
    				predators.set(i,agents_new.get(i));
    			}*/
    			//show("after: "+predators.toString());
    			
    			//moves.clear();
    			//predators.clear();
    			//agents_new.clear();
    		}
    		//pauseProg();
    	}
    
	System.out.println("confused:"+allRunsconf.size());
	System.out.println("catched:"+allRuns.size());
    //((QLearning)predPolicy).printTable(new Position(5,5));
    //((QLearning)predPolicy).printList(new Position(5,5));
	//System.out.println("\nAll runs overview:");
	//System.out.println(allRuns);
	
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation for catching is: "+stdDev);
	
	double stdDev2 = getStdDev(allRunsconf);
	System.out.println("The standard deviation for confused is: "+stdDev2);
	//HashMap test = ((QLearning)predPolicy).getStateactions();
	//double delta = optimal.optimality(test);
	//show("\nRuns: "+timesRun+ " optimality: "+delta);
    }
    
	/*
	 * initializes S; choose a start state from the qtable
	 * 
	 */
    static ArrayList<Position> initS(int numPred, QLearning qobj) {
		//Random gen = new Random();
    	ArrayList<Position> agents = new ArrayList<Position>();
		for (int i=0; i< numPred; i++){
			if (i==0){
				agents.add(new Position(0,0, "predator",qobj.initQ(15.00, numPred)));	
			}else if(i==1){
				agents.add(new Position(10,0, "predator",qobj.initQ(15.00, numPred)));
			}else if(i==2){
				agents.add(new Position(0,10, "predator",qobj.initQ(15.00, numPred)));
			}else if (i==3){
				agents.add(new Position(10,10, "predator", qobj.initQ(15.00, numPred)));
			}
		}
		//predator = new Position(gen.nextInt(11), gen.nextInt(11));
		//prey = new Position(gen.nextInt(11), gen.nextInt(11));
		agents.add(new Position(5,5, "prey", qobj.initQ(15.00, numPred)));
		//State start = new State(agents);
		//return start;
		return agents;
	}
    static void initS2(ArrayList<Position> predators, Position prey) {
		//Random gen = new Random();
    	//ArrayList<Position> agents = new ArrayList<Position>();
		for (int i=0; i<predators.size(); i++){
			if (i==0){
				//agents.add(new Position(0,0, "predator",qobj.initQ(15.00, numPred)));
				predators.get(i).setX(0);
				predators.get(i).setY(0);
				//agents.add(predators.get(i));
			}else if(i==1){
				//agents.add(new Position(10,0, "predator",qobj.initQ(15.00, numPred)));
				predators.get(i).setX(10);
				predators.get(i).setY(0);
				//agents.add(predators.get(i));
			}else if(i==2){
				//agents.add(new Position(0,10, "predator",qobj.initQ(15.00, numPred)));
				predators.get(i).setX(0);
				predators.get(i).setY(10);
				//agents.add(predators.get(i));
			}else if (i==3){
				//agents.add(new Position(10,10, "predator", qobj.initQ(15.00, numPred)));
				predators.get(i).setX(10);
				predators.get(i).setY(10);
				//agents.add(predators.get(i));
			}
		}
		//agents.add(prey);
		//predator = new Position(gen.nextInt(11), gen.nextInt(11));
		//prey = new Position(gen.nextInt(11), gen.nextInt(11));
		//agents.add(new Position(5,5, "prey", qobj.initQ(15.00, numPred)));
		//State start = new State(agents);
		//return start;
	}
   
    /*
	static State initS(int nrPred) {
		Random gen = new Random();
		predator = new Position(gen.nextInt(11), gen.nextInt(11));
		prey = new Position(gen.nextInt(11), gen.nextInt(11));
		State start = new State(predator, prey);
		return start;
	}
	*/
	public static PolicySelect discounted(double nrRuns) {
		double stepsize = parameter/Math.pow(timesRun,2);
		show("stepsize: "+stepsize);
		if(arg.equals("softmax"))
			return new SoftMax(stepsize);
		else
			return new EGreedyPolicyTD(stepsize);
		
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
		for(double delta : optimalities) {
			try {
				write(optfile, String.valueOf(delta)+"\n", true);
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