package environment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** @see http://stackoverflow.com/questions/4331699 */

	public class VisualizeGrid extends JPanel {
	    private static final int N = 12;
	    private static final int SIZE = 100;
	    
	    //public void DrawEnv(){
	    public VisualizeGrid(PredatorPosition predpos, PreyPosition preypos) {
	        super(new GridLayout(N, N));
	        this.setPreferredSize(new Dimension(N * (SIZE), N * (SIZE-50)));
	        for (int i = 0; i < N * N; i++) {
	            this.add(new ChessButton(i,predpos,preypos));
	        }
	    }

	    private static class ChessButton extends JButton {

	        public ChessButton(int i, PredatorPosition predpos, PreyPosition preypos) {
	            //super(i / N + "," + i % N);
	            if(i/N == predpos.getXpos()+1 &&  i%N == predpos.getYpos()+1){
	                this.setText("predator");
	            }
	            else if(i/N == preypos.getXpos()+1 && i%N == preypos.getYpos()+1){
	                this.setText("prey");
	            }
	            else{
	                int xcoord = i/N;
	                int ycoord = i%N;
	                if(xcoord == 0){
	                	if (ycoord > 0){
	                		ycoord = ycoord - 1;	//adjust cause of the extra row and column
	                		this.setText(Integer.toString(ycoord));
	                	}
	                }else if (ycoord == 0){
	                	if (xcoord >0){
	                		xcoord = xcoord -1;
	                		this.setText(Integer.toString(xcoord));
	                	}
	                }
	                
	            }
	            //this.setOpaque(true);
	            //this.setBorderPainted(true);
	            this.setBackground(Color.white);
	            /*if ((i / N + i % N) % 2 == 1) {
	                this.setBackground(Color.gray);
	            }*/
	        }
	    }
	     public void display() {
	        JFrame f = new JFrame("Environment");
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.add(this);
	        f.pack();
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);
	    }

	}
