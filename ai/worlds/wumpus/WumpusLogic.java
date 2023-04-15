package ai.worlds.wumpus;

// Object which performs logical inference about the wumpus world
import java.awt.*;
import java.util.Vector;
import java.util.Hashtable;
import javax.swing.plaf.metal.MetalLookAndFeel;
import ai.logic.*;
import ai.worlds.*;
import ai.search.*;
/**
 * A wumpus logic unit that performs logical inference about the wumpus world. 
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public abstract class WumpusLogic
{
	/**
	 * The knowledge base.
	 */
    public HornKnowledgeBase kb;
	/**
	 * Information the agent has stored about the world.
	 */
    public Object grid[][];
	/**
	 * The agents location.
	 */
    public Location agentloc;
	/**
	 * The size of the world.
	 */
    public Location size;
	/**
	 * The graphical representation of the world.
	 */
    public WumpusGrid wumpgrid;
    
    public WumpusLogic(int xsize, int ysize)
    {
	size = new Location(xsize,ysize);
	grid = new Object[xsize+1][ysize+1];
	agentloc = new Location(1,1);
	kb = new HornKnowledgeBase();
	for (int i=1; i<=xsize; i++)
	    for (int j=1; j<=ysize; j++){
		Vector v = new Vector();
		v.addElement("");
		v.addElement("");
		v.addElement("");
		grid[i][j] = v;
	    }
	Vector v = (Vector)grid[1][1];
	v.setElementAt("A",0);
	wumpgrid = new WumpusGrid();
	makeKB(kb);
    }
    
	/**
	 * Make a knowledge base.
	 * @param kb is the knowledge base that will be constructed.
	 */
    public abstract void makeKB(HornKnowledgeBase kb);
	/**
	 * Find the closest move from a list.
	 * @param moves is a list of locations to which the agent can move.
	 * @return the move that is closest to the agent's current location.
	 */
    public abstract Location closestMove(Vector moves);
	/**
	 * Determine the next location the agent will visit.
	 * @return the location that the agent will move to next.
	 */
    public abstract Location nextMove();
	/**
	 * Determine the sequence of actions which will take the agent to the given location.
	 * @param loc is the location we wish to travel to.
	 * @param heading is the agent's current heading.
	 * @return the sequence of actions which will take the agent to the given location.
	 */
    public abstract Vector pathTo(Location loc, Location heading);
    
	/**
	 * Analyze the status of each grid cell using the knowledge base.
	 */
   public void getStatus()
    {
	try{
	for (int x=1; x<=size.x; x++)
	    for (int y=1; y<=size.y; y++){
		Vector g = (Vector)grid[x][y];
		String w = (String)g.elementAt(1);
		String p = (String)g.elementAt(2);
		Vector answers;
		if (!w.equals("wOK")) {
		    answers = kb.ask(Logic.parse("wOK(" + x + "," + y + ")"));
		    if (succeeded(answers)){
			g.setElementAt("wOK",1);
		    }
		    else {
			answers = kb.ask(Logic.parse("w!(" + x + "," + y + ")"));
			if (succeeded(answers)) {
			    g.setElementAt("w!",1);
			}
			else {
			    answers = kb.ask(Logic.parse("w?(" + x + "," + y + ")"));
			    if (succeeded(answers)) {
				g.setElementAt("w?",1);
			    }
			}
		    }
		}
		if (!p.equals("pOK")) {
		    answers = kb.ask(Logic.parse("pOK(" + x + "," + y + ")"));
		    if (succeeded(answers)) {
			g.setElementAt("pOK",2);
		    }
		    else {
			answers = kb.ask(Logic.parse("p!(" + x + "," + y + ")"));
			if (succeeded(answers)) {
			    g.setElementAt("p!",2);
			}
			else {
			    answers = kb.ask(Logic.parse("p?(" + x + "," + y + ")"));
			    if (succeeded(answers)) {
				g.setElementAt("p?",2);
			    }
			}
		    }
		}
	    }
	}
	catch(Exception e) {System.out.println("Illegal query to kb");};	
    }
    
	/**
	 * Did the logic query succeed?.
	 * @param answers is a list of answers from the knowledge base query.
	 * @return true if any answer was obtained.
	 */
    public boolean succeeded(Vector answers)
    {
	for (int i=0; i<answers.size(); i++)
	    if ( !((Hashtable)answers.elementAt(i)).containsKey("fail"))
		return true;
	return false;
    }	
    
	/**
	 * Record a move of the agent.
	 * @param x and y are the coordinates moved to.
	 */
    public void move(int x, int y)
    {
	Vector g1 = (Vector)grid[agentloc.x][agentloc.y];
	Vector g2 = (Vector)grid[x][y];
	g1.setElementAt("V",0);
	g1.setElementAt("wOK",1);
	g1.setElementAt("pOK",2);
	g2.setElementAt("A",0);
	g2.setElementAt("wOK",1);
	g2.setElementAt("pOK",2);
	agentloc = new Location(x,y);
    }
    
	/**
	 * Has the cell been visited before?.
	 * @param x and y are the coordiates of the cell.
	 * @return true if cell previously visited.
	 */
    public boolean visited(int x, int y)
    {
	Vector g = (Vector)grid[x][y];
	String v = (String)g.elementAt(0);
	return v.equals("V") || v.equals("A");
    }
    
	/**
	 * Add the percept information to the knowledge base.
	 * @param s is the string indicating whether the agent perceives a stench.
	 * @param b is the string indicating whether the agent perceives a breeze.
	 */
    public void percept(String s, String b)
    {
	try{
	if (s.equals("stench"))
	    kb.tell(Logic.parse("smell(" + agentloc.x + "," + agentloc.y + ")"));
	else
	    kb.tell(Logic.parse("nosmell(" + agentloc.x + "," + agentloc.y + ")"));
	if (b.equals("breeze"))
	    kb.tell(Logic.parse("breeze(" + agentloc.x + "," + agentloc.y + ")"));
	else
	    kb.tell(Logic.parse("nobreeze(" + agentloc.x + "," + agentloc.y + ")"));
	}
	catch(Exception e) {};
    }
    
	/**
	 * Dispay the known information inferred about the grid.
	 */
    public void display()
    {
	wumpgrid.paint(wumpgrid.getGraphics());
    }
  
	/**
	 * Determine cells known to be okay.
	 * @return a vector of safe cells.
	 */
    public Vector okayMoves()
    {
	Vector ok = new Vector();
	for (int x=1; x<=size.x; x++)
	    for (int y=1; y<=size.y; y++) {
		Vector g = (Vector)grid[x][y];
		String v = (String)g.elementAt(0);
		String w = (String)g.elementAt(1);
		String p = (String)g.elementAt(2);
		if (v.equals("") && w.equals("wOK") && p.equals("pOK"))
		    ok.addElement(new Location(x,y));
	    }
	return ok;
    }
 
	/**
	 * Determine cells that have unknown risk.
	 * @return a vector of risky cells.
	 */
    public Vector riskyMoves()
    {
	Vector risky = new Vector();
	for (int x=1; x<=size.x; x++)
	    for (int y=1; y<=size.y; y++) {
		Vector g = (Vector)grid[x][y];
		String v = (String)g.elementAt(0);
		String w = (String)g.elementAt(1);
		String p = (String)g.elementAt(2);
		if (v.equals("") && (w.equals("w?") || p.equals("p?") &&
		     !(w.equals("w!") || p.equals("p!"))))
		    risky.addElement(new Location(x,y));
	    }
	return risky;
    }
    
	/**
	 * Determine block distance between two locations.
	 * @param loc1 and loc2 are two locations.
	 * @return a distance.
	 */
    public int distance(Location loc1, Location loc2)
    {
	// block distance between two locations
	return Math.abs(loc1.x - loc2.x) + Math.abs(loc1.y - loc2.y);
    }

class WumpusGrid extends Canvas{
	int cellSize = 50;
	int startx = 25;
	int starty = 25;
	int endx, endy;
	int numCols, numRows;
    
	public WumpusGrid() {
	    numRows = size.y;
	    numCols = size.x;
	    endx = startx + numCols*cellSize;
	    endy = starty + numRows*cellSize;
	    Color metalColor = MetalLookAndFeel.getDesktopColor();
	    setBackground(metalColor);
	    setSize(400,400);
	}
    
	public void paint(Graphics g) {
	    g.setColor(Color.white);
	    g.fillRect(startx, starty, numCols*cellSize, numRows*cellSize);
	
	    // draw row and col lines
	    g.setColor(Color.black);
	    for (int i=0; i<=numCols; i++)
		g.drawLine(startx+i*cellSize, starty, startx+i*cellSize, endy);
	    for (int i=1; i<=numCols; i++)
		g.drawString(Integer.toString(i),startx+i*cellSize-cellSize/2, endy+15);
	    for (int i=0; i<=numRows; i++)
		g.drawLine(startx, starty+i*cellSize, endx, starty+i*cellSize);
	    for (int i=0; i<numRows; i++)
		g.drawString(Integer.toString(numRows-i), startx-15, 
		    starty+(i+1)*cellSize-cellSize/2);
	    // draw cell contents
	    for (int i=1; i<=numCols; i++)
		for (int j=1; j<=numRows; j++) {
		    int pos = 0;
		    Vector v = (Vector)grid[i][j];
		    String visit = (String)v.elementAt(0);
		    String wump = (String)v.elementAt(1);
		    String pit = (String)v.elementAt(2);
		    if (!visit.equals("")) {
		    	addtext(visit,i,j,pos);
		    	pos++;
		    }
		    if (wump.equals("wOK") && pit.equals("pOK")) {
		    	addtext("OK",i,j,pos);
		    	pos++;
		    }
		    if (pit.equals("p?") || pit.equals("p!") ){
		    	addtext(pit,i,j,pos);
		    	pos++;
		    }
		    if (wump.equals("w?") || wump.equals("w!")) {
		    	addtext(wump,i,j,pos);
		    	pos++;
		    }
		}
	}
    
	Point screenpos(int x, int y) {
	    return new Point(startx+cellSize*(x-1), endy-cellSize*y);
	}
    
	void addtext(String text, int x, int y, int pos) {
	    Graphics g = getGraphics();
	    g.setColor(Color.black);
	    Point p = screenpos(x,y);
	    g.drawString(text, p.x + 10, p.y + (pos+1)*10 + 5);
	}
}
    
    class PathProblem extends Problem
    // Search problem for finding the shortest path between grid cells
    {
	Location goalLoc;
	Location startLoc;
	Location startHeading;
	
	PathProblem(Location start, Location heading, Location goal)
	{
	    super(new PathProblemState(start, heading));
	    startLoc = start;
	    startHeading = heading;
	    goalLoc = goal;
	    display = false;
	}

	public boolean goalReached(Object state)
	{
	    PathProblemState s = (PathProblemState) state;
	    return (s.loc.x==goalLoc.x && s.loc.y==goalLoc.y);
	}

	// Return a list of StateActionPairs 
	public Vector successors(Object state)
	{
	    Vector succ = new Vector();
	    PathProblemState s = (PathProblemState) state;
	    // Determine location achieved after a possible forward move
	    Location newloc = new Location(s.loc.x+s.heading.x,s.loc.y+s.heading.y);
	    if (newloc.x>=1 && newloc.y>=1 && newloc.x<=size.x && newloc.y<=size.y){
		Vector g = (Vector)grid[newloc.x][newloc.y];
		String v = (String)g.elementAt(0);
		if (v.equals("V") || (newloc.x == goalLoc.x && newloc.y==goalLoc.y))
		    succ.addElement(new StateActionPair("forward",new PathProblemState(newloc,s.heading)));  
	    }
	    succ.addElement(new StateActionPair("turn left",
				    new PathProblemState(s.loc,
				    rotate(s.heading,0,-1,1,0))));
	    succ.addElement(new StateActionPair("turn right",
				    new PathProblemState(s.loc,
				    rotate(s.heading,0,1,-1,0))));
           
	    return succ;
	}
	
	public boolean equalState(Object state1, Object state2)
	{
	    PathProblemState s1 = (PathProblemState)state1;
	    PathProblemState s2 = (PathProblemState)state2;
	    return (s1.loc.x==s2.loc.x && s1.loc.y==s2.loc.y && s1.heading.x==s2.heading.x &&
		    s1.heading.y==s2.heading.y);
	}
	
	public String stateToString(Object state)
	{
	    String str = new String();
	    PathProblemState s = (PathProblemState)state;
	    str = "(" + s.loc.x + "," + s.loc.y + "),(" + s.heading.x + "," + s.heading.y +  ")";
	    return str;
	}
	
	public String actionToString(Object action)
	{
	    String str = new String(" ");
	    if (action != null) {
		str = (String)action;
	    }
	    return str;
	}
	
	Location rotate(Location heading, int a, int b, int c, int d)
	{
	    return new Location(heading.x*a + heading.y*b,
				heading.x*c + heading.y*d);
	}
			
    }

    public class PathProblemState
    {
	Location loc;
	Location heading;
    
	public PathProblemState(Location l, Location h)
	{
	    loc = l;
	    heading = h;
	}
    }	
}
