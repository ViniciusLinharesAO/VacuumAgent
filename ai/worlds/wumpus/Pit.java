
package ai.worlds.wumpus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import ai.worlds.Obj;
import ai.worlds.Location;
/**
 * A pit that appears in a wumpus world.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public class Pit extends Obj
{
	public Pit() {
		super();
	}
	public Pit(Location location) {
		super();
		loc = location;
	}
    public void draw(Graphics g, Point p, int cellSize)
    {
	g.setColor(Color.black);
	g.fillOval(p.x,p.y,cellSize,cellSize);
    }
}
