package policy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import statespace.Position;
import statespace.State;

import simulation.Testsimulation;

public class OnPolicyMC {
	private static Map<State, Double> Q;
	private static Map<State, ArrayList<Double>> returns;
	private static Map<String, String> bestActioninState;
	private static ArrayList<Integer> runsEachEpisode = new ArrayList<Integer>();
	private Policy preyPolicy;
	private static ESoftPolicy Pi;
	private static double gamma;
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
		this.gamma = 0.8;
		//The key is String consist of State-action pair, and the Value is double.
		Q = new HashMap<State, Double>();
		
		returns = new HashMap<State, ArrayList<Double>>();
		
		//prey fixed
		Position prey = new Position(5,5);
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++)
				for (String action:this.Actions){
					//String key = "["+i+"]["+j+"][5][5]-"+action;
					State init = new State(new Position(i,j), prey, action);
					Q.put(init,0.0);
					//Q.put(init, 0.0);
					ArrayList<Double> return_list = new ArrayList<Double>();
					returns.put(init, return_list);
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
		while(!currentState.endState()) {
			//System.out.println(counter);
			counter++;
			predAction = Pi.getAction(currentState);
			dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey(), predAction),getReward(currentState),predAction));
			currentState.setPredator(currentState.getPredator().move(predAction));
			//if(getReward(currentState)!=0){
			//	System.out.println(currentState);
			//	System.out.println(getReward(currentState));
			//}
			//System.out.println("current state reward:"+getReward(currentState));
			//Save the last State
			if (currentState.endState()){
				//show("generated end state");
				dataEpisode.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey(), null),getReward(currentState),null));
			}else{
				preyAction = preyPolicy.getAction(currentState);
				currentState.setPrey(currentState.getPrey().move(preyAction));
				Position predproj = currentState.getPredator().transformPrey55(currentState.getPrey());
				currentState.updatePosition(predproj, preyDefault);
			}
			//r = getReward(currentState);
		}//while (! currentState.endState());
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
			 runsEachEpisode.add(dataEpisode.size());
			 //-2 because we exclude for checking the terminal state
			
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
				 //int power = dataEpisode.size() - t+1;
				 int power = 0;
				 //System.out.println(power);
				 for (int k=t+1;k<dataEpisode.size();k++){
					 //For now we just use 0.5 since the probability of taking action in
					 //behaviour policy is equally likely
					 //w*=1/((EGreedyPolicy)this.Pi).getProbability(dataEpisode.get(k).getS(), dataEpisode.get(k).getAction());
					 State tempState = dataEpisode.get(k).getS();
					 Position pred = new Position(tempState.getPredator());
					 Position prey = new Position(tempState.getPrey());
					 String action = dataEpisode.get(k).getAction();
					 //System.out.println(action);
					 // if end state continue
					 double oldQ = 0.0;
					 if (action == null){
						 //show("end state");
						 //show(tempState.toString());
						 oldQ = 10.0;
					 }else{
						 State key = new State(pred,prey,action);
						 //System.out.println(key.getAction());
						 //System.out.println(OnPolicyMC.Q);
						 oldQ = Q.get(key);
						 //Return += oldQ + gamma*(dataEpisode.get(k).getReward() - oldQ);
					 }
					 //if(dataEpisode.get(k).getReward() != 0)
					//	 System.out.println(dataEpisode.get(k).getReward());
					 Return +=  Math.pow(gamma,power)*(dataEpisode.get(k).getReward());
					 power ++;
					 //Return += oldQ + gamma*(dataEpisode.get(k).getReward() - oldQ); 
					 //Return+=dataEpisode.get(k).getReward() + (gamma*dataEpisode.get(k).getReward());
				 }
				 //Return+=dataEpisode.get(dataEpisode.size()-1).getReward();
				 tempreturns = OnPolicyMC.returns.get(currState);
				 tempreturns.add(Return);
				 average_returns = calculateAverage(tempreturns);

				 //System.out.println(currState.toString()+" "+average_returns);
				 returns.put(currState, tempreturns);
				 Q.put(currState, average_returns);
				 Return = 0.0;
			 }

			 State key=null;
			 double Qval ;

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
						 //System.out.println("Action:"+action+" value:"+Qval);
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
					 double e = Pi.getEpsilon();
					 for(String action : Actions){
						 //key = "["+i+"]["+j+"][5][5]-"+action;
						 key = new State(new Position(i,j), prey, action);
						 if (action.equals(maxAction)){
							 //System.out.println("best action is:"+action);
							 new_Value = 1-e+(e/Actions.size());
							 Pi.updateProbablity(key, action, new_Value);
							 //((ArbitraryPolicy)this.Pi).updateValue(key, new_Value);
						 }else{
							 new_Value = e/Actions.size();
							 Pi.updateProbablity(key, action, new_Value);
							 //((ArbitraryPolicy)this.Pi).updateValue(key, new_Value);
						 }
					 }
					 
					 //adaptive epsilon
					 /*
					 if (counter%100==0){
						e = e*0.8;
						Pi.setEpsilon(e);
					 }
					 */
					 
					 //Assign max action to the policy
					 //((ArbitraryPolicy)this.Pi).updateAction("["+i+"]["+j+"][5][5]", maxAction);
				 }
			 System.out.println("doing control step "+counter);
		//}while (counter <2000000);
		}while(counter < 20000);
		
		
		/*
		double sum = 0.0;
		for (int i=0;i<runsEachEpisode.size();i++)
			sum+=runsEachEpisode.get(i);
		double average = (double)sum/runsEachEpisode.size();
		System.out.println("average "+average);
		*/
		
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
		ESoftPolicy Pi = new ESoftPolicy(0.5);
		OnPolicyMC on = new OnPolicyMC(Pi);
		
		on.doControl();
		bestActioninState = new HashMap<String, String>();
		ESoftPolicy Piprime = new ESoftPolicy(-1.0);
		Piprime.setPolicyProb(Pi.getPolicyProb());
		System.out.println("begining evaluation!");
		//String key;
		State key;
		//String action;
		//System.out.println(Q);
		//System.out.println(Q.size());
		for (int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				key = new State(new Position(j,i), new Position(5,5));
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
				key = new State(new Position(j,i), new Position(5,5));
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
				State keyQ = new State(new Position(j,i), new Position(5,5), action);
				System.out.printf("%.3f\t",Q.get(keyQ));
			}
			System.out.println();	
		}
		
		double stdDev = Testsimulation.getStdDev(runsEachEpisode);
		System.out.println("The standard deviation is: "+stdDev);
		
	    try {
		    on.output();
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	
	public void show(String s){
		System.out.println(s);
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
		File policyfile = new File("convergenceOnMC.data");
		policyfile.delete();
		policyfile.createNewFile();
		if(!runsEachEpisode.isEmpty()) {
			for(int times : runsEachEpisode){
		    //Enumeration enu = stateactions.keys();
		    //while(enu.hasMoreElements()) {
			//String state = (String) enu.nextElement();
			//String action = (String) stateactions.get(state);
				try {
					write(policyfile, String.valueOf(times)+"\n", true);
				} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			    System.out.println("Cannot write to file");
			}
		    }
		} else
		    show("\nRuns each episode is empty!!");
	    }
	    
	    
}
