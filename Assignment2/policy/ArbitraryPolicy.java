package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import statespace.Position;
import statespace.State;

public class ArbitraryPolicy implements Policy{
	
	private Map<String, String > policyCollections = new HashMap<String, String>();
	
	public Map<String, String> getPolicyCollections() {
		return policyCollections;
	}

	public static ArrayList<String> getAllActions(){
		ArrayList<String> allActions = new ArrayList<String>();
		allActions.add("south");
		allActions.add("north");
		allActions.add("west");
		allActions.add("east");
		allActions.add("wait");
		return allActions;
	}
	
	public boolean generateEpisode(int initialx, int initialy, RandomPolicyPrey preyPolicy){
		Position preyDefault = new Position(5,5);
		Double r=0.0;
		State currentState;
		currentState = new State(new Position(initialx,initialy),new Position(5,5));
		String predAction=null;
		String preyAction=null;
		boolean valid=true;
		int counter=0;
		do {
			counter++;
			predAction = getAction(currentState);
			currentState.setPredator(currentState.getPredator().move(predAction));
			preyAction = preyPolicy.getAction(currentState);
			currentState.setPrey(currentState.getPrey().move(preyAction));
			Position predproj = currentState.getPredator().transformPrey55(currentState.getPrey());
			currentState.updatePosition(predproj, preyDefault);
		}while (! currentState.endState() && counter<500); 	//500 is threshold for number of state in an 
															//episode to avoid never end episode
		if (counter>=500)
			valid = false;
		return valid;
	}
	
	public void initialize(){
		if (this.policyCollections.size()!=0)
			this.policyCollections.clear();
		ArrayList<String> actions = getAllActions();
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++){
				Random random = new Random();
				int index = random.nextInt(actions.size());
				String randAction = actions.get(index);
				String key = "["+i+"]["+j+"][5][5]";
				this.policyCollections.put(key, randAction);
			}
	}
	
	public ArbitraryPolicy(){
		RandomPolicyPrey preyPolicy = new RandomPolicyPrey();
		boolean valid;
		//Check whether this policy is valid (can reach terminal state) for all initial states.
		do{
			initialize();
			valid=true;
			for (int i=0;i<11;i++)
				for (int j=0;j<11;j++){
					valid = generateEpisode(i, j, preyPolicy);
					if (valid==false)
						break;
				}			
		}while (valid==false);

	}
	
	public void updateAction(State s, String action){
		policyCollections.put(s.toString(), action);
	}
	
	public void updateAction(String key, String action){
		policyCollections.put(key, action);
	}
	
	public String getAction(State cs){
		String action = policyCollections.get(cs.toString());
		return action;
	}
	
	public String getAction(String key){
		String action = policyCollections.get(key);
		return action;
	}
	

}
