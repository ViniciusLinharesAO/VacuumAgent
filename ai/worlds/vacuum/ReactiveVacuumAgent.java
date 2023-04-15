package ai.worlds.vacuum;

import java.util.Vector;
/**
 * A vacuum agent which reacts to its percepts.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class ReactiveVacuumAgent extends VacuumAgent
{
	/**
	 * Determine the next action to be performed.
	 */
    public void determineAction()
    {
	Vector p = (Vector) percept;
	if (p.elementAt(1) == "dirt") action = "suck";
	else if (p.elementAt(2) == "home") {
	    int i = (int)Math.floor(Math.random()*3);
	    switch (i) {
		case 0: action = "shut-off"; break;
		case 1: action = "forward"; break;
		case 2: action = "turn left";
	    }
	}
	else if (p.elementAt(0) == "bump") {
	    int i = (int)Math.floor(Math.random()*2);
	    switch (i) {
		case 0: action = "turn right"; break;
		case 1: action = "turn left";
	    }	
	}
	else {
	    int i = (int)Math.floor(Math.random()*5);
	    switch (i) {
		case 0: action = "forward"; break;
		case 1: action = "forward"; break;
		case 2: action = "forward"; break;
		case 3: action = "turn right"; break;
		case 4: action = "turn left"; 
	    }
	}
    }
}