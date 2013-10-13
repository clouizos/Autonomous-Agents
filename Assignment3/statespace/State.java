package statespace;

import java.io.File;
import java.util.*;
/*
 * A state is defined by 2 Positions, that of prey and predator
 */
public class State {
    
	// agents defines the state with order defined in comparator Position
    protected ArrayList<Position> agents;
    
    private String action;
   
    
    public State() {
    	agents = new ArrayList();
	}
    
    public void addAgent(Position p) {
    	agents.add(p);
    }
    
    /* constructor with prey move
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
    }
    
    *
     * nextStates are projected to the prey[5][5]
     *
    
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
    }*/
    
	/*
	 * Encodes the endstate
	 */
    public int endState(){
    	//if predators are on the same block
    	ArrayList<Position> predators = new ArrayList<Position>();
    	Position prey = new Position(5,5);
    	Collections.sort(agents);
    	// remove prey from list
    	for (Position agent : agents){
    		if (!agent.equals(prey))
    		predators.add(agent);	
    	}
    	// check for collision first
    	for(int i=0; i<predators.size()-2;i++) {
    		Position pred1 = predators.get(i);
    		Position pred2 = predators.get(i+1);
    		if(pred1.equals(pred2))
    			return -1;
    	}
    	// check for capture
    	for(Position predator: predators)
    		if(predator.equals(prey))
    			return 1;
    	// no endstate
    	return 0;
    }
    
    public String toString() {
	String state = null;
	Collections.sort(agents);
    for(Position agent: agents) {
    	state.concat(agent.toString());
    }
    	return state;
    }
    
    public void show(String s) {
	System.out.println(s);
    }
    
    
    public void setAction(String move) {
    	action = move;
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
	if(s.toString().equals(s2.toString())&&s.getAction().equals(action))
	    return true;
	return false;
    }
}
