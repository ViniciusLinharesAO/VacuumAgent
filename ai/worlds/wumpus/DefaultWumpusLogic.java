package ai.worlds.wumpus;

import java.util.Vector;

import ai.logic.HornKnowledgeBase;
import ai.logic.Logic;
import ai.worlds.Location;

/**
 * A generic wumpus logic unit. 
 * This code must be completed to perform logical reasoning.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class DefaultWumpusLogic extends WumpusLogic {
	
	public DefaultWumpusLogic(int x, int y) {
		super(x, y);
	}

	/**
	 * Make a knowledge base.
	 * @param kb is the knowledge base that will be constructed.
	 */
	public void makeKB(HornKnowledgeBase kb)
	    // construct the wumpus knowledge base (this kb is incomplete)
	    {
		for (int x=1; x<=size.x; x++)
		    for (int y=1; y<=size.y; y++) {
		      try{
			if (x>1) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + (x-1) + "," +
						     y + ")"));
			if (x<size.x) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + (x+1) + "," +
						     y + ")"));
			if (y>1) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + x + "," +
						     (y-1) + ")"));
			if (y<size.y) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + x + "," +
						     (y+1) + ")")); 
		      }
		      catch(Exception e) {};
		    }
		try{  
		kb.tell(Logic.parse("wOK(1,1)"));
		kb.tell(Logic.parse("pOK(1,1)"));  
		kb.tell(Logic.parse("$x1=$x2 & $y1=$y2 -> same($x1,$y1,$x2,$y2)"));
		kb.tell(Logic.parse("neighbor($x1,$y1,$x3,$y3) & " + 
				    "~same($x2,$y2,$x3,$y3) -> " +
				    "otherneighbor($x1,$y1,$x2,$y2,$x3,$y3)"));
		kb.tell(Logic.parse("otherneighbor($x1,$y1,$x2,$y2,$x3,$y3) & w?($x3,$y3) -> " +
				    "neighborwithw($x1,$y1,$x2,$y2)"));
		kb.tell(Logic.parse("~neighborwithw($x1,$y1,$x2,$y2) ->" +
				    "allothersWfree($x1,$y1,$x2,$y2)"));
		kb.tell(Logic.parse("otherneighbor($x1,$y1,$x2,$y2,$x3,$y3) & p?($x3,$y3) ->" +
				   "neighborwithp($x1,$y1,$x2,$y2)"));		    
		kb.tell(Logic.parse("~neighborwithp($x1,$y1,$x2,$y2) ->" +
				    "allothersPfree($x1,$y1,$x2,$y2)"));
		}
		catch(Exception e) {System.out.println("Error in building kb");};
	 }

	/**
	 * Find the closest move from a list.
	 * @param moves is a list of locations to which the agent can move.
	 * @return the move that is closest to the agent's current location.
	 */
	public Location closestMove(Vector moves) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Determine the next location the agent will visit.
	 * @return the location that the agent will move to next.
	 */
	public Location nextMove() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Determine the sequence of actions which will take the agent to the given location.
	 * @param loc is the location we wish to travel to.
	 * @param heading is the agent's current heading.
	 * @return the sequence of actions which will take the agent to the given location.
	 */
	public Vector pathTo(Location loc, Location heading) {
		// TODO Auto-generated method stub
		return null;
	}

}
