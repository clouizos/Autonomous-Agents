package policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import statespace.*;

/*
 * Interface for all policies; a policy must contain a getAction method.
 */
public class PolicySelect {
	public double parameter;
	
	public PolicySelect() {}
	
	public PolicySelect(double p) {
		parameter = p;
	}

	public String getAction(State s, Map<State, Double> qtable) {return "wait";}
	//public String getAction(State s, Map<String, Double> qtable) {return "wait";}

	public double getParameter() {
		return parameter;
	}

	public void setParameter(double parameter) {
		this.parameter = parameter;
	}
	
	public static ArrayList<String> getAllActions(){
		String[] actions = {"north","south","west","east","wait"};
		ArrayList<String> allActions = new ArrayList(Arrays.asList(actions));
		return allActions;
	}
}