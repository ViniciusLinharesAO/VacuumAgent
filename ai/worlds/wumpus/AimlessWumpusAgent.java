package ai.worlds.wumpus;

import java.util.Vector;

/**
 * A Wumpus agent which moves aimlessly. 
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class AimlessWumpusAgent extends WumpusAgent
{
	/**
	 * A list of planned moves
	 */
    Vector plan = new Vector();
	/**
	 * A flag indicating if the wumpus is still alive
	 */
    boolean wumpusAlive = true;
    
    public AimlessWumpusAgent() {
    	super();
    }
    
	/**
	 * Determines the next action to be taken by the agent.
	 * The action is stored as a string in the field 'action'.
	 */
    public void determineAction()
    {
	Vector p = (Vector) percept;
	if (p.elementAt(4) =="sound") wumpusAlive=false;
	if (p.elementAt(2) =="glitter") action = "grab";
	else if (p.elementAt(3) =="bump") {
	    plan.removeAllElements();
	    plan.addElement("turn right");
	    plan.addElement("forward");
	    action = "turn right";
	}
	else if (! plan.isEmpty()) {
	    action = (String)plan.elementAt(0);
	    plan.removeElementAt(0);
	}
	else if (p.elementAt(1)=="breeze" || 
		(wumpusAlive && p.elementAt(0)=="stench")) {
	    plan.removeAllElements();
	    plan.addElement("forward");
	    int i = (int)Math.floor(Math.random()*2);
	    switch (i) {
		case 0: action = "turn left"; break;
		case 1: action = "turn right";
	    }
	}
	else {
	    int i = (int)Math.floor(Math.random()*6);
	    switch (i) {
		case 0: action = "climb"; break;
		case 1: action = "forward"; break;
		case 2: action = "forward"; break;
		case 3: action = "turn right"; break;
		case 4: action = "turn left"; break;
		case 5: action = "shoot";
	    }
	}
    }
}