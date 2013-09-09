/**
 * Convert.java version 1.0 Mar 10, 2007
 *
 * Copyright (c) 2007 qrio.
 * All rights reserved
 *
 */
package statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 * @author qrio
 *
 */
public class Convert {
    File temp;
    /**
     * 
     */
    public Convert() {
	// TODO Auto-generated constructor stub
	File log = new File("game.log");
	if(log.canRead()) {
	    temp = new File("temp.log");
	    to2digit(log);	    
	} else
	    System.out.println("No game.log file!");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	Convert convert = new Convert();
    }
    
    public void to2digit(File file) {
	String s1, s2;
	try {
	    FileReader readFile = new FileReader(file);
	    BufferedReader readBuf = new BufferedReader(readFile);
	    PrintWriter out
	    = new PrintWriter(new BufferedWriter(new FileWriter(temp)));

	    while((s1 = readBuf.readLine())!=null) {
		if(s1.startsWith("(world (stats")) {
		    StringTokenizer tok = new StringTokenizer(s1.substring(7),") (");
		    while (tok.hasMoreTokens()) {
			s2 = tok.nextToken(); // token
			if(s2.compareTo("stats")==0) {
			    tok.nextToken();
			    tok.nextToken();
			    s2 = tok.nextToken();
			    double twodigit = Double.parseDouble(s2);
			    BigDecimal bd = new BigDecimal(twodigit);
			        BigDecimal bd_round = bd.setScale( 2, BigDecimal.ROUND_HALF_UP );
			    out.write(s1.replaceFirst(s2, String.valueOf(bd_round.doubleValue())));
			}
		    }
		} else
		    out.write(s1+"\n");
		}
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
