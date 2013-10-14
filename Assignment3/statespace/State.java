package statespace;

import java.io.File;
import java.util.*;
/*
 * A state is defined by 2 Positions, that of prey and predator
 */
public class State {
    
	// agents defines the state with order defined in comparator Position
    protected ArrayList<Position> predators = new ArrayList();
    protected Position prey;
    
    private String action;
   
    
    public State(Position p) {
    	predators = new ArrayList();
    	prey = p;
	}
    
    public void addPred(Position p) {
    	predators.add(p);
    }
    
    public State(ArrayList<Position> preds, Position p) {
    	predators = preds;
    	prey = p;
    }
    
    
    // constructor used in TD
    public State(Position p, String a) {
    	predators = new ArrayList();
    	prey = p;
    	action = a;
    }
    
    /*
    public State(State s) {
    	predator = s.getPredator();
    	prey = s.getPrey();   	
    }

	public void updatePosition(Position predator, Position prey){
    	this.predator = predator;
    	this.prey = prey;
    }
    
    /* predmove -> preymove
     * Next states is defined on the next position of the predator
     * and the possible positions the prey would get on succession.
     * However the prey would never go to a position already occupied,
     * so that has to be taken into account on the possible next states. 
     *
    public Vector nextStates(String predmove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west","wait"};
	Position preDnext, preYnext;
	// the next position of the predator when taken a:predmove
	preDnext = predator.move(predmove);
	State nextstate = new State(preDnext, prey);
	if(nextstate.endState()){
		succstates.add(nextstate);
		return succstates;
	}
		
	// preymoves
	for(int i=0;i<moves.length;i++) {
	    // the next position of the prey
		preYnext = prey.move(moves[i]);
	    //show(preynext2.toString());
	    nextstate = new State(preDnext, preYnext, moves[i]);
	    // a prey will never move to an occupied position
	    if(nextstate.endState())
		continue;
	    else
		succstates.add(nextstate);
	}
	return succstates;
    }s
    
    *
     * nextStates are projected to the prey[5][5]
     *
    
    public Vector nextStatesReduced(String predmove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west", "wait"};
	Position preDnext, preYnext;
	// the next position of the predator when taken a:psredmove
	preDnext = predator.move(predmove);
	State nextstate = new State(preDnext, prey);
	if(nextstate.endState()){
		succstates.add(nextstate);
		return succstates;
	}
	
	// preymoves
	for(int i=0;i<moves.length;i++) {
	    // the next position of the prey
		preYnext = prey.move(moves[i]);
	    //show(preynext2.toString());
	    nextstate = new State(preDnext.transformPrey55(preYnext), new Position(5,5), moves[i]);
	    // a prey will never move to an occupied position
	    if(nextstate.endState())
		continue;
	    else
		succstates.add(nextstate);
	}
	return succstates;
    }
    
    // returns projected state for prey at (5,5)
    public State projectState() {
    	Position predproj = predator.transformPrey55(prey);
    	State stateproj = new State(predproj, new Position(5,5), action);
    	return stateproj;
    }*/
    
    // next state after prey has taken a:preymove
    public State nextStatePrey(String preymove) {
    	return new State(predators, prey.forecast(preymove));
    }
    
	/*
	 * Encodes the endstate
	 */
    public int endState(){
    	// check for collision first
    	for(int i=0; i<predators.size()-2;i++) {
    		Position pred1 = predators.get(i);
    		Position pred2 = predators.get(i+1);
    		if(pred1.equals(pred2))
    			return -1;
    	}
    	for(Position pred : predators)
    		if(pred.equals(prey))
    			return 1;
    	// no endstate
    	return 0;
    }
    
    public String toString() {
	String state = "";
	Collections.sort(predators);
    for(Position pred : predators) {
    	state+=pred.toString();
    }
    state+= prey.toString();
    return state;
    }
    
    public static void show(String s) {
	System.out.println(s);
    }
    
    
    public void setAction(String move) {
    	action = move;
    }    
	
	// used in assignment 2 for Qstate
	public String getAction() {
		return action;
	}

	public ArrayList<Position> getPredators() {
		return predators;
	}

	public void setPredators(ArrayList<Position> predators) {
		this.predators = predators;
	}

	public Position getPrey() {predators = new ArrayList();
		return prey;
	}

	public void setPrey(Position prey) {
		this.prey = prey;
	}

	@Override
	public int hashCode() {
		return (toString()+action).hashCode();
	}
	
	@Override
    public boolean equals(Object s2) {
	State s = (State) s2;
	if(s.toString().equals(s2.toString())&&s.getAction().equals(action))
	    return true;
	return false;
    }
}
