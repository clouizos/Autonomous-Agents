package simulation;

import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

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
    double gamma = 0.5;
    double alpha = 0.1;
    // egreedy with epsilon = 0.1
    EGreedyPolicyTD egreedy = new EGreedyPolicyTD(0.1);
    // qlearning with policy
    QLearning predPolicy = new QLearning(gamma, alpha, egreedy);
    Policy preyPolicy = new RandomPolicyPrey();
	
	/* fill look up table if Value iteration Policy is run
	File file = new File("policy.data");
	try {
		predPolicy.filltable(file);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	 
    test(predPolicy, preyPolicy);
    }
    
    public static void test(Policy predPolicy, Policy preyPolicy) {
    	State currentState = initS();
    	State oldState;
    	while(timesRun < 10000) {
    		if(resetGrid){
    			runs = 0;
    			System.out.println("Resetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			currentState = initS();
    			resetGrid = false;
    		}

    		//show(currentState.getPrey().toString()+" start rel coordinates of prey at begin loopbody");
    		show("\n===========\nAt beginloop: Prey "+ prey.toString());
    		show("At beginloop: Predator "+ predator.toString()+'\n');
    		
    		//predator move on new state(prey)
    		//updates the state according to predator move
    		String move = predPolicy.getAction(currentState);
    		predator = predator.move(move);
    		show("\npredator moved: "+move);
    		oldState = new State(currentState, move);
    		currentState.setPredator(predator);
    		show("Predator: " + predator.toString());
    		
    		//prey move
    		//updates the state upon the prey move
    		move = preyPolicy.getAction(currentState);
    		prey = prey.move(move);
    		show("prey move: "+move);
    		// update state after prey moves
    		currentState.setPrey(prey);
    		show("Prey: " + prey.toString());
    		((QLearning)predPolicy).updateQ(oldState, currentState);
    		
    		
    		if(currentState.endState()){
    			show("\nPredator catched the prey in "+runs+" runs!");
    			timesRun++;
    			allRuns.add(runs);
    			resetGrid = true;
    			//break;
    		}else{
    			runs++;
    		}
    		//pauseProg();
    	}
	
	System.out.println("\nAll runs overview:");
	System.out.println(allRuns);
	
	/*for (int i=0;i<allRuns.size();i++){
		averageRuns += allRuns.get(i);
	}
	averageRuns = averageRuns/allRuns.size();
	System.out.println("Average time for the predator to catch the prey is: "+ averageRuns+" !");
	*/
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation is: "+stdDev);
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
}
