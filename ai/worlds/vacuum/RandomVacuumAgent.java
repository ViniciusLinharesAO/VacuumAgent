package ai.worlds.vacuum;
/**
 * A vacuum agent that simply chooses actions at random
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public class RandomVacuumAgent extends VacuumAgent
{   
	/**
	 * Determine the next action to be performed.
	 */
    public void determineAction()
    {
	int i = (int)Math.floor(Math.random()*5);
	switch (i) {
	    case 0: action = "forward"; break;
	    case 1: action = "turn right"; break;
	    case 2: action = "turn left"; break;
	    case 3: action = "shut-off"; break;
	    case 4: action = "suck";
	}
    }
}