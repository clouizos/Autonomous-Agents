package policy;

import statespace.*;

import java.io.*;
import java.util.*;

public class VIPolicyReduced implements Policy {
	/* max statespace state[i][j][k][l]
	 * where predator[i][j] prey[k][l]
	 */
	
	/* gamma = discount factor
	 * theta = small positive number; threshold for continueing evaluation
	 * delta = change in state value  
	 */
    private State[][] statespace;
    private static Hashtable stateactions, statevalues;
    private double gamma, delta, theta;

    public VIPolicyReduced(double g, double t) {
       // statespace init
	statespace = new State[11][11];
    stateactions = new Hashtable<>();
    statevalues = new Hashtable<>();
    
	for(int i = 0; i < 11; i++) {
	    for(int j = 0; j < 11; j++) {
	    			State s = new State(new Position(i, j), new Position(5, 5));
	    			statespace[i][j] = s;
	    			statevalues.put(s.toString(), (Double)0.0);
	    		}
	}
        gamma = g;
        theta = t;
	}
    
    public VIPolicyReduced() {}

    public static void main(String[] args) {
        // value iteration is run with VIpolicy(gamma, theta)
    	VIPolicyReduced p = new VIPolicyReduced(0.5, 0.001);
        p.multisweep();
        Position prey = new Position(5,5);
        p.printList(prey);
        p.printTable(prey);
        
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

    /*
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
    
    // the outer loop: repreat untill delta is smaller than theta
    public void multisweep() {
        int k = 0;
        //int depth = 0;
        delta = 0;
        stateactions.clear();
        do {
            delta = 0;
            //stateactions.clear();
            delta = sweep();
            show("\ndelta: " + delta+'\n');
            show("\ntheta: " + theta+'\n');
            k++;
            //--k;
        } //while (k>0);
        while(delta > theta);
        show("\nnr of iterations: "+k+'\n');
    }
/*
 *     2nd loop: for each state perform the update of state value.
 *     Policy evaluation is stopped after just one sweep 
 *     (one backup of each state).
 */
    public double sweep() {
        double v;
        double vUpdate;
        //loop: for every state/node
        for(int i=0;i<11;i++) {
            for(int j=0;j<11;j++) {
    	    			State currentState = statespace[i][j];
    	    			v = (double) statevalues.get(currentState.toString());
    	    			//show("current value: "+v+'\n');
    	    			vUpdate = updateValue(currentState);
    	    			//show("updated value: "+vUpdate+'\n');
    	    			// put the statevalue for currentState in the look up table
    	    	        statevalues.put(currentState.toString(), vUpdate);
    	    			//show("check updated value: "+currentState.getValue()+'\n');
    	    			delta = Math.max(delta, Math.abs(v - vUpdate));
    	    		}
        }
        //show("booya delta: " + delta);
        return delta;
    }
    /*
     * Backup operation that combines the policy improvement 
     * and truncated policy evaluation steps.
     */
    public double updateValue(State cS) {
    	String action = "wait"; // action
    	State currentState = cS;
    	State nextState;
    	String[] moves = {"north", "south", "east", "west", "wait"};
    	double[] actions = {0,0,0,0,0};
    	double nextStateValue;
    	Vector nextStates;
    	for(int i=0;i<moves.length;i++) {
    		action = moves[i];
    		if(cS.endState())
    			continue;
    		nextStates = currentState.nextStatesReduced(action);
    		for(int j=0; j<nextStates.size();j++) {
    			nextState = (State) nextStates.elementAt(j);
    			nextStateValue = (double)statevalues.get(nextState.toString());
    			actions[i] += getP(nextStates.size(), nextState) *
    					(getReward(nextState) + (gamma * nextStateValue));
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
    	stateactions.put(currentState.toString(), action);
    	return max;
    }

    /*  Uncertainty in prey action is expressed in P; prey is modeled into the state.
    *	See State.nextStates()
    */
    public double getP(int nrnextstates, State next) {
        if(next.endState())	
        	return 1.0;
        else if(next.getPreyaction().equals("wait"))
    	    return 0.8;
    	else
    	    return (0.2/nrnextstates);
        }

    // implement reward function: only when captured the immediate award=10, else 0
    public double getReward(State s) {
        if(s.endState())
            return 10.0;
	return 0.0;
    }

    public void show(String s) {
        System.out.print(s);
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
    
    // outputs the state actions into a file policy.data
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
    
    // fill the stateactions "look up table" with the values in policy.data
    public void filltable(File file) throws Exception {
	String s;
	stateactions = new Hashtable<> ();
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
    
    public void printTable(Position prey){

    	// outputs the values of all states where state:predator[i][j]prey[5][5] in a grid
    	show("\n======statevalues in grid around prey[5][5]======\n");
    	int nextline = 0;
    	for(int i = 0; i < 11; i++) {
    		if(nextline < i) {show("\n");}
    		for(int j = 0; j < 11; j++) {
    			State statePrey = new State(new Position(i,j), prey);
    			//polEval.show(String.format( "%.20f",(double)statevalues.get(statePrey55.toString())) + " ");
    			show(String.format( "%.3f",(double) statevalues.get(statePrey.toString())) + " ");
    		}
    		nextline = i;
    	}
    }

    public void printList(Position prey){
    	for(int i = 0; i < 11; i++) {
    		for(int j = 0; j < 11; j++) {
    			State statePrey = new State(new Position(i,j), prey);
    			show('\n'+ statePrey.toString() +  " statevalue: " +(double)statevalues.get(statePrey.toString()));
    		}
    	}
    }
}
