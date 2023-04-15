package ai.worlds.wumpus;


import java.awt.*;
import ai.worlds.*;
/**
 * A wumpus that appears in the wumpus world.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class Wumpus extends Obj
{
    public Wumpus()
    {
    	alive = true;
    }
    public Wumpus(Location location) {
    	alive = true;
    	loc = location;
    }
    public void draw(Graphics g, Point p, int cellSize)
    {
	g.setColor(Color.red);
	g.fillOval(p.x+cellSize/8,p.y+cellSize/8,3*cellSize/4,3*cellSize/4);
	if (alive){
	    g.setColor(Color.black);
	    g.drawString("W",p.x+cellSize/2-6,p.y+cellSize/2-2);
	}
    }
}