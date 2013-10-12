package statespace;

import java.io.File;
import java.util.*;
/*
 * A state is defined by 2 Positions, that of prey and predator
 */
public class State {
    
/*    // State is defined on position prey and predator
    private Position predator;
    private Position prey;*/
	//last entry in this arraylist should be the prey
	private ArrayList<Position> agents;
    
    //private String action;
    
    public State() {
    	agents = new ArrayList<Position>();
	}
    
    public State(ArrayList<Position> agents){
    	this.agents = agents; 
    }
    
    public State(ArrayList<Position> predators, Position prey){
    	ArrayList<Position> pred = new ArrayList<Position>();
    	pred = (ArrayList<Position>)predators.clone();
    	pred.add(prey);
    	this.agents = pred;
    }
    //constructor for multiagent TD
    //public State(State s, String a){
    //	agents = s.getAgents();
    //	action = a;
    //}
    
    public State(State s){
    	agents = s.getAgents();
    }
    
    //public State(ArrayList<Position> agents, String a){
    //	this.agents = agents;
    //	action = a;
    //}
    
/*    // constructor with prey move
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
    }*/
    
    public void addAgent(Position p) {
    	agents.add(p);
    }
    
	/*
	 * TODO: needs changes for assign3. Encodes the endstate
	 */
    public int endState(){
    	//if predators are on the same block
    	ArrayList<Position> predators = new ArrayList<Position>();
    	Position prey = null;
    	for (Position agent : agents){
    		if (agent.getAgent().equals("prey")){
    			prey = agent;
    		}else
    			predators.add(agent);
    	}
    	for (int i=0;i<predators.size()-1;i++){
    		for (int j = i+1; j<predators.size();j++){
    			if (predators.get(i).equals(predators.get(j))){
    				return 1;
    			}
    		}
    	}
    	//if catched the prey
    	for (Position predator : predators){
    		if(predator.equals(prey))
    			return 2;
    	}
    	
    	return 0;
    }
    /*public boolean endState() {
	if(prey.equals(predator)) {
	    //show("captured!!");
	    return true;
	}
	return false;
    }*/
    
    /* predmove -> preymove
     * Next states is defined on the next position of the predator
     * and the possible positions the prey would get on succession.
     * However the prey would never go to a position already occupied,
     * so that has to be taken into account on the possible next states. 
     */
    /*public Vector nextStates(String predmove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west","wait"};
	Position preDnext, preYnext;
	// the next position of the predator when taken a:predmove
	preDnext = predator.move(predmove);
	State nextstate = new State(preDnext, prey);
	if(nextstate.endState() == 1 || nextstate.endState() == 2){
		//todo: need to distinguish between first and second end state
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
    }*/
    
    /*for(int i=0; i <predators.size(); i++){
    		if(agents.get(i).equals(agents.get(agents.size()-1))){
    			return 2;
    		}
    	}
     * nextStates are projected to the prey[5][5]
     */
   
    //todo: according to all of the predators
    /*
    public Vector nextStatesReduced(String predmove) {
	Vector succstates = new Vector();
	String[] moves = {"north", "east", "south", "west", "wait"};
	Position preDnext, preYnext;
	// the next position of the predator when taken a:predmove
	preDnext = predator.move(predmove);
	State nextstate = new State(preDnext, prey);
	if(nextstate.endState() == 1 || nextstate.endState() == 2){
		//todo: need to distinguish between first and second end state
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
    */
    // returns projected state for prey at (5,5)
    public State projectState() {
    	ArrayList<Position> predators = new ArrayList<Position>();
    	Position prey = null;
    	for (Position agent : agents){
    		if (agent.getAgent().equals("prey")){
    			prey = agent;
    		}else
    			predators.add(agent);
    	}
    	ArrayList<Position> projected = new ArrayList<Position>();
    	//for(int i=0;i<agents.size();i++){
    	for(Position predator : predators){
    		//predator = predator.transformPrey55(prey);
    		predator.transformPrey55(prey);
    		projected.add(predator);
    	}
    	projected.add(new Position(5,5,"prey"));
    	/*
    		if (i!=agents.size()-1){
    			Position predproj = agents.get(i).transformPrey55(agents.get(agents.size()-1));
    		}else
    			projected.add(new Position(5,5,"prey"));
    	}*/
    	//Position predproj = predator.transformPrey55(prey);
    	//State stateproj = new State(predproj, new Position(5,5), action);
    	State stateproj = new State(projected);
    	return stateproj;
    }
    
    //todo: according to all predators
    /*
    // next state after prey has taken a:preymove
    public State nextStatePrey(String preymove) {
    	return new State(predator, prey.move(preymove));
    }
    
    // next state after predator has taken a:predmove
    public State nextStatePred(String predmove) {
    	return new State(predator.move(predmove), prey);
    }
    */
    /*
    public String toString() {
	return predator.toString() + prey.toString();
    }
    */
    public String toString(){
    	return agents.toString();
    }
    
    public void show(String s) {
	System.out.println(s);
    }
    
    // setter getters for prey and predator
    public ArrayList<Position> getAgents(){
    	return agents;
    }
    
    public void setAgents(ArrayList<Position> agents){
    	this.agents = agents;
    }
    
    public void setAgents(ArrayList<Position> predators, Position prey){
    	ArrayList<Position> pred = new ArrayList<Position>();
    	pred = (ArrayList<Position>)predators.clone();
    	pred.add(prey);
    	this.agents = pred;
    }
    
    /*public Position getPrey() {
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
    */
    
    //public void setAction(String move) {
    //	action = move;
    //}
    
   
    // used in assignment 1 to calculate prey action probability
	//public String getPreyaction() {
	//	return action;
	//}
	
	// used in assignment 2 for Qstate
	//public String getAction() {
	//	return action;
	//}

	@Override
	public int hashCode() {
		return (toString()).hashCode();
	}
	/*
	public int hashCode() {
		return (toString()+action).hashCode();
	}*/
	
	
	@Override
    /*public boolean equals(Object s2) {
	State s = (State) s2;
	//if(s.getPredator().equals(predator)&&s.getPrey().equals(prey)&&s.getPreyaction().equals(action))
	if(s.getAgents().equals(agents)&&s.getAction().equals(action))
	    return true;
	return false;
    }*/
	public boolean equals(Object s2) {
		State s = (State) s2;
		if(s.toString().equals(this.toString()))//&&s.getPrey().equals(prey))
		    return true;
		return false;
	}
}