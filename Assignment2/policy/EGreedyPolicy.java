package policy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import statespace.State;

public class EGreedyPolicy implements Policy{
	private Map<String, Double> Q;
	private double e=0.1;
	
	public EGreedyPolicy(Map<String, Double> Q){
		this.Q = Q;
	}
	
	public String getAction(State s){
		ArrayList<String> Actions = ArbitraryPolicy.getAllActions();
		String key=null;
		double Qval;
		double maxQ=-10;
		String maxAction=null;
		for (String action : Actions){
			key = s.toString()+"-"+action;
			Qval = this.Q.get(key);
			if (maxQ<Qval){
				maxQ = Qval;
				maxAction = action;
			}
		}
		Random rand = new Random();
		int index;
		if (Math.random()<this.e){
			index = rand.nextInt(4);
			Actions.remove(maxAction);
			return Actions.get(index);
		}
		return maxAction;
	}
	
	public double getProbability(State s, String a){
		ArrayList<String> Actions = ArbitraryPolicy.getAllActions();
		String key=null;
		double Qval;
		double maxQ=-10;
		String maxAction=null;
		for (String action : Actions){
			key = s.toString()+"-"+action;
			Qval = this.Q.get(key);
			if (maxQ<Qval){
				maxQ = Qval;
				maxAction = action;
			}
		}
		
		if (a.equals(maxAction))
			return 1-this.e;
		else
			return e/4;
	}

}
