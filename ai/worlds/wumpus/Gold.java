
package ai.worlds.wumpus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ai.worlds.Obj;
import ai.worlds.Location;
/**
 * Gold that appears in a wumpus world.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public class Gold extends Obj
{  
	public Gold() {
		super();
	}
	public Gold(Location location) {
		super();
		loc = location;
	}
	public void draw(Graphics g, Point p, int cellSize)
    {
	g.setColor(Color.yellow);
	g.fillOval(p.x+1,p.y+1,cellSize/4,cellSize/4);
    }
}
