package simulation;

import io.MyInput;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import policy.*;
import statespace.Position;
import statespace.State;

public class testsimulation {
	// init 
	static int runs = 0;
	 // absolute coordinates
	 static int[] predator = {0,0};
	 static int[] prey = {5,5};
	 // relative prey from pred
	
	
	 
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
	 Position prey1 = new Position(prey[0],prey[1]);
	 State currentState = new State(prey1);
	 //State cs1 = new State(prey1);
	while(q!='q' && runs < 100) {
		/*System.out.println("Please enter x coordinate: ");
	    int x = input.readInt();
	    System.out.println("Please enter y coordinate: ");
	    int y = input.readInt();*/
		//preymove
		//updates the state upon the prey move
		String move = randomPolicyPrey.getAction(currentState);
		Position newPrey = prey1.preymove(move);
		currentState.updatePosition(newPrey);
		prey = getAbsPrey(newPrey);
		System.out.println("prey moved:"+move);
		System.out.println("prey X:"+prey[0]+" prey Y:"+prey[1]);
		prey1 = new Position(prey[0],prey[1]);
		
		
		//predator move on new state(prey)
		//updates the state according to predator move
		move = randomPolicyPredator.getAction(currentState);
		System.out.println("prey1:"+prey1);
		Position newPred = prey1.predmove(move);
		currentState.updatePosition(newPred);
		predmoveAbs(move);
		//prey1 = new Position(prey[0],prey[1]);
		System.out.println("predator moved:"+move);
	    //Position prey = new Position(x,y);
	    
	    //int preyX = prey[0];
	    //int preyY = prey[1];
	    
	    System.out.println("predator X:"+predator[0]+" predator Y:"+predator[1]);
	    //System.out.println(p.getAction(currentState));
	    System.out.println("Enter q to quit, c to continue:");
	    q = input.readChar();
	    runs++;

	}
    }
    
    public static int[] getAbsPrey(Position prey) {
    	
    	int absPreyX = wrapAbs(prey.getX() + predator[0]);
    	int absPreyY = wrapAbs(prey.getY() + predator[1]);
    	int[] absPrey = {absPreyX,absPreyY}; 
    	return absPrey;
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
}
