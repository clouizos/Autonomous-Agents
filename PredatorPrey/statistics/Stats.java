/**
 * stats.java version 1.0 8 Mar 2007
 *
 * Copyright (c) 2007 qrio.
 * All rights reserved
 *
 */
package statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author qrio
 *
 */
public class Stats {

    private File gamelog;
    private int online = 0;
    private int diagonal = 0;
    private int other = 0;
    private int ocycles = 0;
    private int dcycles = 0;
    private int othercyc = 0;
    
    /**
     * 
     */
    public Stats(File file) {
	// TODO Auto-generated constructor stub
	gamelog = file;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	Stats st;
	File log = new File("game.log");
	if(log.canRead()) {
	    st = new Stats(log);
	    st.trace();
	    System.out.println("Statistics: ");
	    System.out.println("online: "+st.getOnline());
	    System.out.println("online cycles: "+st.getOcycles());
	    System.out.println("online average capture time: "+
		    (double)st.getOcycles()/(double)st.getOnline());
	    System.out.println("diagonal: "+st.getDiagonal());
	    System.out.println("diagonal cycles: "+st.getDcycles());
	    System.out.println("diagonal average capture time: "+
		    (double)st.getDcycles()/(double)st.getDiagonal());
	    System.out.println("other: "+st.getOther());
	    System.out.println("other cycles: "+st.getOthercyc());
	    System.out.println("other average capture time: "+
		    (double)st.getOthercyc()/(double)st.getOther());
	} else
	    System.out.println("No game.log file!");
	
    }
    
    public void trace() {
	boolean secondprey = false, secondpred = false;
	String s1, s2 = "";
	int x1=0, y1=0, x2=0, y2=0, px1=0, py1=0, px2=0, py2=0;
	int lastepisode = 1, currentepisode = 1;
	int cycles = 0;
	try {
	    FileReader readFile = new FileReader(gamelog);
	    BufferedReader readBuf = new BufferedReader(readFile);

	    while((s1 = readBuf.readLine())!=null) {
		if(s1.startsWith("(world (stats")) {
		    //System.out.println(s1);
		    StringTokenizer tok = new StringTokenizer(s1.substring(7),") (");
		    while (tok.hasMoreTokens()) {
			s2 = tok.nextToken(); // token
			if(s2.compareTo("stats")==0) {
			    currentepisode = Integer.parseInt(tok.nextToken());
			    //System.out.println("stats: episode:"+currentepisode+" cycles:"+cycles);
			    if(currentepisode!=lastepisode)
				updateStats(x1, y1, x2, y2, px1, py1, px2, py2, cycles);
			    cycles = Integer.parseInt(tok.nextToken());
			    lastepisode = currentepisode;
			}
			if(s2.compareTo("prey")==0) {
			    if(secondprey==false) {
				// first prey block
				tok.nextToken();
				x1 = Integer.parseInt(tok.nextToken());
				y1 = Integer.parseInt(tok.nextToken());
				//System.out.println("prey1: x:"+x1+" y:"+y1);
				secondprey = true;
			    } else {
				// second prey block
				tok.nextToken();
				x2 = Integer.parseInt(tok.nextToken());
				y2 = Integer.parseInt(tok.nextToken());
				//System.out.println("prey2: x:"+x2+" y:"+y2);
				secondprey = false;
			    }
			}
			if(s2.compareTo("predator")==0) {
			    if(secondpred==false) {
				// first predator block
				tok.nextToken();
				px1 = Integer.parseInt(tok.nextToken());
				py1 = Integer.parseInt(tok.nextToken());
				//System.out.println("pred1: x:"+px1+" y:"+py1);
				secondpred = true;
			    } else {
				// second predator block
				tok.nextToken();
				px2 = Integer.parseInt(tok.nextToken());
				py2 = Integer.parseInt(tok.nextToken());
				//System.out.println("pred2: x:"+px2+" y:"+py2);
				secondpred = false;
			    }
			}
		    }
		}
		}
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    // state: [x1, y1, x2, y2, px1, py1, px2, py2]
    // of prey1, prey2, pred1, pred2
    public void updateStats(int xprey1, int yprey1, int xprey2, int yprey2,
	    int xpred1, int ypred1, int xpred2, int ypred2, int cyc) {
	int x1, y1, x2, y2, px1, py1, px2, py2;
	px1 = xpred1;
	py1 = xpred2;
	// transform to relative coordinates to pred1 in respect to the wrap around
	x1 = wrap(xprey1 - px1);
	y1 = wrap(yprey1 - py1);
	x2 = wrap(xprey2 - px1);
	y2 = wrap(yprey2 - py1);
	px2 = wrap(xpred2 - px1);
	py2 = wrap(ypred2 - py1);
	px1 = 0;
	py1 = 0;
	int[] pred1 = {px1, py1};
	int[] pred2 = {px2, py2};
	if(online(pred1, pred2)) {
	    online++;
	    ocycles+=cyc;
	    return;
	}
	if(diagonal(pred2)) {
	    diagonal++;
	    dcycles+=cyc;
	    return;
	}
	else {
	    other++;
	    othercyc+=cyc;
	    return;
	}
    }

    public int wrap(int i) {
	if((i > 7)||(i < -7))
	   	 return i -= 15;
	return i;
    }
    
    public boolean online(int[] pred1, int[] pred2) {
	if((pred1[0]==pred2[0])||(pred1[1]==pred2[1]))
	    return true;
	return false;
    }
    
    public boolean diagonal(int[] pred2) {
	int[] a = { -1, -1 };
	int[] b = { 1, 1 };
	int[] c = { 1, -1 };
	int[] d = { -1, 1 };
	Object[] diagstates = {a,b,c,d};
	for(int i=0;i<diagstates.length;i++) {
	    if(Arrays.equals((int[])diagstates[i], pred2))
		return true;
	}
	return false;
    }

    public double getDcycles() {
        return dcycles;
    }

    public void setDcycles(int dcycles) {
        this.dcycles = dcycles;
    }

    public int getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(int diagonal) {
        this.diagonal = diagonal;
    }

    public File getGamelog() {
        return gamelog;
    }

    public void setGamelog(File gamelog) {
        this.gamelog = gamelog;
    }

    public double getOcycles() {
        return ocycles;
    }

    public void setOcycles(int ocycles) {
        this.ocycles = ocycles;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public double getOthercyc() {
        return othercyc;
    }

    public void setOthercyc(int othercyc) {
        this.othercyc = othercyc;
    }
}
