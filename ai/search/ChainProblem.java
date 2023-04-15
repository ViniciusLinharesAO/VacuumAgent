// The Chain Problem
package ai.search;
import java.util.Vector;
import java.awt.*;
import javax.swing.*;

public class ChainProblem extends Problem 
{
	
	ChainProblem()
	{
		super(null);
		initialState = initialChainState();
	}
	

	public boolean goalReached(Object state)
	{
		ChainState cs = (ChainState)state;
		if ((cs.openLinks==0) && (cs.chains.size()==1)) {
		    Chain c = (Chain)cs.chains.elementAt(0);
		    return (c.numLinks==12 && c.loop);
		}
		return false;
	}


// Return a list of StateActionPairs. The action can be either 
// "close straight" or "close loop" followed by the chain number, or
// "open" followed by the chain number and the link number or 
// "join" followed by the two chain numbers.

	public Vector successors(Object state)
	{
		Vector succ = new Vector();

		// Complete the code here

		return succ;
	}
	
	
	public boolean equalState(Object state1, Object state2)
	{
	    ChainState cs1 = (ChainState)state1;
	    ChainState cs2 = (ChainState)state2;
	    if (cs1.openLinks==cs2.openLinks && 
		cs1.chains.size()==cs2.chains.size()) {
		    for (int i=0; i<cs1.chains.size(); i++) {
			Chain c1 = (Chain)cs1.chains.elementAt(i);
			Chain c2 = (Chain)cs2.chains.elementAt(i);
			if (c1.numLinks!=c2.numLinks || 
			    c1.loop!=c2.loop) return false;
		    }
		    return true;
		}
	    return false;
	}
	
	public String stateToString(Object state)
	{
	    ChainState cs = (ChainState)state;
	    String str = new String();
	    str = "(" + cs.openLinks;
	    for (int i=0; i<cs.chains.size(); i++) {
		Chain c = (Chain)cs.chains.elementAt(i);
		str = str + " (" + c.numLinks;
		if (c.loop)
		    str = str + " loop)";
		else
		    str = str + " straight)";
	    }
	    str = str + ")";
	    return str;
	}
	
	public String actionToString(Object action)
	{
	    return (String)action;
	}

	
	void currentDisplay(Graphics g)
	{
	}
	
	void drawCanvas(Graphics g)
	{
	}
	
	JPanel createControlPanel(ProblemPanel probPanel)
	{
	    JPanel cp = super.createControlPanel(probPanel);
	    probPanel.maxExpanded.setText("1000");
	   // probPanel.paintImmediately(probPanel.getVisibleRect());
	    canvas.setVisible(false);
	    probPanel.queuePanel.setVisible(false);
	    return cp;
	}
	
	void displaySolution(Graphics g)
	{
	    System.out.println("Solution : ");
	    Vector solution = searchResult.solutionNodes();
	    for (int i=0; i<solution.size(); i++){
		Node n = (Node)solution.elementAt(i);
		System.out.println(actionToString(n.action) + " " +
		    stateToString(n.state));
	    }
	}
	
	void updateProbPanel()
	{
	    if (display) {
		probPanel.numExpanded.setText("   " + numExpanded);
		probPanel.paintImmediately(probPanel.getVisibleRect());
	    }
	}
	
	// Return the state resulting from opening a given link in 
	// the specified chain
	ChainState open(ChainState cs, int chainNum, int linkNum)
	{
	    // Create a new ChainState without that chain
	    ChainState newcs = new ChainState();
	    newcs.openLinks = cs.openLinks + 1;
	    for (int i=0; i<cs.chains.size(); i++)
		if (i!=chainNum)
		    newcs.chains.addElement(cs.chains.elementAt(i));
	    
	    Chain c1 = (Chain)cs.chains.elementAt(chainNum);
	    
	    // If opening a link at end or opening a loop then result
	    // is removing one link of straight chain
	    if (c1.numLinks > 1)
		if (linkNum==0 || linkNum==c1.numLinks-1 || c1.loop)
		    insertChain(newcs, new Chain(c1.numLinks-1,false));
		else { // result is two chains
		    insertChain(newcs, new Chain(linkNum,false));
		    insertChain(newcs, new Chain(c1.numLinks-linkNum-1,false));
		}
	    return newcs;
	}
	
	// Return the state resulting from closing a link on 
	// the specified chain
	ChainState close(ChainState cs, int chainNum, boolean loop)
	{
	    // Create a new ChainState without that chain
	    ChainState newcs = new ChainState();
	    newcs.openLinks = cs.openLinks - 1;
	    for (int i=0; i<cs.chains.size(); i++)
		if (i!=chainNum)
		    newcs.chains.addElement(cs.chains.elementAt(i));
	    
	    Chain c1 = (Chain)cs.chains.elementAt(chainNum);
	    
	    // Create a new chain
	    insertChain(newcs, new Chain(c1.numLinks+1,loop));
	    return newcs;
	}
	
	// Return the state resulting from joining two chains
	ChainState join(ChainState cs, int chainNum1, int chainNum2)
	{
	    // Create a new ChainState without those chains
	    ChainState newcs = new ChainState();
	    newcs.openLinks = cs.openLinks - 1;
	    for (int i=0; i<cs.chains.size(); i++)
		if (i!=chainNum1 && i!=chainNum2)
		    newcs.chains.addElement(cs.chains.elementAt(i));
	    
	    Chain c1 = (Chain)cs.chains.elementAt(chainNum1);
	    Chain c2 = (Chain)cs.chains.elementAt(chainNum2);
	    
	    // Create a new chain
	    insertChain(newcs, new Chain(c1.numLinks+c2.numLinks+1,false));
	    return newcs;
	}
	
	void insertChain(ChainState cs, Chain c)
	{
	    for (int i=0; i<cs.chains.size(); i++) {
		Chain c1 = (Chain)cs.chains.elementAt(i);
		if (c.numLinks > c1.numLinks){
		    cs.chains.insertElementAt(c,i);
		    return;
		}
	    }
	    cs.chains.insertElementAt(c,cs.chains.size());
	}
	    
	
	ChainState initialChainState()
	{
	    ChainState cs = new ChainState();
	    cs.chains.addElement(new Chain(3,false));
	    cs.chains.addElement(new Chain(3,false));
	    cs.chains.addElement(new Chain(3,false));
	    cs.chains.addElement(new Chain(3,false));
	    return cs;	
	}   
}


// The state says how many open links there are and gives a list of chains

class ChainState
{
	int openLinks;
	Vector chains;

	ChainState()
	{
		openLinks = 0;
		chains = new Vector();
	}
	
}

// A chain consists of a number of links plus a boolean stating whether linked
// into a loop or straight.
class Chain
{
	int numLinks;
	boolean loop;
	
	Chain(int n, boolean lp)
	{
	    numLinks = n;
	    loop = lp;
	}
}

