package statespace;

import java.io.File;
import java.util.*;
/*
 * A state is defined by 2 Positions, that of prey and predator
 */
public class MinimaxState extends State{
    

	// agents defines the state with order defined in comparator Position
    private String action_opponent;

    public String getAction_opponent() {
		return action_opponent;
	}

	public void setAction_opponent(String action_opponent) {
		this.action_opponent = action_opponent;
	}

	public MinimaxState(Position p, String action, String action_opponent) {
		super(p, action);
		this.action_opponent = action_opponent;
	}
    
    @Override
	public int hashCode() {
		return (toString()+super.getAction()+action_opponent).hashCode();
	}
	
	@Override
    public boolean equals(Object s2) {
	MinimaxState s = (MinimaxState) s2;
	if(s.super.toString().equals(s2.super.toString())&&s.super.getAction().equals(super.getAction())&&s.getAction_opponent().equals(getAction_opponent()))
	    return true;
	return false;
    }
    
}
