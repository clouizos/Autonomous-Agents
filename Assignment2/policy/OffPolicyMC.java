package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import statespace.Position;
import statespace.State;

class DataEpisode{
	private State s;
	private Double reward;
	private String action;
	
	public DataEpisode(State s, double r, String a){
		this.s = s;
		this.reward = r;
		this.action =a;
	}
	public State getS() {
		return s;
	}
	public Double getReward() {
		return reward;
	}
	public String getAction() {
		return action;
	}
	
	public String toString(){
		return s.toString()+"-"+action+"-"+reward;
	}
	
}
public class OffPolicyMC {
	private Map<String, Double> Q;
	private Map<String, Double> N;
	private Map<String, Double> D;
	private Map<String, Double> W;
	private Policy preyPolicy;
	private Policy Pi;
	private Policy PiPrime;
	private double gamma = 0.8;
	private ArrayList<String> Actions=new ArrayList<String>();
	/**
	 * @param args
	 */
	
	public OffPolicyMC(Policy Pi){
		this.Pi = Pi;
		this.preyPolicy = preyPolicy;
		initialize();
	}
	
	public void initialize(){
		this.Actions = ArbitraryPolicy.getAllActions();
		
		//The key is String consist of State-action pair, and the Value is double.
		Q = new HashMap<String, Double>();
		N = new HashMap<String, Double>();
		D = new HashMap<String, Double>();
		W = new HashMap<String, Double>();
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++)
				for (String action:this.Actions){
					String key = "["+i+"]["+j+"][5][5]-"+action;
					Q.put(key,0.0);
					N.put(key,0.0);
					D.put(key,0.0);
					W.put(key,0.0);
				}
	}
	
	public double getReward(State s){
		if (s.endState())
			return 10;
		else
			return 0;
	}
	
	//
	public ArrayList<DataEpisode> generateEpisode(){
		ArrayList<DataEpisode> dataEpisode = new ArrayList<DataEpisode>();
		//Choose Initial State(position of predator), and follow policy until terminate
		Random generator = new Random();
		int i = generator.nextInt(11);
		int j = generator.nextInt(11);
//		Position pred = new Position(i,j);
//		Position prey = new Position(5,5);
		Position preyDefault = new Position(5,5);
		String predAction=null;
		String preyAction=null;
		Double r=0.0;
		State currentState;
		currentState = new State(new Position(i,j),new Position(5,5));
		int counter=0;
		while (! currentState.endState()) {
			counter++;
			predAction = PiPrime.getAction(currentState);
			dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey()),getReward(currentState),predAction));
			currentState.setPredator(currentState.getPredator().move(predAction));
			if (currentState.endState()){
				//Save the last State
				dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey()),getReward(currentState),null));
			} else{
				preyAction = preyPolicy.getAction(currentState);
				currentState.setPrey(currentState.getPrey().move(preyAction));
				Position predproj = currentState.getPredator().transformPrey55(currentState.getPrey());
				currentState.updatePosition(predproj, preyDefault);
			}
		}
		return dataEpisode;
	}
	
	public void doControl(){
		this.preyPolicy = new RandomPolicyPrey();
		ArrayList<DataEpisode> dataEpisode = new ArrayList<DataEpisode>();
		int counter=0;
		do{
			counter++;
			this.PiPrime = new EGreedyPolicy(this.Q);
			 do{
				 dataEpisode.clear();
				 dataEpisode =  generateEpisode();
			 }while (dataEpisode.size()<2);
			 //-2 because we exclude for checking the terminal state
			 int tau = 0;
			 for ( int i=dataEpisode.size()-2;i>=0;i--){
				 if (!dataEpisode.get(i).getAction().equals(this.Pi.getAction(dataEpisode.get(i).getS()))){
					 tau = i;
					 break;
				 }
			 }
			 System.out.println(counter+" Size episode "+dataEpisode.size()+" Tau : "+(dataEpisode.size()-tau));
			 State tempState;
			 String tempStateString;
			 String tempAction;
			 int t=0;
			 //Hashmap for save the first occurrence state,action pair 
			 HashMap<String, Integer> tableFirstTimeOccurence = new HashMap<String, Integer>();
			 //Copy from ArrayList to HashSet to avoid duplicate pair state,action
			 Set<String> set = new HashSet<String>();
//			 System.out.println("tau "+tau+"size episode "+dataEpisode.size());
			 for (int i=tau;i<dataEpisode.size()-1;i++){
				 if (!dataEpisode.get(i).getS().endState())
					 set.add(dataEpisode.get(i).getS().toString()+"-"+dataEpisode.get(i).getAction());
			 }
			 //For each pair state, action appearing in the episode after time tau
//			 System.out.println("size "+set.size());
			 for (String pair : set){
				 String[] tempPair = pair.split("-");
				 tempStateString = tempPair[0];
				 tempAction = tempPair[1];
				 //looking for the time of first occurrence of s,a in the table
				 if (tableFirstTimeOccurence.get(pair) != null)
					 t = tableFirstTimeOccurence.get(pair);
				 else //If it doesn't exist in the table so just find, and add to the table
					 for (int j=tau;j<dataEpisode.size();j++)
						 if ((dataEpisode.get(j).getS().toString()+"-"+dataEpisode.get(j).getAction()).equals(pair)){
							 t = j;
							 tableFirstTimeOccurence.put(pair, j);
							 break;	 
						 } 
				 double w=1.0;
				 double Return=0.0;
				 //Product of 1/piPrime(s_k,a_k)
				 double power=0;
				 for (int k=t+1;k<dataEpisode.size()-1;k++){
					 //For now we just use 0.5 since the probability of taking action in
					 //behaviour policy is equally likely
					 w*=1/((EGreedyPolicy)this.PiPrime).getProbability(dataEpisode.get(k).getS(), dataEpisode.get(k).getAction());
					 Return+=dataEpisode.get(k).getReward()*Math.pow(this.gamma, power++);
				 }
				 Return+=dataEpisode.get(dataEpisode.size()-1).getReward()*Math.pow(this.gamma, power);
//				 this.N.put(pair, this.N.get(pair)+w*Return);
//				 this.D.put(pair, this.D.get(pair)+w);
//				 this.Q.put(pair, this.N.get(pair)/this.D.get(pair));
				 //incremental
				 this.W.put(pair, this.W.get(pair)+w);
				 this.Q.put(pair, this.Q.get(pair)+(w/this.W.get(pair))*(Return-this.Q.get(pair)));
//				 System.out.println("Update Q at "+pair+"with value "+this.Q.get(pair));
			 }
//			 for (Map.Entry<String, Double> entry : this.Q.entrySet()) {
//				    String key = entry.getKey();
//				    Double value = entry.getValue();
//				    System.out.println(key+" "+value);
//			 }
			 
			 String key=null;
			 double Qval ;
			 //Part d
			 //Iterate over all States
//			 for (String action : this.Q.keySet())
//				 System.out.println(action);
			 for (int i=0;i<11;i++)
				 for (int j=0;j<11;j++){
					 if (i==5&&j==5)
						 continue;
					 double maxQ=0.0;
					 String maxAction=null;
					 //Iterate over all actions
//					 System.out.println("Size actions "+Actions.size());
					 for (String action : Actions){
						 key = "["+i+"]["+j+"][5][5]-"+action;
						 Qval = this.Q.get(key);
//						 System.out.println(action+" "+Qval);
//						 System.out.println(Qval);
						 if (Qval>=maxQ){
							 maxQ = Qval;
							 maxAction = action;
						 }
					 }
					 //Assign max action to the policy
					 ((ArbitraryPolicy)this.Pi).updateAction("["+i+"]["+j+"][5][5]", maxAction);
				 }
//			 System.out.println("Size Q : "+this.Q.size());
//			 System.out.println("Size N : "+this.N.size());
//			 System.out.println("Size D : "+this.D.size());
//			 System.out.println("Size table : "+tableFirstTimeOccurence);
//			 System.out.println("Size D : "+this.D.size());
			 
			 
		}while (counter <30000);
	}
	
	public Policy getPi() {
		return Pi;
	}
	
	public void printMaxQ(){
		String key;
		double Qval;
		
		String maxAction;
//		String action;
		ArrayList<String> Actions = ArbitraryPolicy.getAllActions();
		for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
//				key = this.statespace.toState(i, j, 0, 0);
				
				if (i==0 && j==0){
					System.out.printf("%f\t",0.0);
					continue;
				}
				double maxQ= 0.0;
				for (String action : Actions){
					 key = "["+j+"]["+i+"][5][5]-"+action;
//					 String stateAction = key+"-"+action;
//					 System.out.println(stateAction);
					 Qval = this.Q.get(key);
//					 System.out.println(action+" "+Qval);
//					 System.out.println(Qval);
					 if (Qval>=maxQ){
						 maxQ = Qval;
						 maxAction = action;
					 }
//					 System.out.println(action+" "+maxQ);
					 
				 }
//				action = ((ArbitraryPolicy)off.getPi()).getPolicyCollections().get(key);
				System.out.printf("%f\t",maxQ);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArbitraryPolicy Pi = new ArbitraryPolicy();
		OffPolicyMC off = new OffPolicyMC(Pi);
		
		off.doControl();
//		Map<String, String > collection = ((ArbitraryPolicy)off.getPi()).getPolicyCollections();
//		for (Map.Entry<String, String> entry : collection.entrySet()) {
//		    String key = entry.getKey();
//		    String value = entry.getValue();
//		    System.out.println(key);
//		}
		off.printMaxQ();
		String key;
		String action;
		for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				key = "["+j+"]["+i+"][5][5]";
				action = ((ArbitraryPolicy)off.getPi()).getPolicyCollections().get(key);
				System.out.printf("%s\t",action);
			}
			System.out.println();
		}
	}

}
