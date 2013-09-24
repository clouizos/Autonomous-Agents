package statespace;
/*
 *  Position encodes an agent; either prey or predator
 */
public class Position {
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
     */
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
    }
    
    /* transforms its position(predator) of given actual prey position to
     * the projected position for prey[5][5]
     */
    public Position transformPrey55(Position prey) {
    	int projx = 5-prey.getX()+this.getX();
    	int projy = 5-prey.getY()+this.getY();
    	return new Position(projx, projy);
    }
    
    public boolean equals(Position p) {
	if(x==p.getX()&&y==p.getY())
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
}
