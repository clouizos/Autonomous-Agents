package statespace;

import java.util.Vector;

/**
 * <p>Title: opdr 3</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 *
 * @author duy chuan
 * @version 1.0.
 */
public class State {
    
    // State is defined on position prey and predator
    private Position predator;
    private Position prey;
    private double stateValue;
    
    private String preyaction;

    public State(Position pred, Position prey) {
    this.predator = pred;
    this.prey = prey;
	}
    
    public State(Position pred, Position prey, String preya) {
    predator = pred;
    this.prey = prey;
	preyaction = preya;
    }

    public State(Position p, String preya) {
	prey = p;
	preyaction = preya;
    }
    
    public State(Position pred, Position prey, double v) {
    predator = pred;
    this.prey = prey;
	stateValue = v;
    }
    
    public State(Position p, double v) {
	prey = p;
	this.stateValue = v;
    }
    
    public void setValue(double value) {
        this.stateValue = value;
    }

    public double getValue() {
        return stateValue;
    }
    
    public void updatePosition(Position predator, Position prey){
    	this.predator = predator;
    	this.prey = prey;
    }
    
    public boolean endState() {
	if(prey.equals(predator)) {
	    //show("captured!!");
	    return true;
	}
	return false;
    }
    
    public String getPreyaction() {
	return preyaction;
    }
    
    /* predmove -> preymove
     * Next states is defined on the next position of the predator
     * and the possible positions the prey would get on succession.
     * However the prey would never go to a position already occupied,
     * so that has to be taken into account on the possible next states. 
     */
    public Vector nextStates(String predmove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west", "wait"};
	Position preDnext, preYnext;
	// the next position of the predator when taken a:predmove
	preDnext = predator.move(predmove);
	// preymoves
	for(int i=0;i<moves.length;i++) {
	    // the next position of the prey
		preYnext = prey.move(moves[i]);
	    //show(preynext2.toString());
	    State nextstate = new State(preDnext, preYnext, moves[i]);
	    // a prey will never move to an occupied position
	    if(nextstate.endState()&&i!=4)
		continue;
	    else
		succstates.add(nextstate);
	}
	return succstates;
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
	return "Predator: "+predator.toString() + "Prey: "+prey.toString();
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
}
