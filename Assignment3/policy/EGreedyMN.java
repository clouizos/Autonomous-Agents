package policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import statespace.Position;
import statespace.State;

public class EGreedyMN extends PolicySelect{
	
	private HashMap<State, Double> probState;
	ArrayList<String> actionS = new ArrayList<String>();
	public EGreedyMN(double epsilon){
		super(epsilon);
		probState = new HashMap<State, Double>();
		actionS = PolicySelect.getAllActions();
	}

	//@Override
	public String getActionMM(State s){
		ArrayList<String> actionS = PolicySelect.getAllActions();
		// randomized list, so no move will get selected by default/preference at init stage
		long seed = System.nanoTime();
		Collections.shuffle(actionS, new Random(seed));
		double epsilon = super.parameter;
		State key;
		Double prob;
		double maxProb=-1.0;
		//double qVal;
		//double maxQ=-10;
		String maxAction="wait";
		
		for (String action : actionS){
			key = new State(s.getPrey(), action);
			for (int i =0; i< s.getPredators().size(); i++){
				key.addPred(s.getPredators().get(i));
			}
			prob = probState.get(key);
			if(prob == null){
				probState.put(key, 0.2);
				prob = 0.2;
			}
			if(prob > maxProb){
				maxProb = prob;
				maxAction = action;
			}
		}
		Random rand = new Random();
		int index;
		if (Math.random()<epsilon){
			index = rand.nextInt(5);
			//actionS.remove(maxAction);
			return actionS.get(index);
		}
		return maxAction;
	}
	
	public void updateProb(State s, double[] probs){
		
		for(int i=0; i < actionS.size();i++){
			State key = new State(s,actionS.get(i));
			probState.put(key, probs[i]);
		}
	}
	
	public static void main(String[] args) {
		Position p1 = new Position(7, 4);
		Position prey = new Position(5,5);
		State s = new State(prey, "wait");
		s.addPred(p1);
		
		// TODO Auto-generated method stub

	}

}
