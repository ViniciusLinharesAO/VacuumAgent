package ai.worlds.vacuum;

import ai.worlds.*;


/**
 * A generic vacuum agent. 
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public abstract class VacuumAgent extends Agent
{  
	
	public  String lastAction = null;
	
	/**
	 * shut off the vacuum.
	 */
    public void shutOff( )
    {
    	body.alive=false;
    }
    
	/**
	 * suck dirt.
	 * @param vw is the vacuum world environment.
	 */
    void suck(VacuumWorld vw)
    {
    	vw.grab(body);
    }
    
	/**
	 * take the next action.
	 * @param e is the environment.
	 */
    public void takeAction(Environment e)
    {
	VacuumWorld vw = (VacuumWorld) e;
	if (action.equals("suck")) suck(vw);
	else if (action.equals("forward")) vw.forward(body);
	else if (action.equals("turn right")) vw.turn(body,"right");
	else if (action.equals("turn left")) vw.turn(body,"left");
	else if (action.equals("shut-off")) shutOff();
    }    
}