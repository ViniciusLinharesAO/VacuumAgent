package ai.search;

import java.awt.*;
import java.util.Vector;
/**
 * Search problem using a road map of Romania.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu 
 *
 */
public class RomanianProblem extends RouteProblem
{
    RomanianProblem(String startCity, String goalCity)
    {
	super(startCity,goalCity,null);
	cities = new Vector();
	Vector neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Zerind",75));
	neighbors.addElement(makeNeighbor("Sibiu",140));
	neighbors.addElement(makeNeighbor("Timisoara",118));
	cities.addElement(makeCity("Arad",new Point(91,492),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Fagaras",211));
	neighbors.addElement(makeNeighbor("Pitesti",101));
	neighbors.addElement(makeNeighbor("Giurgiu",90));
	neighbors.addElement(makeNeighbor("Urziceni",85));
	cities.addElement(makeCity("Bucharest",new Point(400,327),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Dobreta",120));
	neighbors.addElement(makeNeighbor("Rimnicu",146));
	neighbors.addElement(makeNeighbor("Pitesti",138));
	cities.addElement(makeCity("Craiova",new Point(253,288),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Mehadia",75));
	neighbors.addElement(makeNeighbor("Craiova",120));
	cities.addElement(makeCity("Dobreta",new Point(165,299),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Hirsova",86));
	cities.addElement(makeCity("Eforie",new Point(562,293),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Sibiu",99));
	neighbors.addElement(makeNeighbor("Bucharest",211));
	cities.addElement(makeCity("Fagaras",new Point(305,449),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Bucharest",90));
	cities.addElement(makeCity("Giurgiu",new Point(375,270),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Urziceni",98));
	neighbors.addElement(makeNeighbor("Eforie",86));
	cities.addElement(makeCity("Hirsova",new Point(534,350),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Neamt",87));
	neighbors.addElement(makeNeighbor("Vaslui",92));
	cities.addElement(makeCity("Iasi",new Point(473,506),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Timisoara",111));
	neighbors.addElement(makeNeighbor("Mehadia",70));
	cities.addElement(makeCity("Lugoj",new Point(165,379),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Lugoj",70));
	neighbors.addElement(makeNeighbor("Dobreta",75));
	cities.addElement(makeCity("Mehadia",new Point(168,339),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Iasi",87));
	cities.addElement(makeCity("Neamt",new Point(406,537),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Zerind",71));
	neighbors.addElement(makeNeighbor("Sibiu",151));
	cities.addElement(makeCity("Oradea",new Point(131,571),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Rimnicu",97));
	neighbors.addElement(makeNeighbor("Craiova",138));
	neighbors.addElement(makeNeighbor("Bucharest",101));
	cities.addElement(makeCity("Pitesti",new Point(320,368),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Sibiu",80));
	neighbors.addElement(makeNeighbor("Pitesti",97));
	neighbors.addElement(makeNeighbor("Craiova",146));
	cities.addElement(makeCity("Rimnicu",new Point(233,410),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Arad",140));
	neighbors.addElement(makeNeighbor("Oradea",151));
	neighbors.addElement(makeNeighbor("Fagaras",99));
	neighbors.addElement(makeNeighbor("Rimnicu",80));
	cities.addElement(makeCity("Sibiu",new Point(207,457),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Arad",118));
	neighbors.addElement(makeNeighbor("Lugoj",111));
	cities.addElement(makeCity("Timisoara",new Point(94,410),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Bucharest",85));
	neighbors.addElement(makeNeighbor("Hirsova",98));
	neighbors.addElement(makeNeighbor("Vaslui",142));
	cities.addElement(makeCity("Urziceni",new Point(456,350),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Iasi",92));
	neighbors.addElement(makeNeighbor("Urziceni",142));
	cities.addElement(makeCity("Vaslui",new Point(509,444),neighbors));
	neighbors = new Vector();
	neighbors.addElement(makeNeighbor("Arad",75));
	neighbors.addElement(makeNeighbor("Oradea",71));
	cities.addElement(makeCity("Zerind",new Point(108,531),neighbors));
    }
}