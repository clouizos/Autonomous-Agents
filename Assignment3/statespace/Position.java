package statespace;

import policy.TestPolicy;

import java.util.*;

/*
 *  Position encodes an agent; either prey or predator
 */
public class Position implements Comparable<Position> {
	// absolute coordinates
    private int x, y;

    /*
     *  Constructors
     */
    public Position(int a, int b) {
	this.x = wrap(a);
	this.y = wrap(b);
    }

    public Position(Position pos) {
	this.x = pos.getX();
	this.y = pos.getY();
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
     *
    public Position move(String move) {
	if(move.equals("north"))
	    return new Position(x, y-1);
	if(move.equals("south"))
	    return new Position(x, y+1);
	if(move.equals("west"))
	    return new Position(x-1, y);
	if(move.equals("east"))
	    return new Position(x+1, y);
	return new Position(x, y);
    }*/
    
    public void move(String move){
	if(move.equals("north")){
		y = wrap(y - 1);
	}
	if(move.equals("south")){
		y = wrap(y + 1);
	}
	if(move.equals("west")){
		x = wrap(x - 1);
	}
	if(move.equals("east")){
		x = wrap(x + 1);
	}
    }
    
    /* transforms its position(predator) of given actual prey position to
     * the projected position for prey[5][5]
     */
    public Position transformPrey55(Position prey) {
    	int projx = 5-prey.getX()+this.getX();
    	int projy = 5-prey.getY()+this.getY();
    	return new Position(projx, projy);
    }
    
    @Override
    public boolean equals(Object p2) {
    Position p = (Position) p2;
    if(x==p.getX()&&y==p.getY())
	    return true;
	return false;
    }
    
    @Override
    public int compareTo(Position p2) {
    	int x2 = p2.getX();
    	int y2 = p2.getY();
    	if(equals(p2))
    		return 0;
    	if(y > y2)
    		return 1;
    	if(y==y2&&x>x2)
    		return 1;
    	return -1;
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
    
	public static void main(String[] args) {
		Position p1 = new Position(7, 4);
		Position p2 = new Position(3,4);
		Position p3 = new Position(10,5);
		ArrayList<Position> agents = new ArrayList<>();
		agents.add(p1);
		agents.add(p2);
		agents.add(p3);
		Collections.sort(agents);
		for(Position agent: agents) {
			System.out.println(agent.toString());
		}
		show("p1 p2: " + p1.compareTo(p2));
		show("p2 p3: " + p2.compareTo(p3));
		show("p3 p1: " + p3.compareTo(p1));
	}
	
    public static void show(String s) {
        System.out.print(s);
    }
}
