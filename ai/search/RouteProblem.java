package ai.search;

import java.util.Vector;
import java.awt.*;
/**
 * Find a route between cities on a map.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class RouteProblem extends Problem
{
	/**
	 * A collection of cities.
	 */
	Vector cities;
	int scalex, scaley;
	
	RouteProblem(String startCity, String goalCity, Vector map)
	{
		super(startCity);
		goal = goalCity;
		if (map != null)
		    cities = map;
		else 
		    cities = makeRandomMap(10);
		scalex = 65; scaley = 200;
		canvas.setBackground(Color.white);
	}

	public boolean goalReached(Object state)
	{
		String s = (String) state;
		return (s.equals(goal));
	}


	/**
	 * Determine all the states which are successors of the given state.
	 * @param state is the given state
	 * @return a vector of successors of state-action, which are both the name of the city
	 */
	public Vector successors(Object state)
	{
	    Vector succ = new Vector();
	    City c = findCity((String)state);
	    for (int i=0; i<c.neighbors.size(); i++) {
		Neighbor n = (Neighbor)c.neighbors.elementAt(i);
		succ.addElement(new StateActionPair(n.name,n.name));
	    }
	    return succ;
	}
	
	/**
	 * Determine the cost of an edge which is the distance between the cities.
	 */
	public int edgeCost(Node n, Object action, Object state)
	{
	    City c1 = findCity((String)n.state);
	    City c2 = findCity((String)state);
	    if (c1 == c2) return 0;
	    else {
		Neighbor neighbor = findNeighbor(c2.name,c1.neighbors);
		return neighbor.distance;
	    }
	}
	
	/**
	 * Estimate the cost to goal by taking straight-line distance.
	 */
	public int hCost(Object state)
	{
	    City c1 = findCity((String)state);
	    City c2 = findCity((String)goal);
	    return straightDistance(c1.location, c2.location);
	}
	
	public boolean equalState(Object state1, Object state2)
	{
	    return ((String)state1).equals((String)state2);
	}
	
	public String stateToString(Object state)
	{
	    return (String)state;
	}
	
	public String actionToString(Object action)
	{
	    if (action != null)
		return (String)action;
	    else 
		return new String("");
	}
	
	City makeCity(String name, Point location, Vector neighbors)
	{
	    return new City(name,location,neighbors); 
	}
	
	Neighbor makeNeighbor(String name, int distance)
	{
	    return new Neighbor(name,distance);
	}
	
	void currentDisplay(Graphics g)
	{
	    City c2 = findCity((String)currentNode.state);
	    c2.draw(g,Color.black,scalex,scaley); 
	    if (currentNode.parent != null ) {
		City c1 = findCity((String)currentNode.parent.state);
		int x1 = c1.location.x;
		int y1 = c1.location.y;
		int x2 = c2.location.x;
		int y2 = c2.location.y;
		g.setColor(Color.orange);
		g.drawLine(x1-scalex,y1-scaley,x2-scalex,y2-scaley);
	    }
	}
	
	void displaySolution(Graphics g)
	{
	    Node n = searchResult;
	    while (n.parent != null) {
		City c1 = findCity((String)n.parent.state);
		City c2 = findCity((String)n.state);
		int x1 = c1.location.x;
		int y1 = c1.location.y;
		int x2 = c2.location.x;
		int y2 = c2.location.y;
		g.setColor(Color.red);
		g.drawLine(x1-scalex,y1-scaley,x2-scalex,y2-scaley);
		n = n.parent;
	    }
	}
	
	City findCity(String city)
	{
	    for (int i=0; i<cities.size(); i++)
		if (((City)cities.elementAt(i)).name.equals(city))
		    return (City)cities.elementAt(i);
	    return null;
	}
	
	Neighbor findNeighbor(String city, Vector neighbors)
	{
	    for (int i=0; i<neighbors.size(); i++)
		if (((Neighbor)neighbors.elementAt(i)).name.equals(city))
		    return (Neighbor)neighbors.elementAt(i);
	    return null;
	}
	
	int straightDistance(Point p1, Point p2)
	{
	    int dx = p1.x - p2.x;
	    int dy = p1.y - p2.y;
	    return (int)Math.round(Math.sqrt(dx*dx + dy*dy));
	}
	
	Vector makeRandomMap(int nCities)
	{
	    String names = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    Vector map = new Vector();
	    for (int i=0; i<nCities; i++){
		String name = names.substring(i,i+1);
		Point location = new Point((int)Math.floor(Math.random()*500 + 100),
					 (int)Math.floor(Math.random()*350 + 250));
					 
		City c = new City(name,location,new Vector());
		map.addElement(c);
	    }
	    
	    // Lay down roads (1-2 times straight line distance)
	    for (int i=0; i<nCities; i++) {
		City c1 = (City)map.elementAt(i);
		Vector neighbors = c1.neighbors;
		int maxRoads = nCities;
		if (nCities>3) maxRoads = 4;
		int numRoads = (int)Math.floor(Math.random()*(maxRoads -1)) + 1 - neighbors.size();
		Vector candidates = buildNeighbors(numRoads, c1, map);
		for (int j=0; j<candidates.size(); j++)
		    buildRoad(c1,(City)candidates.elementAt(j));
	    }
		
	    return map;
	}
	
	Vector buildNeighbors(int numRoads, City c1, Vector map)
	{
	    Vector notNeighbors = new Vector();
	    for (int i=0; i<map.size(); i++)
	    {
		City c2 = (City)map.elementAt(i);
		Neighbor n1 = findNeighbor(c2.name,c1.neighbors);
		if (c1!=c2 && n1==null)
		    notNeighbors.addElement(c2);
	    }
	    // Randomly choose n candidates from the cities which are not already neighbors
	    int count = 0;
	    Vector candidates = new Vector();
	    while (count < numRoads && notNeighbors.size()>0) {
		int i = (int)Math.floor(Math.random()*notNeighbors.size());
		candidates.addElement(notNeighbors.elementAt(i));
		notNeighbors.removeElementAt(i);
		count++;
	    }
	    
	    return candidates;
	}
		    
		
	void buildRoad(City c1, City c2)
	{
	    int distance = straightDistance(c1.location,c2.location);
	    int roadDistance = (int)Math.floor((Math.random()*100 +100) * distance / 100);
	    c1.neighbors.addElement(new Neighbor(c2.name,roadDistance));
	    c2.neighbors.addElement(new Neighbor(c1.name,roadDistance));
	}
	
	void drawCanvas(Graphics g)
	{
	  for (int i=0; i<cities.size(); i++) {
	    City c1 = (City)cities.elementAt(i);
	    c1.draw(g,Color.green.darker(),scalex,scaley); 
	    for (int j=0; j<c1.neighbors.size(); j++){
		Neighbor n = (Neighbor)c1.neighbors.elementAt(j);
		City c2 = findCity(n.name);
		if (c1.name.compareTo(c2.name)<0) {
		    int x1 = c1.location.x;
		    int y1 = c1.location.y;
		    int x2 = c2.location.x;
		    int y2 = c2.location.y;
		    g.setColor(Color.lightGray);
		    g.drawLine(x1-scalex,y1-scaley,x2-scalex,y2-scaley);
		    g.setColor(Color.green.darker());
		    g.drawString(""+n.distance,x1+(x2-x1)/2-scalex,y1+(y2-y1)/2-scaley);
		}
	    }
	  }
	}
	
    class City
    {
	String name;
	Point location;
	Vector neighbors;
    
	City(String n, Point p, Vector v)
	{
	    name = n;
	    p.y = -(p.y - 800);
	    location = p;
	    neighbors = v;
	}
    
	void draw(Graphics g, Color clr, int scalex, int scaley)
	{
	    g.setColor(clr);
	    g.fillOval(location.x-scalex-5,location.y-scaley-5,10,10);
	    g.setColor(Color.blue);
	    g.drawString(name,location.x-scalex-5,location.y-scaley-10);
	}
    }
    
    class Neighbor
    {   
	String name;
	int distance;
    
	Neighbor(String n, int d)
	{
	    name = n;
	    distance = d;
	}
    }
	
}




