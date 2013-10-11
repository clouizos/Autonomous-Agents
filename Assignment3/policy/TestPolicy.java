package policy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import statespace.Position;
import statespace.State;

public class TestPolicy implements Policy {
	HashMap<String, String> stateactions;
	HashMap<String, String> optimal;
	
	public TestPolicy() {
		optimal = new HashMap<String, String>();
		File file2 = new File("optimal.data");
		try {
			filltable(file2, optimal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stateactions = new HashMap<String, String>();
		File file = new File("policy.data");
		try {
			filltable(file, stateactions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TestPolicy test = new TestPolicy();
		test.printActionsTable();
	}
	
    /* Required method to implement; returns an action according to implemented policy
     * Projects the currentState to the one when prey[5][5]
     * 
     */
    public String getAction(State currentState){
    	State stateproj = currentState.projectState();
    	return (String)stateactions.get(stateproj.toString());
    }
    
    // fill the stateactions "look up table" with the values in policy.data
    public void filltable(File file, HashMap table) throws Exception {
    	String s;
    	FileReader readFile = new FileReader(file);
    	BufferedReader readBuf = new BufferedReader(readFile);

    	while((s = readBuf.readLine())!=null) {
    		String[] stateaction = s.split("=>");
    		if(stateaction.length>1) {
    			String state = stateaction[0];
    			String action = stateaction[1];
    			table.put(state, action);
    		}
    	}
    }
    
    /* Policy comparisons
     * Calculates the ratio stateactions/optimal
     * to give a measure how much a tested policy differs
     * from the optimal policy; the closer to 1.0 the more optimal.
     * Optimal policy is taken from value iteration
     */
    public double optimality(HashMap<String, String> sa) {
    	int delta = 0;
    	State state;
    	String test, opti;
    	Position prey = new Position(5,5);
        for(int y = 0; y < 11; y++) {
        	for(int x = 0; x < 11; x++) {
        		state = new State(new Position(x,y),prey);
        		test = sa.get(state.toString());
        		opti = optimal.get(state.toString());
        		if(test.equals(opti)) delta++;
        	}
        }
    	return delta/121.0;
    }
    
    public void printActionsTable() {
    State state;
    String action;
	Position prey = new Position(5,5);
    for(int y = 0; y < 11; y++) {
    	for(int x = 0; x < 11; x++) {
    		state = new State(new Position(x,y),prey);
    		action = optimal.get(state.toString());
    		System.out.println(state+" "+action);
    	}
    }
	}
    
    public static void show(String s) {
        System.out.print(s);
    }
}