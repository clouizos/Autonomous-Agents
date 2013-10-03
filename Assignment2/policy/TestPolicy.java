package policy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import statespace.Position;
import statespace.State;

public class TestPolicy implements Policy {
	HashMap<String, String> stateactions;
	
	public TestPolicy() {
		stateactions = new HashMap<String, String>();
		File file = new File("policy.data");
		try {
			filltable(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /* Required method to implement; returns an action according to implemented policy
     * Projects the currentState to the one when prey[5][5]
     * 
     */
    public String getAction(State currentState){
    	Position pred = currentState.getPredator();
    	Position prey = currentState.getPrey();
    	Position predproj = pred.transformPrey55(prey);
    	State stateproj = new State(predproj, new Position(5,5));
    	return (String)stateactions.get(stateproj.toString());
    }
    
    // fill the stateactions "look up table" with the values in policy.data
    public void filltable(File file) throws Exception {
    	String s;
    	FileReader readFile = new FileReader(file);
    	BufferedReader readBuf = new BufferedReader(readFile);

    	while((s = readBuf.readLine())!=null) {
    		String[] stateaction = s.split("=>");
    		if(stateaction.length>1) {
    			String state = stateaction[0];
    			String action = stateaction[1];
    			if(stateactions.containsKey(state)) {
    				System.out.println("stateactions is not empty!");
    				return;
    			} else {
    				stateactions.put(state, action);
    			}
    		}
    	}
    }
}