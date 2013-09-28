package policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
	private ArbitraryPolicy Pi;
	private ArbitraryPolicy PiPrime;
	/**
	 * @param args
	 */
	
	public OffPolicyMC(ArbitraryPolicy PiPrime, Policy preyPolicy){
		this.PiPrime = PiPrime;
		this.preyPolicy = preyPolicy;
		initialize();
	}
	public void initialize(){
		String[] Actions = {"north","south","east","west","wait"};
		Q = new HashMap<String, Double>();
		N = new HashMap<String, Double>();
		D = new HashMap<String, Double>();
		for (int i=0;i<11;i++)
			for (int j=0;j<11;j++)
				for (int k=0;k<Actions.length;k++){
					String key = "["+i+"]["+j+"][5][5]-"+Actions[k];
					Q.put(key,0.0);
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
	public ArrayList<DataEpisode> generateEpisode(){
		ArrayList<DataEpisode> episodeData = new ArrayList<DataEpisode>();
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
//		State nextState;
		currentState = new State(pred,prey);
		int counter=0;
		do {
//			System.out.println(++counter);
//			System.out.println(currentState.toString());
			predAction = PiPrime.getAction(currentState);
			episodeData.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey()),r,predAction));
			currentState.setPredator(currentState.getPredator().move(predAction));
			preyAction = preyPolicy.getAction(currentState);
			currentState.setPrey(currentState.getPrey().move(preyAction));
//			System.out.println(currentState.toString());
			Position predproj = currentState.getPredator().transformPrey55(currentState.getPrey());
			currentState.updatePosition(predproj, preyDefault);
			r = getReward(currentState);
			//Save the last State
			if (currentState.endState())
				episodeData.add(new DataEpisode(new State(currentState.getPredator(), currentState.getPrey()),r,null));
		}while (! currentState.endState() && counter<500);
		return episodeData;
	}
	
	public void doControl(){
		
		do{
			
		}while (true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<DataEpisode> data = new ArrayList<DataEpisode> ();
		do{
			if (data.size()!=0)
				data.clear();
			ArbitraryPolicy PiPrime = new ArbitraryPolicy();
			Policy preyPolicy = new RandomPolicyPrey();
			OffPolicyMC off = new OffPolicyMC(PiPrime, preyPolicy);
			data =  off.generateEpisode();			
		} while (data.size()>499);
		
		for (DataEpisode d : data){
			System.out.println(d.toString());
		}
		System.out.println(data.size());

	}

}
