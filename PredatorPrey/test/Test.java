package test;

public class Test {

    public Test() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	Test test = new Test();
	test.test();
    }
    
    public void test() {
	for(int x = -2; x < 3; x++) {
	    for(int y = -2; y < 3; y++) {
		double correction = (Math.atan2(y, x)+Math.PI)/(2*Math.PI);
		double sum = absDistance(x, y) + correction;
		System.out.println("x: " + x + " y: "+y+" "+sum);
	    }
	}
    }
    
    public int absDistance(int x, int y) {
	return Math.abs(x) + Math.abs(y);
    }
}
