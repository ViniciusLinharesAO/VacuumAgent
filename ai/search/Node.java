package ai.search;

import java.util.Vector;
/**
 * A node in a search problem.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public class Node
{
	/**
	 * A state in the domain.
	 */
	public Object state;	
	/**
	 * The parent Node.
	 */
	public Node parent;	
	/**
	 * The domain action leading to the state.
	 */
	public Object action;	
	/**
	 * A list of successor Nodes.
	 */
	public Vector successors; 
	/**
	 * The depth of the node in the tree (root = 0).
	 */
	public int depth;		
	/**
	 * The path cost from root to node.
	 */
	public int gCost;		
	/**
	 * Estimated distance from state to goal.
	 */
	public int hCost;		
	/**
	 * gCost + hCost.
	 */
	public int fCost;		

	Node()
	{}

	Node(Object st)
	{
		state = st;
		depth = 0;
		gCost = 0;
		hCost = 0;
		fCost = 0;
		//expanded = false;
		successors = new Vector();
	}

	Node(Object st, Node prnt, Object act, int dth, int g, int h, int f)
	{
		state = st;
		parent = prnt;
		action = act;
		depth = dth;
		gCost = g;
		hCost = h;
		fCost = f;
		//expanded = false;
		successors = new Vector();
	}

	/**
	 * Generate a list of all the nodes that can be reached from a node.
	 * Note: the problem's successor method returns a list of (action . state) pairs.
	 * This function turns each of these into a node.
	 * @param problem is a search problem
	 * @return the list of nodes that can be reached from this node.
	 */
	public Vector expand(Problem problem)
	{
		//if (expanded) return null;
		// else {
			//expanded = true;
		problem.numExpanded = problem.numExpanded + 1;
		Vector succList = problem.successors(state);
		Vector nodes = new Vector();
		for (int i=0; i<succList.size(); i++){
		    StateActionPair pair = (StateActionPair)succList.elementAt(i);
		    int g = gCost + problem.edgeCost(this,pair.action,pair.state);
		    int h = problem.hCost(pair.state);
		    int f = fCost;
		    if (g+h > fCost) f = g+h;
		    nodes.addElement(new Node(pair.state,this,pair.action,depth+1,
								  g,h,f));
		}
		return nodes;
		//}
	}

	/**
	 * Generate a list of actions that reach a solution.
	 * @return the list of actions that reach a solution.
	 */
	public Vector solutionActions()
	{
		Vector answer; 
		if (parent == null) {
			answer = new Vector();
			//answer.addElement(action);
		}		
		else {
			answer = parent.solutionActions();
			answer.addElement(action);
		}
		return answer;
	}

	/**
	 * Generate a list of nodes that reach a solution.
	 * @return the list of nodes that reach a solution.
	 */
	public Vector solutionNodes()
	{
		Vector answer;
		if (parent == null) {
			answer = new Vector();
			answer.addElement(this);
		}
		else {
			answer = parent.solutionNodes();
			answer.addElement(this);
		}
		return answer;
	}
	
	boolean loopingNode(Problem problem,int depth)
	{
	    Node ancestor = parent;
	    for (int i=0; i<depth; i++){
		if (ancestor == null) return false;
		if (problem.equalState(state,ancestor.state)) return true;
		else ancestor = ancestor.parent;
	    }
	    return false;
	}
	
	boolean returnNode(Problem problem)
	{
	    return loopingNode(problem,2);
	}

}