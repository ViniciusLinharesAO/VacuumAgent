package ai.worlds.wumpus;

/**
 * A wumpus agent that takes actions randomly.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class RandomWumpusAgent extends WumpusAgent
{
	
	public RandomWumpusAgent() {
    	super();
    }
	
	/**
	 * Determines the next action to be taken by the agent.
	 * The action is stored as a string in the field 'action'.
	 */
    public void determineAction()
    {
	int i = (int)Math.floor(Math.random()*6);
	switch (i) {
	    case 0: action = "forward"; break;
	    case 1: action = "turn right"; break;
	    case 2: action = "turn left"; break;
	    case 3: action = "grab"; break;
	    case 4: action = "shoot"; break;
	    case 5: action = "climb";
	}
    }
}