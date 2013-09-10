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
    
    private double stateValue;
    private Position prey;
    private String preyaction;

    public State(Position p) {
	prey = p;
    }

    public State(Position p, String preya) {
	prey = p;
	preyaction = preya;
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
    
    public void updatePosition(Position prey){
    	this.prey = prey;
    }
    
    public boolean endState() {
	Position onprey = new Position(0,0);
	if(prey.equals(onprey)) {
	    //show("captured!!");
	    return true;
	}
	return false;
    }
    
    public String getPreyaction() {
	return preyaction;
    }
    
    public Vector nextStates(String mymove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west", "none"};
	Position preynext1, preynext2;
	// mymove
	preynext1 = prey.predmove(mymove);
	//show(preynext1.toString());
	// preymoves
	for(int i=0;i<moves.length;i++) {
	    preynext2 = preynext1.preymove(moves[i]);
	    //show(preynext2.toString());
	    State nextstate = new State(preynext2, moves[i]);
	    // a prey will never move to a occupied position
	    if(nextstate.endState()&&i!=4)
		continue;
	    else
		succstates.add(nextstate);
	}
	return succstates;
    }
    
    public String toString() {
	return prey.toString();
    }
    
    public void show(String s) {
	System.out.println(s);
    }

    public Position getPrey() {
        return prey;
    }

    public void setPrey(Position prey) {
        this.prey = prey;
    }
}
