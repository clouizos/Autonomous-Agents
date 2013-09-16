package statespace;

import java.io.File;
import java.util.Vector;

public class State {
    
    // State is defined on position prey and predator
    private Position predator;
    private Position prey;
    private double stateValue;
    
    //private String action;
    private String preyAction;
    
    public State(Position pred, Position prey) {
    predator = pred;
    this.prey = prey;
	}

    public State(Position pred, Position prey, String a) {
    predator = pred;
    this.prey = prey;
    preyAction = a;
	}
    
    public State(Position pred, Position prey, int v) {
	predator = pred;
    this.prey = prey;
    stateValue = v;
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
	
	/*SAID ADD HERE, PLEASE CORRECT ME IF I'M WRONG ===================================
        if After predator move, the state is the terminal state, 
        then there shouldn't be any next state right? so we don't consider prey can move anymore
        and just return the terminal state as next state
        For the transition probability, it will become 1.
        This change may affect VIPolicy..
        
        No, even if one of the possible next state would be the captured state, 
        it doesn't exclude the possibility of the prey not standing still. So, you have to consider
        that as possible next states. Remember the prey is modeled into the enviroment, 
        so you have to express that uncertainty into the possible next states. 
        See it like a simultaneous move of predator and prey, if you will, to make it clearer; the next state 
        considers the predator making a move (probabilty 1), but the prey making a move too with probabilities 
        as documented; you can refer to the latest mail of the TA about this.
        
        Besides it will create a nullpointerException :p 
	 
	State testState = new State(preDnext, prey);
	if (testState.endState()){
		succstates.add(testState);
		return succstates;
	}*/
	
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

	public String getPreyaction() {
		return preyAction;
	}

	public double getValue() {
		return stateValue;
	}

	public void setValue(double v) {
		stateValue = v;		
	}
}
