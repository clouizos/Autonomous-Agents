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
	 // absolute coordinates
	static int[] predator = {0,0};
	static int[] prey = {5,5};
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
	 // relative prey from pred
	 Position preyRel = new Position(prey[0],prey[1]);
	 State currentState = new State(preyRel);
	 //State cs1 = new State(prey1);
	while(q!='q' && timesRun < 100) {
		if(resetGrid){
			runs = 0;
			System.out.println("Resetting Grid for the "+timesRun+" run!");
			//System.out.println("prey: "+prey[0]+" "+prey[1]);
			prey[0] = 5;
			prey[1] = 5;
			preyRel.setX(prey[0]);
			preyRel.setY(prey[1]);
			currentState.updatePosition(preyRel);
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
		show("At beginloop abs: Prey("+prey[0]+", "+prey[1]+")");
		show("At beginloop abs: Predator("+predator[0]+", "+predator[1]+")"+'\n');
		
		//preymove
		//updates the state (prey relative from pred) upon the prey move
		String move = randomPolicyPrey.getAction(currentState);
		preyRel = preyRel.preymove(move);
		
		show("prey move: "+move);
		//show(preyRel.toString()+" PreyRel after prey moves");
		
		currentState.updatePosition(preyRel);
		
		//show(currentState.getPrey().toString()+" updated state|newPreyRel");
		
		//updates the absolute prey coordinates within the simulator
		prey = getAbsPrey(preyRel);
		
		show("abs: Prey:("+prey[0]+", "+prey[1]+")");
		
		
		//predator move on new state(prey)
		//updates the state (prey relative from pred) according to predator move
		move = randomPolicyPredator.getAction(currentState);
		preyRel = preyRel.predmove(move);
		
		show("predator moved: "+move);
		//show(preyRel.toString()+" PreyRel after pred moves");
		
		currentState.updatePosition(preyRel);
		
		//show(currentState.getPrey().toString()+" updated state|newPreyRel");
		
		//updates the absolute predator coordinates within the simulator
		predmoveAbs(move);
		  
	    show("abs: Predator("+predator[0]+", "+predator[1]+")");
	    if(predator[0] == prey[0] && predator[1] == prey[1]){
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
	
	System.out.println("All runs overiview:");
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
    
    //returns the absolute coordinates from prey within the simulator
    public static int[] getAbsPrey(Position prey) {
    	
    	int absPreyX = wrapAbs(prey.getX() + predator[0]);
    	int absPreyY = wrapAbs(prey.getY() + predator[1]);
    	int[] absPrey = {absPreyX,absPreyY}; 
    	return absPrey;
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

    //absolute move updates
    public static void predmoveAbs(String move) {
    	if(move.compareTo("north")==0)
    	    //return new Position(x, y-1);
    		predator[1] -=1;
    		predator[1] = wrapAbs(predator[1]);
    	if(move.compareTo("south")==0)
    	   // return new Position(x, y+1);
    		predator[1] +=1;
    		predator[1] = wrapAbs(predator[1]);
    	if(move.compareTo("west")==0)
    		//return new Position(x+1, y);
    		predator[0] -=1;
    		predator[0] = wrapAbs(predator[0]);
    	if(move.compareTo("east")==0)
    	    //return new Position(x-1, y);
    		predator[0] +=1;
    		predator[0] = wrapAbs(predator[0]);
    	//return new Position(x, y);
        }
     
    public static int wrapAbs(int i) {
    	if ((i > 10))
    	    return i -= 11;
    	if ((i < 0))
    	    return i += 11;
    	return i;
        }
    
    public static void show(String s) {
        System.out.println(s);
    }
}
