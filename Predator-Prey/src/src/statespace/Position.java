package src.statespace;

public class Position {
	
	private int xAbsolute, yAbsolute;
	private int x,y;
	private int[] absolutePos = {xAbsolute,yAbsolute};
	private int[] pos = {x,y};
	
	public Position(int x, int y){
		xAbsolute = x;
		yAbsolute = y;
		this.x = wrapCoord(x);
		this.y = wrapCoord(y);
	}
	
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public int getAbsoluteX(){
		return xAbsolute;
	}
	public int getAbsoluteY(){
		return yAbsolute;
	}
	
	public int[] getAbsolutePos(){
		return absolutePos;
	}
	
	public int[] getWrappedPos(){
		return pos;
	}
	
	public int wrapCoord(int i){
		
		if (i > 5){
			return i -= 11;
		}
		else if ( i < -5){
			return i += 11;
		}
		return i;
	}
	
	public Position movingPredator(String action){
		switch (action){
			case "north":
				return new Position(x, y-1);
			case "south":
				return new Position(x, y+1);
			case "east":
				return new Position(x-1, y);
			case "west":
				return new Position(x+1, y);			
		}
		return new Position(x,y);
	}
}
