package src.executeHunting;

import java.awt.EventQueue;

import src.environment.VisualizeGrid;
import src.policy.PredatorPolicies;
import src.policy.PreyPolicies;
import src.statespace.Position;

public class Hunt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final Position predator = new Position(0,0); //create predator and prey and initialize their positions
		final Position prey = new Position(5,5);
		
		
		//final predator.PredatorPolicies predpolicy= new predator.PredatorPolicies();
		final PredatorPolicies predpolicy = new PredatorPolicies();
		final PreyPolicies preypolicy = new PreyPolicies();
		
		//String previousPos  = Integer.toString(predator.getX()) + "," + Integer.toString(prey.getY());
		
		//System.out.println(predpolicy.randomPolicy());
		while(true){
			String PredatorAction =  predpolicy.randomPolicy();
			String PreyAction = preypolicy.dummyPreyPolicy();
			
			System.out.println("pred action " + PredatorAction);
			System.out.println("prey action " + PreyAction);
			
			break;
		}
		/*
		EventQueue.invokeLater(new Runnable() {	//display the environment with the agents
			@Override
        	public void run() {
				new VisualizeGrid(predpos,preypos).display();
			}
        });
		*/
	}

}
