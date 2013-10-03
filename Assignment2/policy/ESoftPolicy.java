package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import statespace.*;

public class ESoftPolicy implements Policy{

	//private Map<String, Map<State,Double> > policyProb = new HashMap<String, Map<String,Double>>();
	private Map<String, Map<String, Double>> policyProb = new HashMap<String, Map<String, Double>>();
	private Map<String, Double> actionProb; 
	private double epsilon;
	//private Statespace sp;
	private State currentState;
	private ArrayList<String> allActions = new ArrayList<String>();
	
	public ESoftPolicy(double epsilon) {
		allActions = ArbitraryPolicy.getAllActions();
		for (int i=0; i< 11; i++)
			for(int j=0; j<11;j++){
				currentState = new State(new Position(i,j), new Position(5,5));
				actionProb =  new HashMap<String, Double>();
				for (String action : allActions){
					actionProb.put(action, 0.2);
				}
				String textState = currentState.toString();
				policyProb.put(textState, actionProb);
			}
		this.epsilon = epsilon;
		/*
		this.currentState = currentState;
		this.epsilon = epsilon;
		ArrayList<String> actions = ArbitraryPolicy.getAllActions();
		for (State key : statespace.keySet()){
			Map<String,Double> actprob = new HashMap<String,Double>();
			for (String a:actions){
				actprob.put(a, 0.2);
			}
		}
		policyCollections.put(key,actprob);
		*/
		// TODO Auto-generated constructor stub
	}
	/*
	public void updateAction(String state, Map<String, Double> value){
		state = sp.toState(sp.getPredator(state), sp.getPrey(state));
		policyCollections.put(state, value);
	}*/
	
	public void updateProbablity(State s, String action, double prob){
		policyProb.get(s.toString()).put(action, prob);
	}
	

	public String getAction(State cs){
		String state = cs.toString();
		Map<String,Double> actprob = new HashMap<String,Double>();
		actprob = policyProb.get(state);
		Random random = new Random();
		double e = Math.random();
		double maxProb = 0.0;
		String bestAction = null;
		String randomKey = null;
		double lowProb = 0.0;
		//find best action according to the probabilities of each action
		for(String action : allActions){
			double newProb = actprob.get(action);
			if (newProb > maxProb){
				maxProb = newProb;
				bestAction = action; 
			}else{
				lowProb = newProb;
			}
		}
		/*
		System.out.println("high: "+maxProb);
		System.out.println("low: "+lowProb);
		System.out.println("e: "+e);
		*/
		//if you need to select a non greedy action return a random one, except the best one
		if (e < lowProb){
			actprob.remove(bestAction);
			List<String> keys = new ArrayList<String>(actprob.keySet());
			randomKey = keys.get( random.nextInt(keys.size()));
			actprob.put(bestAction, maxProb);
			return randomKey;	
		}
		//System.out.println("best action");
		//else return the optimal one calculated above
		return bestAction;
	}

	public double getActionProb(State state, String action){
		return policyProb.get(state.toString()).get(action);
	}	

	/*public static void main(String[] args){
		
	}*/
	
	public Map<String, Map<String, Double>> getPolicyProb() {
		return policyProb;
	}

	public void setPolicyProb(Map<String, Map<String, Double>> policyProb) {
		this.policyProb = policyProb;
	}

}
