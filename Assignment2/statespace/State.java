package statespace;

import java.io.File;
import java.util.Vector;
/*
 * A state is defined by 2 Positions, that of prey and predator
 */
public class State {
    
    // State is defined on position prey and predator
    private Position predator;
    private Position prey;
    
    private String action;
    //private String preyAction;
    
    public State(Position pred, Position prey) {
    predator = pred;
    this.prey = prey;
	}
    
    // constructor with prey move
    public State(Position pred, Position prey, String a) {
    predator = pred;
    this.prey = prey;
    action = a;
	}
    
    // constructor used in TD
    public State(State s, String a) {
    	predator = s.getPredator();
    	prey = s.getPrey();
    	action = a;
    }
    
    public State(State s) {
    	predator = s.getPredator();
    	prey = s.getPrey();   	
    }

	public void updatePosition(Position predator, Position prey){
    	this.predator = predator;
    	this.prey = prey;
    }
    
	/*
	 * Encodes the endstate
	 */
    public boolean endState() {
	if(prey.equals(predator)) {
	    //show("captured!!");
	    return true;
	}
	return false;
    }
    
    /* predmove -> preymove
     * Next states is defined on the next position of the predator
     * and the possible positions the prey would get on succession.
     * However the prey would never go to a position already occupied,
     * so that has to be taken into account on the possible next states. 
     */
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
    }
    
    /*
     * nextStates are projected to the prey[5][5]
     */
    
    public Vector nextStatesReduced(String predmove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west", "wait"};
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
    }
    
    // next state after prey has taken a:preymove
    public State nextStatePrey(String preymove) {
    	return new State(predator, prey.move(preymove));
    }
    
    // next state after predator has taken a:predmove
    public State nextStatePred(String predmove) {
    	return new State(predator.move(predmove), prey);
    }
    
    public String toString() {
	return predator.toString() + prey.toString();
    }
    
    public void show(String s) {
	System.out.println(s);
    }
    
    // setter getters for prey and predator
    public Position getPrey() {
        return prey;
    }
    
    public Position getPredator() {
    	return predator;
    }

    public void setPrey(Position prey) {
        this.prey = prey;
    }
    
    public void setPredator(Position predator) {
        this.predator = predator;
    }
    
    public void setPreyPred(Position predator, Position prey) {
    	this.predator = predator;
    	this.prey = prey;
    }
    
    public void setAction(String move) {
    	action = move;
    }
    
    // used in assignment 1 to calculate prey action probability
	public String getPreyaction() {
		return action;
	}
	
	// used in assignment 2 for Qstate
	public String getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		return (toString()+action).hashCode();
	}
	
	@Override
    public boolean equals(Object s2) {
	State s = (State) s2;
	if(s.getPredator().equals(predator)&&s.getPrey().equals(prey)&&s.getPreyaction().equals(action))
	    return true;
	return false;
    }
}
