package ai.search;

/**
 * The Missionaries and Cannibals Domain.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
import java.util.Vector;
import java.awt.*;

public class CannibalProblem extends Problem
{
	Vector drawnNodes;
	CannibalState previousDrawn;
	
	CannibalProblem()
	{
		super(new CannibalState(3,3,1,0,0,0));
		drawnNodes = new Vector();
		previousDrawn = null;
	}

	public boolean goalReached(Object state)
	{
		CannibalState s = (CannibalState) state;
		return (s.m1==0 && s.c1==0);
	}

	/**
	 * Return a list of StateActionPairs.
	 * @param state is the current state
	 * @return a vector of state-actions.  
	 * An action is a triple of the form delta-m delta-c delta-b) 
	 * where a positive delta means to move from side 1 to side 2; negative is the opposite.
	 * For example, the action (1 0 1) means move one missionary and 1 boat 
	 * from side 1 to side 2.
	 */
	public Vector successors(Object state)
	{
		CannibalAction[] actions = {new CannibalAction(1,0,1), 
					    new CannibalAction(0,1,1),
					    new CannibalAction(2,0,1),
					    new CannibalAction(0,2,1),
					    new CannibalAction(1,1,1), 
					    new CannibalAction(-1,0,-1),
					    new CannibalAction(0,-1,-1),
					    new CannibalAction(-2,0,-1),  
					    new CannibalAction(0,-2,-1), 
					    new CannibalAction(-1,-1,-1)}; 
		Vector succ = new Vector();
		for (int i=0; i<actions.length; i++){
			CannibalState newState = takeTheBoat((CannibalState)state,actions[i]);
			if ( newState != null && ! cannibalsEat(newState))
				succ.addElement(new StateActionPair(actions[i],newState));
		}
		return succ;
	}
	
	
	public boolean equalState(Object state1, Object state2)
	{
	    CannibalState s1 = (CannibalState)state1;
	    CannibalState s2 = (CannibalState)state2;
	    return (s1.m1==s2.m1 && s1.c1==s2.c1  && s1.b1==s2.b1 && s1.m2==s2.m2 && s1.c2==s2.c2 &&
		    s1.b2==s2.b2);
	}
	
	public String stateToString(Object state)
	{
	    String str = new String();
	    CannibalState s = (CannibalState)state;
	    str = "(" + s.m1 + "," + s.c1 + "," + s.b1 + "," + s.m2 + "," + s.c2 + "," +
				s.b2 + ")";
	    return str;
	}
	
	public String actionToString(Object action)
	{
	    String str = new String(" ");
	    if (action != null) {
		CannibalAction a = (CannibalAction)action;
		str = "(" + a.deltaM + "," + a.deltaC + "," + a.deltaB +")";
	    }
	    return str;
	}

	/**
	 * Move a certain number of missionaries, cannibals, and boats (if possible).
	 * @param state is the current state
	 * @param action is the given action.
	 * @return the new state.  
	 */
	CannibalState takeTheBoat(CannibalState state, CannibalAction action)
	{
		CannibalState newState;
		if ( (action.deltaB == 1 && state.b1>0) ||
		     (action.deltaB == -1 && state.b2>0)){
			newState = new CannibalState(state.m1-action.deltaM,
						      state.c1-action.deltaC,
						      state.b1-action.deltaB,
						      state.m2+action.deltaM,
						      state.c2+action.deltaC,
						      state.b2+action.deltaB);
			if (newState.m1>=0 && newState.c1>=0 && newState.m2>=0 &&
			    newState.c2>=0) return newState;
			else
			    return null;
		}
		return null;
	}
     
	/**
	 * Cannibals eat if they outnumber the missionaries on either side.
	 * @param state is the current state  
	 */
	boolean cannibalsEat(CannibalState state)
	{
		return (state.c1 > state.m1 && state.m1 > 0) ||
		       (state.c2 > state.m2 && state.m2 > 0);
	}	
	
	void currentDisplay(Graphics g)
	{
	    int xsize = 500;
	    Point p,q;
	    CannibalState s = (CannibalState)currentNode.state;
	    Node cn = drawn(currentNode);
	    if (cn == null){
		if (currentNode.parent == null) {
		    // root node;
		    p = new Point(15,10);
		    s.location = p;
		    g.setColor(Color.red);
		    g.drawString(stateToString(s),p.x,p.y);
		    previousDrawn = s;
		}
		else {
		    CannibalState sparent = (CannibalState)currentNode.parent.state;
		    q = sparent.location;
		    int numchild = sparent.numchildren;
		    sparent.numchildren++;
		    p = new Point(q.x+(numchild)*150,q.y+30);
		    s.location = p;
		    g.setColor(Color.red);
		    g.drawString(stateToString(s),p.x,p.y);
		    g.setColor(Color.black);
		    g.drawLine(q.x,q.y,p.x,p.y);
		    // draw previous drawn state in black
		    if (previousDrawn!= null)
			g.drawString(stateToString(previousDrawn),previousDrawn.location.x,
					previousDrawn.location.y);
		    previousDrawn = s;
		}
		drawnNodes.addElement(currentNode);
	    }
	    else { // already drawn so copy that info
		CannibalState cns = (CannibalState)cn.state;
		s.location = cns.location;
		s.numchildren = cns.numchildren;
		// draw in red
		g.setColor(Color.red);
		g.drawString(stateToString(cns),cns.location.x,
					cns.location.y);
		// draw previous drawn in black
		if (!equalState(cns,previousDrawn)){
		    g.setColor(Color.black);
		    g.drawString(stateToString(previousDrawn),previousDrawn.location.x,
					previousDrawn.location.y);
		}
		previousDrawn = cns;
	    }
	    
	}
	
	void drawCanvas(Graphics g)
	{
	    g.setColor(Color.white);
	    g.fillRect(0,0,500,500);
	    drawnNodes = new Vector();
	    initialState = new CannibalState(3,3,1,0,0,0);
	    previousDrawn=null;
	}
	
	void displaySolution(Graphics g)
	{
	    Node n = searchResult;
	    while (n.parent != null) {
		Point p1 = ((CannibalState)n.state).location;
		Point p2 = ((CannibalState)n.parent.state).location;
		g.setColor(Color.red);
		g.drawLine(p1.x,p1.y,p2.x,p2.y);
		n = n.parent;
	    }
	}
	
	Node drawn(Node n1)
	{
	    for (int i=0; i<drawnNodes.size(); i++) {
		Node n2 = (Node)drawnNodes.elementAt(i);
		if (equalState(n1.state,n2.state))
		    return n2;
	    }
	    return null;
	}		
}


// The state says how many missionaries, cannibals, and boats are on the first
// and second banks. The list is (m1 c1 b1 m2 c2 b2) where m1 is the number
// of missionaries on the first bank, etc.

class CannibalState
{
	int m1,c1,b1,m2,c2,b2;
	Point location;
	int numchildren;

	CannibalState(int ma, int ca, int ba, int mb, int cb, int bb)
	{
		m1 = ma; c1 = ca; b1 = ba; 
		m2 = mb; c2 = cb; b2 = bb;
		location = null;
		numchildren = 0;
	}
	
}

class CannibalAction
{
	int deltaM, deltaC, deltaB;

	CannibalAction(int dm, int dc, int db)
	{
		deltaM = dm; deltaC = dc; deltaB = db;
	}
}

