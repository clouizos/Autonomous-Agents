package statespace;

public class Position {
    private int x, y;

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
    public int absDistance(Position p) {
	return Math.abs(wrap(x - p.getX())) + Math.abs(wrap(y - p.getY()));
    }
    */

    public int wrap(int i) {
	if ((i > 10))
	    return i -= 11;
	if ((i < 0))
	    return i += 11;
	return i;
    }

    public String toString() {
	return ("x: " + x + " y: " + y);
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
    
    public Position move(String move) {
	if(move.compareTo("north")==0)
	    return new Position(x, y-1);
	if(move.compareTo("south")==0)
	    return new Position(x, y+1);
	if(move.compareTo("west")==0)
	    return new Position(x-1, y);
	if(move.compareTo("east")==0)
	    return new Position(x+1, y);
	return new Position(x, y);
    }
    /*
    public Position preymove(String move) {
	if(move.compareTo("north")==0)
	    return new Position(x, y-1);
	if(move.compareTo("south")==0)
	    return new Position(x, y+1);
	if(move.compareTo("west")==0)
		return new Position(x-1, y);
	if(move.compareTo("east")==0)
	    return new Position(x+1, y);
	return new Position(x, y);
    }
    */
    
    public boolean equals(Position p) {
	if(x==p.getX()&&y==p.getY())
	    return true;
	return false;
    }
    
    // test main
    /*
    public static void main(String[] args) {
	Position p1, p2;
	p1 = new Position(1,0);
	p2 = p1.preymove("north");
	System.out.println(p2.toString());
    }*/
}
