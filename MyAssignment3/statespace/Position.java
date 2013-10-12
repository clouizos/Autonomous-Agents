package statespace;

import java.util.HashMap;

/*
 *  Position encodes an agent; either prey or predator
 */
public class Position {
	// absolute coordinates
    private int x, y;
    private String agent;
    private HashMap<String, Double> qtable ;

    public HashMap<String, Double> getQtable() {
		return qtable;
	}


	public void setQtable(HashMap<String, Double> qtable) {
		this.qtable = qtable;
	}


	/*
     *  Constructors
     */
    public Position(int a, int b, String entity) {
	this.x = wrap(a);
	this.y = wrap(b);
	agent = entity;
    }
    
    public Position(int a, int b, String entity, HashMap<String, Double> qtable) {
    	this.x = wrap(a);
    	this.y = wrap(b);
    	agent = entity;
    	this.qtable = qtable;
    }
    

    public Position(Position pos) {
	this.x = pos.getX();
	this.y = pos.getY();
	agent = pos.getAgent();
    }

    public int absDistance() {
	return Math.abs(x) + Math.abs(y);
    }

    /*
     * Wrapper, since grid is toroidal
     */
    public int wrap(int i) {
	if ((i > 10))
	    return i -= 11;
	if ((i < 0))
	    return i += 11;
	return i;
    }

    public String toString() {
	return ("["+ x +"]["+ y+']');
    }

    /*
     * Gives new position according to move
     */
    //public Position move(String move) {
    public void move(String move){
	if(move.equals("north")){
	    //return new Position(x, y-1, agent);
		this.y = wrap(this.y - 1);
	}
	if(move.equals("south")){
	    //return new Position(x, y+1, agent);
		this.y = wrap(this.y+1);
	}
	if(move.equals("west")){
	    //return new Position(x-1, y, agent);
		this.x = wrap(this.x - 1);
	}
	if(move.equals("east")){
	    //return new Position(x+1, y, agent);
		this.x = wrap(this.x + 1);
	}
	//return new Position(x, y, agent);
    }
    
    /* transforms its position(predator) of given actual prey position to
     * the projected position for prey[5][5]
     */
    //public Position transformPrey55(Position prey) {
    public void transformPrey55(Position prey) {
    	int projx = 5-prey.getX()+this.getX();
    	int projy = 5-prey.getY()+this.getY();
    	this.x = wrap(projx);
    	this.y = wrap(projy);
    	//return new Position(projx, projy, agent);
    }
    
    public boolean equals(Position p) {
	if(x==p.getX()&&y==p.getY()&&agent==p.getAgent())
	    return true;
	return false;
    }
    
    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }
    
    public int[] getArray() {
	int[] position = {x, y};
	return position;
    }

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
}