package simulation;

import io.MyInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import policy.*;
import statespace.Position;
import statespace.State;

public class testsimulation {
	// init 
	static int runs = 0;
	static ArrayList<Integer> allRuns = new ArrayList<Integer>();
	static double averageRuns = 0;
	static int timesRun = 0;

	static Position predator = new Position(0,0);
	static Position prey = new Position(5,5);
	static boolean resetGrid = false;
	
	 
    public testsimulation() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	VIPolicy p = new VIPolicy(1.0, 0.1);
        //p.multisweep();
        test(p);
    }
    
    public static void test(Policy p) {
	 MyInput input = new MyInput();
	 char q = ' ';
	 Policy randomPolicyPrey = new RandomPolicyPrey();
	 Policy randomPolicyPredator = new RandomPolicyPredator();

	 State currentState = new State(prey, predator);
	 //State cs1 = new State(prey1);
	while(q!='q' && timesRun < 100) {
		if(resetGrid){
			runs = 0;
			System.out.println("Resetting Grid for the "+timesRun+" run!");
			//System.out.println("prey: "+prey[0]+" "+prey[1]);
			prey.setX(5);
			prey.setY(5);
			predator.setX(0);
			predator.setY(0);
			currentState.updatePosition(prey, predator);
			resetGrid = false;
			//Position test = currentState.getPrey();
			//System.out.println(test.getX()+" "+test.getY());
			//break;
		}
		/*System.out.println("Please enter x coordinate: ");
	    int x = input.readInt();
	    System.out.println("Please enter y coordinate: ");
	    int y = input.readInt();*/
		
		//show(currentState.getPrey().toString()+" start rel coordinates of prey at begin loopbody");
		show("At beginloop: Prey "+ prey.toString());
		show("At beginloop: Predator "+ predator.toString()+'\n');
		
		//preymove
		//updates the state (prey relative from pred) upon the prey move
		String move = randomPolicyPrey.getAction(currentState);
		prey = prey.move(move);
		
		show("prey move: "+move);
		//show(prey.toString()+" after prey moves");
		
		// update state after prey moves
		currentState.setPrey(prey);
		
		//show(currentState.getPrey().toString()+" updated state");
		
		show("Prey: " + prey.toString());
				
		//predator move on new state(prey)
		
		//updates the state according to predator move
		move = randomPolicyPredator.getAction(currentState);
		predator = predator.move(move);
		
		show("predator moved: "+move);
		//show(preyRel.toString()+" PreyRel after pred moves");
		
		currentState.setPredator(predator);
		show("Predator: " + predator.toString());
		
	    if(currentState.endState()){
	    	show("Predator catched the prey in "+runs+" runs!");
	    	timesRun++;
	    	allRuns.add(runs);
	    	resetGrid = true;
	    	//break;
	    }else{
	    	runs++;
	    }
	    //show("Enter q to quit, c to continue:");
	    //q = input.readChar();
	    

	}
	
	System.out.println("All runs overview:");
	System.out.println(allRuns);
	
	/*for (int i=0;i<allRuns.size();i++){
		averageRuns += allRuns.get(i);
	}
	averageRuns = averageRuns/allRuns.size();
	System.out.println("Average time for the predator to catch the prey is: "+ averageRuns+" !");
	*/
	double stdDev = getStdDev(allRuns);
	System.out.println("The standard deviation is: "+stdDev);
	//if(flag){
	//	show("Predator didn't manage to catch the prey in "+runs+" runs...");
	//}
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
}
