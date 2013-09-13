package policy;

import statespace.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
 * <p>Title: opdr 2 - Mc, Q, Sarsa</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author duy chuan
 * @version 1.0
 */
public class VIPolicy implements Policy {
    private State[][][][] statespace;
    private static Hashtable stateactions, statevalues;
    private double gamma, delta, theta;
    //int size = 0;

    public VIPolicy(double g, double t) {
       // statespace init
	statespace = new State[11][11][11][11];
	for(int i = 0; i < 11; i++) {
	    for(int j = 0; j < 11; j++) {
	    	for(int k = 0; k < 11; k++) {
	    		for(int l = 0; l < 11; l++) {
	    			State s = new State(new Position(i, j), new Position(k, l), 0);
	    			statespace[i][j][k][l] = s;
	    		}
	    	}

	    }
	}
        gamma = g;
        theta = t;
        stateactions = new Hashtable<>();
        statevalues = new Hashtable<>();
	}
    
    public VIPolicy() {}

    public static void main(String[] args) {
        VIPolicy p = new VIPolicy(0.9, 0.001);
        p.multisweep();
        
        // outputs the values of all states where prey[5][5]
        Position prey55 = new Position(5,5);
    	for(int i = 0; i < 11; i++) {
    	    for(int j = 0; j < 11; j++) {
    	    	State statePrey55 = new State(new Position(i,j), prey55);
    	    	p.show(statePrey55.toString() +  " " +(double)statevalues.get(statePrey55.toString()));
    	    }
    	}
        
        //p.show("size statespace tree: " + p.size);
        try {
	    p.output();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public Hashtable getSA() {
        return stateactions;
    }
    
    public Hashtable getSV() {
        return statevalues;
    }

    public String getAction(State currentState){
         //show("hashcode: "+ currentState.hashCode());
         show(currentState.toString());
         //show(""+currentState.equals(currentState));
         //show(""+stateactions.size());
         //if(stateactions.containsKey(currentState.toString()))
         //    show("in!!");
         //return 0;
	return (String)stateactions.get(currentState.toString());
    }

    public void multisweep() {
        int k = 0;
        //int depth = 0;
        do {
            delta = 0;
            stateactions.clear();
            delta = sweep();
            show("delta: " + delta);
            show("theta: " + theta);
            k++;
            //--k;
        } //while (k>0);
        while(delta > theta);
        show("nr of iterations: "+k);
    }

    public double sweep() {
        double v;
        double vUpdate;
        //loop: for every state/node
        for(int i=0;i<11;i++) {
            for(int j=0;j<11;j++) {
            	for(int k = 0; k < 11; k++) {
    	    		for(int l = 0; l < 11; l++) {
    	    			State currentState = statespace[i][j][k][l];
    	    			//show(currentState.toString());
    	    			v = currentState.getValue();
    	    			show("current value: "+v);
    	    			vUpdate = updateValue(currentState);
    	    			show("updated value: "+vUpdate);
    	    			currentState.setValue(vUpdate);
    	    			// put the statevalue for currentState in the look up table
    	    	        statevalues.put(currentState.toString(), vUpdate);
    	    			show("check updated value: "+currentState.getValue());
    	    			delta = Math.max(delta, Math.abs(v - currentState.getValue()));
    	    		}
            	}
            }
        }
        show("booya delta: " + delta);
        return delta;
    }

    public double updateValue(State cS) {
	String action = "wait"; // action
	State currentState = cS;
	State nextState;
        String[] moves = {"north", "south", "east", "west", "wait"};
        double[] actions = {0,0,0,0,0};
        Vector nextStates;
        for(int i=0;i<moves.length;i++) {
            action = moves[i];
            nextStates = currentState.nextStates(action);
            for(int j=0; j<nextStates.size();j++) {
        	nextState = (State) nextStates.elementAt(j);
        	//show(nextState.toString());
        	/*if(nextState.endState()/*||depth==0) {
        	    actions[i] += getP(nextStates.size(), nextState) *
                    getReward(nextState);
        	} else {*/
        	 //   if(stateactions.containsKey(nextState.toString())) {
		        	int predX = nextState.getPredator().getX();
		        	int predY = nextState.getPredator().getY();
        			int preyX = nextState.getPrey().getX();
        			int preyY = nextState.getPrey().getY();
                	actions[i] += getP(nextStates.size(), nextState) *
                        (getReward(nextState) +
                         (gamma * statespace[predX][predY][preyX][preyY].getValue()));
        	    //System.out.println("nextstate value: "+statespace[nextState.getPrey().getX()+7][nextState.getPrey().getY()+7].getValue());
                	/*
        	    actions[i] += getP(nextStates.size(), nextState) *
        	    (getReward(currentState) +
        		    (gamma * updateValue(nextState, depth-1)));*/
              //  }
            }
        }
        double max = actions[0];
        action = moves[0];
        for(int i=1;i<actions.length;i++) {
            if(actions[i]>max) {
        	max = actions[i];
        	action = moves[i];
            }
        }
        //System.out.println(currentState.toString());
        //System.out.println(action);
      //  if(root==recursion) {
            //show(currentState.toString());
            //show(action);
            stateactions.put(currentState.toString(), action);
       // }
        //show("show: "+currentState.toString());
        return max;
    }

    // todo: implement probablity of next state following current state
    public double getP(int nrnextstates, State next) {
	if(next.getPreyaction().compareTo("wait")==0)
	    return (0.2);
	else
	    return (0.8/nrnextstates);
    }

    // implement reward function
    public double getReward(State s) {
        if(s.endState())
            return 10.0;
	return 0.0;
    }

    public void show(String s) {
        System.out.println(s);
    }
    
    public static void write(File file, String string, boolean append) throws Exception
    {
	if(append==false)
	{
	    file.delete();
	    file.createNewFile();
	}

	FileOutputStream WriteFile = new FileOutputStream(file, true);
	OutputStreamWriter WriteBuff = new OutputStreamWriter(WriteFile, "UTF8");
	WriteBuff.write(string);
	WriteBuff.close();
	WriteFile.close();
    }
    
    public void output() throws Exception {
	File policyfile = new File("policy.data");
	policyfile.delete();
	policyfile.createNewFile();
	if(!stateactions.isEmpty()) {
	    Enumeration enu = stateactions.keys();
	    while(enu.hasMoreElements()) {
		String state = (String) enu.nextElement();
		String action = (String) stateactions.get(state);
		try {
		    write(policyfile, state+"=>"+action+"\n", true);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    System.out.println("Cannot write to file");
		}
	    }
	} else
	    show("State actions table is empty!!");
    }
    
    public void filltable(File file) throws Exception {
	String s;
	stateactions = new Hashtable();
	FileReader readFile = new FileReader(file);
	BufferedReader readBuf = new BufferedReader(readFile);
	
	while((s = readBuf.readLine())!=null) {
	    //System.out.println(s);
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
