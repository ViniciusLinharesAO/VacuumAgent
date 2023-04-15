package ai.search;


/**
 * A State and Action.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public class StateActionPair
{
	Object action;
	Object state;

	public StateActionPair(Object a, Object s)
	{
		action = a;
		state = s;
	}
}