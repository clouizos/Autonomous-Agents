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
	private Policy preyPolicy;
	private Policy Pi;
	private Policy PiPrime;
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
		Actions = ArbitraryPolicy.getAllActions();
		
		//The key is String consist of State-action pair, and the Value is double.
		Q = new HashMap<String, Double>();
		N = new HashMap<String, Double>();
		D = new HashMap<String, Double>();
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++)
				for (String action:this.Actions){
					String key = "["+i+"]["+j+"][5][5]-"+action;
					Q.put(key,Math.random() * 5+1);
					N.put(key,0.0);
					D.put(key,0.0);
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
		Position pred = new Position(i,j);
		Position prey = new Position(5,5);
		Position preyDefault = new Position(5,5);
		String predAction=null;
		String preyAction=null;
		Double r=0.0;
		State currentState;
		currentState = new State(pred,prey);
		int counter=0;
		do {
			counter++;
			predAction = PiPrime.getAction(currentState);
			dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey()),r,predAction));
			currentState.setPredator(currentState.getPredator().move(predAction));
			preyAction = preyPolicy.getAction(currentState);
			currentState.setPrey(currentState.getPrey().move(preyAction));
			Position predproj = currentState.getPredator().transformPrey55(currentState.getPrey());
			currentState.updatePosition(predproj, preyDefault);
			r = getReward(currentState);
			//Save the last State
			if (currentState.endState())
				dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey()),r,null));
		}while (! currentState.endState());
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
			 
			 
			 State tempState;
			 String tempStateString;
			 String tempAction;
			 int t=0;
			 //Hashmap for save the first occurrence state,action pair 
			 HashMap<String, Integer> tableFirstTimeOccurence = new HashMap<String, Integer>();
			 //Copy from ArrayList to HashSet to avoid duplicate pair state,action
			 Set<String> set = new HashSet<String>();
//			 System.out.println("tau "+tau+"size episode "+dataEpisode.size());
			 for (int i=tau;i<dataEpisode.size();i++){
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
				 if (tableFirstTimeOccurence.get(tempStateString) != null)
					 t = tableFirstTimeOccurence.get(tempStateString);
				 else //If it doesn't exist in the table so just find, and add to the table
					 for (int j=tau;j<dataEpisode.size();j++)
						 if (dataEpisode.get(j).getS().toString().equals(tempStateString)){
							 t = j;
							 tableFirstTimeOccurence.put(tempStateString, j);
							 break;	 
						 } 
				 double w=1.0;
				 double Return=0.0;
				 //Product of 1/piPrime(s_k,a_k)
				 for (int k=t+1;k<dataEpisode.size()-1;k++){
					 //For now we just use 0.5 since the probability of taking action in
					 //behaviour policy is equally likely
					 w*=1/((EGreedyPolicy)this.PiPrime).getProbability(dataEpisode.get(k).getS(), dataEpisode.get(k).getAction());
					 Return+=dataEpisode.get(k).getReward();
				 }
				 Return+=dataEpisode.get(dataEpisode.size()-1).getReward();
//				 System.out.println("nilai w"+w);
				 //get the Reward at time t. Need confirmation about R_t or R_t+1
//				 System.out.println(pair);
				 if (w*Return > 0|| w >0)
//					 System.out.println(w*Return+" "+w);
				 this.N.put(pair, this.N.get(pair)+w*Return);
				 this.D.put(pair, this.D.get(pair)+w);
				 this.Q.put(pair, this.N.get(pair)/this.D.get(pair));
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
					 double maxQ=-10;
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
		}while (counter <2000000);
	}
	
	public Policy getPi() {
		return Pi;
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
		String key;
		String action;
		for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				key = "["+i+"]["+j+"][5][5]";
				action = ((ArbitraryPolicy)off.getPi()).getPolicyCollections().get(key);
				System.out.printf("%s\t",action);
			}
			System.out.println();
		}
	}

}
