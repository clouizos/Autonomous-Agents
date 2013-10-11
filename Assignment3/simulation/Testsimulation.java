package simulation;

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
public class Testsimulation {
	// init 
	static int runs = 0;
	static ArrayList<Integer> allRuns = new ArrayList<Integer>();
	static double averageRuns = 0;
	static int timesRun = 0;

	static Position predator = new Position(0,0);
	static Position prey = new Position(5,5);
	static boolean resetGrid = false;
	
	 
    public Testsimulation() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    // State which policies the simulator is run
    //Policy predPolicy = new RandomPolicyPredator();
    //PolicyIter predPolicy = new PolicyIter();
    //VIPolicy predPolicy = new VIPolicy();
    //PolicyIterReduced predPolicy = new PolicyIterReduced();
    VIPolicyReduced predPolicy = new VIPolicyReduced();
    Policy preyPolicy = new RandomPolicyPrey();
	
	// fill look up table if Value iteration Policy is run
	File file = new File("policy.data");
	try {
		predPolicy.filltable(file);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
    test(predPolicy, preyPolicy);
    }
    
    public static void test(Policy predPolicy, Policy preyPolicy) {
    	State currentState = new State(prey, predator);
    	while(timesRun < 100) {
    		if(resetGrid){
    			runs = 0;
    			System.out.println("Resetting Grid for the "+timesRun+" run!");
    			// reset prey and predator positions
    			prey.setX(5);
    			prey.setY(5);
    			predator.setX(0);
    			predator.setY(0);
    			currentState.updatePosition(predator, prey);
    			resetGrid = false;
    		}

    		//show(currentState.getPrey().toString()+" start rel coordinates of prey at begin loopbody");
    		show("\n===========\nAt beginloop: Prey "+ prey.toString());
    		show("At beginloop: Predator "+ predator.toString()+'\n');

    		//prey move
    		//updates the state upon the prey move
    		String move = preyPolicy.getAction(currentState);
    		prey = prey.move(move);
    		show("prey move: "+move);
    		// update state after prey moves
    		currentState.setPrey(prey);
    		show("Prey: " + prey.toString());

    		//predator move on new state(prey)
    		//updates the state according to predator move
    		move = predPolicy.getAction(currentState);
    		predator = predator.move(move);
    		show("\npredator moved: "+move);
    		currentState.setPredator(predator);
    		show("Predator: " + predator.toString());

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
    
    public static double getAverage(ArrayList<Integer> allRuns){
    	double average = 0.0;
    	for (int i=0;i<allRuns.size();i++){
    		average += allRuns.get(i);
    	}
    	average = average/allRuns.size();
    	System.out.println("Average time for the predator to catch the prey is: "+ average+" !");
    	return average;
    }
    
    public static double getVariance(ArrayList<Integer> allRuns)
    {
        double average = getAverage(allRuns);
        double temp = 0;
        for(int a :allRuns)
            temp += (average-a)*(average-a);
        	return temp/allRuns.size();
    }

    public static double getStdDev(ArrayList<Integer> allRuns)
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
