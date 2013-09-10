import java.util.Arrays;

/**
 * test.java version 1.0 2 Mar 2007
 *
 * Copyright (c) 2007 qrio.
 * All rights reserved
 *
 */

/**
 * @author qrio
 *
 */
public class Test {

    /**
     * 
     */
    public Test() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	int[] test = { 0, 1,-1, 1};
	Test dummy = new Test();
	if(dummy.freeze(test))
	    System.out.println("k");

    }
    
    public boolean freeze(int[] test) {
	int[] a = { 0,-1, 1,-1};
	int[] b = { 0, 1,-1, 1};
	int[] c = {-1, 0,-1,-1};
	int[] d = { 1, 0, 1, 1};
	int[] e = { 0,-1, 0,-2};
	int[] f = { 1, 0, 2, 0};
	
	Object[] proximity = {a,b,c,d,e,f};
	for(int i=0;i<proximity.length;i++) {
	    if(Arrays.equals(test, (int[]) proximity[i]))
		return true;
	}
	/*
	if(Arrays.equals(test, a))
		return true;
	else if(Arrays.equals(test, a))
		return true;
	else if(Arrays.equals(test, b))
		return true;
	else if(Arrays.equals(test, c))
		return true;
	else if(Arrays.equals(test, d))
		return true;
	else if(Arrays.equals(test, e))
		return true;
	else if(Arrays.equals(test, f))
		return true;*/
	return false;	
    }

}
