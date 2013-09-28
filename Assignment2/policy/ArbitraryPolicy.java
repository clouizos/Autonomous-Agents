package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import statespace.State;

public class ArbitraryPolicy implements Policy{
	
	private Map<String, String > policyCollections = new HashMap<String, String>();
	
	public static ArrayList<String> getAllActions(){
		ArrayList<String> allActions = new ArrayList<String>();
		allActions.add("north");
		allActions.add("south");
		allActions.add("west");
		allActions.add("east");
		allActions.add("wait");
		return allActions;
	}
	
	public ArbitraryPolicy(){
		ArrayList<String> actions = getAllActions();
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++){
				Random random = new Random();
				int index = random.nextInt(actions.size());
				String randAction = actions.get(index);
				String key = "["+i+"]["+j+"][5][5]";
				policyCollections.put(key, randAction);
			}
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
