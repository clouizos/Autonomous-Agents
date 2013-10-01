package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import statespace.Position;
import statespace.State;

public class OnPolicyMC {
	private static Map<State, Double> Q;
	private static Map<State, Double> prob;
	private static Map<State, ArrayList<Double>> returns;
	private static Map<String, String> bestActioninState;
	//private Map<String, Double> V;
	private Policy preyPolicy;
	private static ESoftPolicy Pi;
	private static ArrayList<String> Actions=new ArrayList<String>();
	
	public OnPolicyMC(Policy P) {
		//this.Pi = Pi;
		Pi = (ESoftPolicy) P;
		//this.preyPolicy = preyPolicy;
		initialize();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize(){
		Actions = ArbitraryPolicy.getAllActions();
		//The key is String consist of State-action pair, and the Value is double.
		Q = new HashMap<State, Double>();
		prob = new HashMap<State, Double>();
		//V = new HashMap<String, Double>();
		//return_list = new ArrayList<Double>();
		returns = new HashMap<State, ArrayList<Double>>();
		//N = new HashMap<String, Double>();
		//D = new HashMap<String, Double>();
		Position prey = new Position(5,5);
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++)
				for (String action:this.Actions){
					//String key = "["+i+"]["+j+"][5][5]-"+action;
					State init = new State(new Position(i,j), prey, action);
					Q.put(init,Math.random() * 5+1);
					prob.put(init, 0.0);
					ArrayList<Double> return_list = new ArrayList<Double>();
					returns.put(init, return_list);
					//V.put(key, 0.0);
					//D.put(key,0.0);
				}
	}
	
	public ArrayList<DataEpisode> generateEpisode(){
		ArrayList<DataEpisode> dataEpisode = new ArrayList<DataEpisode>();
		//Choose Initial State(position of predator), and follow policy until terminate
		Random generator = new Random();
		int i = generator.nextInt(11);
		int j = generator.nextInt(11);
		//Position pred = new Position(i,j);
		//Position prey = new Position(5,5);
		Position preyDefault = new Position(5,5);
		String predAction=null;
		String preyAction=null;
		Double r=0.0;
		State currentState;
		currentState = new State(new Position(i,j),new Position(5,5));
		int counter=0;
		//System.out.println("generating episode..");
		do {
			//System.out.println(counter);
			counter++;
			predAction = Pi.getAction(currentState);
			dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey(), predAction),getReward(currentState),predAction));
			currentState.setPredator(currentState.getPredator().move(predAction));
			//System.out.println("current state reward:"+getReward(currentState));
			//Save the last State
			if (currentState.endState()){
				dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey(), null),getReward(currentState),null));
			}else{
				preyAction = preyPolicy.getAction(currentState);
				currentState.setPrey(currentState.getPrey().move(preyAction));
				Position predproj = currentState.getPredator().transformPrey55(currentState.getPrey());
				currentState.updatePosition(predproj, preyDefault);
			}
			//r = getReward(currentState);
		}while (! currentState.endState());
		//System.out.println("Generated episode that ends in "+counter+" moves!");
		//System.out.println(dataEpisode);
		return dataEpisode;
	}
	
	
	public void doControl(){
		this.preyPolicy = new RandomPolicyPrey();
		ArrayList<DataEpisode> dataEpisode = new ArrayList<DataEpisode>();
		int counter=0;
		System.out.println("controling...");
		double Return = 0.0;
		do{
			counter++;
			//this.Pi = new EGreedyPolicy(OnPolicyMC.Q);
			 do{
				 dataEpisode.clear();
				 dataEpisode =  generateEpisode();
			 }while (dataEpisode.size()<2);
			 System.out.println("Episode size:"+dataEpisode.size());
			 //-2 because we exclude for checking the terminal state
			 /*
			 int tau = 0;
			 for ( int i=dataEpisode.size()-2;i>=0;i--){
				 if (!dataEpisode.get(i).getAction().equals(this.Pi.getAction(dataEpisode.get(i).getS()))){
					 tau = i;
					 break;
				 }
			 }
			 */
			 
			 //State tempState;
			 //String tempStateString;
			 //String tempAction;
			 ArrayList<Double> tempreturns;
			 //double old_value;
			 double average_returns;
			 int t=0;
			 //Hashmap for save the first occurrence state,action pair 
			 HashMap<State, Integer> tableFirstTimeOccurence = new HashMap<State, Integer>();
			 //Copy from ArrayList to HashSet to avoid duplicate pair state,action
			 
			 Set<State> set = new HashSet<State>();

			 for (int i=0;i<dataEpisode.size();i++){
				 if (!dataEpisode.get(i).getS().endState())
					 //set.add(dataEpisode.get(i).getS().toString()+"-"+dataEpisode.get(i).getAction());
					 set.add(dataEpisode.get(i).getS());
			 }
  		// System.out.println("size "+set.size());
			 for (State currState : set){
				 //String[] tempPair = pair.split("-");
				 //tempStateString = tempPair[0];
				 //tempAction = tempPair[1];
				 //looking for the time of first occurrence of s,a in the table
				 if (tableFirstTimeOccurence.get(currState) != null)
					 t = tableFirstTimeOccurence.get(currState);
				 else //If it doesn't exist in the table so just find, and add to the table
					 for (int j=0;j<dataEpisode.size();j++)
						 if (dataEpisode.get(j).getS().equals(currState)){
							 t = j;
							 tableFirstTimeOccurence.put(currState, j);
							 break;	 
						 } 
				 //double w=1.0;
				 //double Return=0.0;
				 //Product of 1/piPrime(s_k,a_k)
				 for (int k=t+1;k<dataEpisode.size();k++){
					 //For now we just use 0.5 since the probability of taking action in
					 //behaviour policy is equally likely
					 //w*=1/((EGreedyPolicy)this.Pi).getProbability(dataEpisode.get(k).getS(), dataEpisode.get(k).getAction());
					 Return+=dataEpisode.get(k).getReward();
				 }
				 //Return+=dataEpisode.get(dataEpisode.size()-1).getReward();
				 tempreturns = OnPolicyMC.returns.get(currState);
				 tempreturns.add(Return);
				 average_returns = calculateAverage(tempreturns);
//				 System.out.println("nilai w"+w);
				 //get the Reward at time t. Need confirmation about R_t or R_t+1
//				 System.out.println(pair);
				 //if (w*Return > 0|| w >0)
//					 System.out.println(w*Return+" "+w);
				 //this.N.put(pair, this.N.get(pair)+w*Return);
				 //this.D.put(pair, this.D.get(pair)+w);
				 returns.put(currState, tempreturns);
				 Q.put(currState, average_returns);
				 Return = 0.0;
			 }
//			 for (Map.Entry<String, Double> entry : this.Q.entrySet()) {
//				    String key = entry.getKey();
//				    Double value = entry.getValue();
//				    System.out.println(key+" "+value);
//			 }
			 
			 State key=null;
			 double Qval ;
			 //Part d
			 //Iterate over all States
//			 for (String action : this.Q.keySet())
//				 System.out.println(action);
			 Position prey = new Position(5,5);
			 for (int i=0;i<11;i++)
				 for (int j=0;j<11;j++){
					 double maxQ=-10;
					 String maxAction=null;
					 //Iterate over all actions
//					 System.out.println("Size actions "+Actions.size());
					 for (String action : Actions){
						 //key = "["+i+"]["+j+"][5][5]-"+action;
						 key = new State(new Position(i,j), prey, action);
						 Qval = Q.get(key);
//						 System.out.println(action+" "+Qval);
//						 System.out.println(Qval);
						 if (Qval>=maxQ){
							 maxQ = Qval;
							 maxAction = action;
						 }
					 }
					 // assign new values for the state action pair in the episode
					
					 //old_value = ((ArbitraryPolicy)this.Pi).getValue(key);
					 //ArrayList<String> actions = ArbitraryPolicy.getAllActions();
					 double new_Value;
					 double e = 0.1;
					 for(String action : Actions){
						 //key = "["+i+"]["+j+"][5][5]-"+action;
						 key = new State(new Position(i,j), prey, action);
						 if (action.equals(maxAction)){
							 new_Value = 1-e+(e/Actions.size());
							 Pi.updateProbablity(key, action, new_Value);
							 //((ArbitraryPolicy)this.Pi).updateValue(key, new_Value);
						 }else{
							 new_Value = e/Actions.size();
							 Pi.updateProbablity(key, action, new_Value);
							 //((ArbitraryPolicy)this.Pi).updateValue(key, new_Value);
						 }
					 }
					 
					 //Assign max action to the policy
					 //((ArbitraryPolicy)this.Pi).updateAction("["+i+"]["+j+"][5][5]", maxAction);
				 }
			 System.out.println("doing control step "+counter);
		//}while (counter <2000000);
		}while(counter < 1000);
		
	}
	
	private double calculateAverage(ArrayList <Double> returns) {
		  double sum = 0.0;
		  //System.out.println(returns);
		  if(!returns.isEmpty()) {
		    for (double return_ : returns) {
		        sum += return_;
		    }
		    return sum / returns.size();
		  }
		  return sum;
		}
	
	public double getReward(State s){
		if (s.endState())
			return 10;
		else
			return 0;
	}
	
	public Policy getPi() {
		return Pi;
	}
	
	public static void main(String[] args) {
		//ArbitraryPolicy Pi = new ArbitraryPolicy();
		ESoftPolicy Pi = new ESoftPolicy(0.1);
		OnPolicyMC on = new OnPolicyMC(Pi);
		
		on.doControl();
		bestActioninState = new HashMap<String, String>();
		ESoftPolicy Piprime = new ESoftPolicy(-1.0);
		Piprime.setPolicyProb(Pi.getPolicyProb());
		//String key;
		State key;
		//String action;
		//System.out.println(Q);
		//System.out.println(Q.size());
		for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				key = new State(new Position(i,j), new Position(5,5));
				/*for (String action : Actions){
					key = new State(new Position(i,j), new Position(5,5), action);
				//key = "["+i+"]["+j+"][5][5]";
				//action = key.getAction();
				//action = ((ArbitraryPolicy)on.getPi()).getPolicyCollections().get(key);
				//System.out.printf("%s\t",action);
					System.out.print("["+i+"]"+"["+j+"]"+"[5][5]-"+action+" ");
					System.out.println(Q.get(key));
				}*/
				String action = Piprime.getAction(key);
				System.out.printf("%s\t",action);
				bestActioninState.put(key.toString(), action);
			}
			System.out.println();	
		}
		System.out.println();
		for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				key = new State(new Position(i,j), new Position(5,5));
				/*for (String action : Actions){
					key = new State(new Position(i,j), new Position(5,5), action);
				//key = "["+i+"]["+j+"][5][5]";
				//action = key.getAction();
				//action = ((ArbitraryPolicy)on.getPi()).getPolicyCollections().get(key);
				//System.out.printf("%s\t",action);
					System.out.print("["+i+"]"+"["+j+"]"+"[5][5]-"+action+" ");
					System.out.println(Q.get(key));
				}*/
				String action = bestActioninState.get(key.toString());
				State keyQ = new State(new Position(i,j), new Position(5,5), action);
				System.out.printf("%.2f\t",Q.get(keyQ));
			}
			System.out.println();	
		}
	}
}
