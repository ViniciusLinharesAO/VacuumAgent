package ai.worlds.wumpus;


import java.util.Vector;
import java.io.*;
import java.util.*;
import ai.worlds.*;
import javax.swing.*;

/**
 * The wumpus world environment.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class WumpusWorld extends GridEnvironment
{
	/**
	 * The single wumpus in the world
	 */
    public Wumpus w;
    
    public WumpusWorld(Agent[] a, int xsize, int ysize, JFrame f) {
    	super(a,xsize,ysize, f);
    	fillGrid(.1, (new Pit()).getClass());
    	// if pit in start location then remove it
    	Vector v = (Vector) grid[1][1];
    	if (contains(v, (new Pit()).getClass())) v.removeElementAt(0);
    	// put in 1 wumpus
    	int x = (int)Math.floor(Math.random()*(size.x-2))+1;
    	int y = (int)Math.floor(Math.random()*(size.y-2))+1;
    	if (x==1 && y==1) y++;
    	addObj(new Location(x,y),w = new Wumpus());
    	// put in 1 pot of gold
    	x = (int)Math.floor(Math.random()*(size.x-2))+1;
    	y = (int)Math.floor(Math.random()*(size.y-2))+1;
    	addObj(new Location(x,y),new Gold());
    }
    public WumpusWorld(Agent[] a, int xsize, int ysize, JFrame f, String filename) {
    	super(a,xsize,ysize, f);
    	boolean wumpus = false, gold = false;
    	BufferedReader in;
    	try {
    		in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
    		for(int y = 4; y > 0; y--) {
    			String s = in.readLine();
        		StringTokenizer st = new StringTokenizer(s);
    			for(int x = 1; x <= 4; x++) {
    				String value = st.nextToken();
    				if(x != 1 || y != 1) {
    					if(value.equals("P")) {
    						fillLoc(new Location(x,y), (new Pit()).getClass());
    					}
    					else if(value.equals("W") && !wumpus) {
    						addObj(new Location(x,y),w = new Wumpus());
    						wumpus = true;
    					}
    					else if(value.equals("G") && !gold) {
    						addObj(new Location(x,y), new Gold());
    						gold = true;
    					}
    					else if(value.equals("X")) {
    						fillLoc(new Location(x,y), (new Pit()).getClass());
    						if(!wumpus) {
    							addObj(new Location(x,y), w = new Wumpus());
    							wumpus = true;
    						}
    					}
    					else if(value.equals("Y")) {
    						fillLoc(new Location(x,y), (new Pit()).getClass());
    						if(!gold) {
    							addObj(new Location(x,y), new Gold());
    							gold = true;
    						}
    					}
    					else if(value.equals("Z")) {
    						if(!wumpus) {
    							addObj(new Location(x,y), w = new Wumpus());
    							wumpus = true;
    						}
    						if(!gold) {
    							addObj(new Location(x,y), new Gold());
    							gold = true;
    						}
    					}
    					else if(value.equals("A")) {
    						fillLoc(new Location(x,y), (new Pit()).getClass());
    						if(!wumpus) {
    							addObj(new Location(x,y), w = new Wumpus());
    							wumpus = true;
    						}
    						if(!gold) {
    							addObj(new Location(x,y), new Gold());
    							gold = true;
    						}
    					}
    				}
    			}
    		}
    	} catch(Exception exception) {System.err.println(exception);}
    	
    	
    }
    /**
     * Determine if an action is legal.
     * @param a is the action string
     */
    public boolean legalAction(String a)
    {
	return (a=="climb") || (a=="shoot") || (a=="grab") || (a=="forward") 
		|| (a=="turn left") || (a=="turn right");
    }
    
    public void updateEnv()
    {
	super.updateEnv();
	// See if anyone died
	for (int i=0; i<agents.length; i++){
	    AgentBody body = agents[i].body;
	    Location loc = body.loc;
	    Location wloc = w.loc;
	    if (contains((Vector)grid[loc.x][loc.y],(new Pit()).getClass()) ||
		(wloc.x==loc.x && wloc.y==loc.y && w.alive )) {
		body.alive = false;
	    }
	} 
    }
    
    /**
     * End when some agent climbs out or for the default reason (everyone dead).
     */
    public boolean termination()
    {
	boolean climb = false;
	for (int i=0; i<agents.length; i++){
	    Location loc = agents[i].body.loc;
	    if (agents[i].action=="climb" && loc.x==start.x && loc.y==start.y) climb = true;
	}
	return (super.termination() || climb);
    }
    
   public int performanceMeasure(Agent a)
   {
	AgentBody body = a.body;
	int score = -step;
	if (contains(body.container,(new Gold()).getClass()))
	    score += 1000;
	if (! body.alive) score -= 10000;
	return score;
    }
   
   /**
    * Get the agent percept.
    * @param a is the agent.
    * @return a vector indicating [stench, breeze, glitter, bump, sound]
    */
    public Object getPercept(Agent a)
    {
	Location loc = a.body.loc;
	Vector v = new Vector(5);
	if (neighbor(loc,(new Wumpus()).getClass())) 
	    v.addElement("stench");
	else 
	    v.addElement("");
	if (neighbor(loc,(new Pit()).getClass())) 
	    v.addElement("breeze");
	else 
	    v.addElement("");
	if (contains((Vector)grid[loc.x][loc.y],(new Gold()).getClass())) 
	    v.addElement("glitter");
	else 
	    v.addElement("");
	if (a.body.bump) 
	    v.addElement("bump");
	else 
	    v.addElement("");
	if (anySound())
	    v.addElement("sound");
	else
	    v.addElement("");
	return v;
    }
    
    /**
     * Determine if a sound is heard.
     */
    private boolean anySound()
    {
	boolean sound = false;
	for (int i=0; i<agents.length; i++)
	    if (agents[i].body.sound) {
		sound = true;
		agents[i].body.sound = false; // sound dissipates
	    } 
	if (w.sound) {
	    sound = true; w.sound = false; // sound dissipates
	}
	return sound;
    }  	
}