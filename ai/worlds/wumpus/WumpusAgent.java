package ai.worlds.wumpus;

import java.awt.*;
import java.util.Vector;
import ai.worlds.*;

/**
 * A generic wumpus agent. 
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public abstract class WumpusAgent extends Agent
{
	/**
	 * The logic unit of the agent.
	 */
	public WumpusLogic logic;
    public WumpusAgent()
    {
    	body.container.addElement(new Arrow());
    	logic = new DefaultWumpusLogic(4,4);
    }
    
	/**
	 * Climb out of the cave.
	 * @param ww is the wumpus world environment
	 */
    public void climb(WumpusWorld ww)
    {
	if (body.loc.x == 1 && body.loc.y==1) 
	    ww.removeObj(new Location(1,1), body);
    }
    
	/**
	 * Shoor the arrow.
	 * @param ww is the wumpus world environment
	 */
    public void shoot(WumpusWorld ww)
    {
	if (ww.contains(body.container,(new Arrow()).getClass())){
	    Arrow a = (Arrow)ww.getItem(body.container,
					(new Arrow()).getClass());
	    a.heading = body.heading;
	    body.container.removeElement(a);
	    Location oldloc = body.loc;
	    Location newloc = body.loc.forward(body.heading);
	    ww.addObj(newloc,a);
	    while ( !(ww.contains((Vector)ww.grid[newloc.x][newloc.y],
				  (new Wall()).getClass())) &&
		    !(ww.contains((Vector)ww.grid[newloc.x][newloc.y],
				  (new Wumpus()).getClass())) ) {
		    if(ww.display){
			ww.canvas.updateHere(oldloc,newloc,ww.canvas.getGraphics());
			try { Thread.sleep(250); }
			catch(Exception e) {}
		    }
		    oldloc = newloc;
		    newloc = newloc.forward(body.heading);
		    ww.removeObj(oldloc,a);
		    ww.addObj(newloc,a);
	    }
	    if (ww.contains((Vector)ww.grid[newloc.x][newloc.y],
			    (new Wumpus()).getClass()) &&
		ww.w.alive){
		if (ww.display) 
		    ww.canvas.updateHere(oldloc,newloc,ww.canvas.getGraphics());
		ww.w.sound=true;
		ww.w.alive=false;
	    }
	}
    } 
	
	/**
	 * Perfom the current action.
	 * @param e is the environment the agent is within
	 */
    public void takeAction(Environment e)
    {
	WumpusWorld ww = (WumpusWorld) e;
	if (action=="shoot") shoot(ww);
	else if (action=="grab") ww.grab(body);
	else if (action=="forward") ww.forward(body);
	else if (action=="turn left") ww.turn(body,"left");
	else if (action=="turn right") ww.turn(body,"right");
	else if (action=="climb") climb(ww);
    }
} 

class Arrow extends Obj
{
    public void draw(Graphics g, Point p, int cellSize)
    {
	g.setColor(Color.black);
	
	Point cp;
	cp = new Point(p.x+cellSize/2,p.y+cellSize/2);
	
	int x1 = cp.x + (-heading.x+1-Math.abs(heading.x))*cellSize/16;
	int y1 = cp.y + (heading.y+1-Math.abs(heading.y))*cellSize/16;
	int x2 = cp.x + (heading.x)*cellSize/4;
	int y2 = cp.y + (-heading.y)*cellSize/4;
	int x3 = cp.x + (-heading.x-1+Math.abs(heading.x))*cellSize/16; 
	int y3 = cp.y + (heading.y-1+Math.abs(heading.y))*cellSize/16;
	int x4 = cp.x - (heading.x)*cellSize/4;
	int y4 = cp.y - (-heading.y)*cellSize/4;
	g.drawLine(x2,y2,x4,y4);
	g.drawLine(x1,y1,x2,y2);
	g.drawLine(x2,y2,x3,y3);
	
    }
}