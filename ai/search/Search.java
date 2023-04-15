package ai.search;

import java.util.Vector;
 
/**
 * The general search function along with set of specific search strategies.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class Search
{
	/**
	 * The search problem.
	 */
	public Problem problem;
	/**
	 * The maximum number of nodes to expand.
	 */
	public int maxExpanded;
	/**
	 * A list of expanded nodes.
	 */
	public Vector nodeTable;  
	/**
	 * A depth limit.
	 */
	public int limit;
	
	/**
	 * Construct a search.
	 * @param p is a search problem
	 * @param m is the maximum nodes to be expanded.
	 */
	public Search(Problem p, int m)
	{
	    problem = p;
	    maxExpanded = m;
	    nodeTable = new Vector();
	    problem.q = new Vector();  
	    Node n = problem.createStartNode();
	    problem.q.addElement(n);
	    nodeTable.addElement(n);
	    limit = 0;
	}
	
	/**
	 * Execute the search with the given algorithm.
	 */
	public Node execute(String algorithm)
	{
		Node n = new Node();
		while(! problem.q.isEmpty() && problem.numExpanded < maxExpanded)
		{
		    n = processNextNode(problem.q, algorithm);
		    if (n != null) {
		    	return n;
		    }
			
		}
		if (problem.q.isEmpty()) return n;
		else return null;
	}
	
	/**
	 * Process the next node from the queue.
	 */
	Node processNextNode(Vector q, String algorithm)
	{
	  if (!problem.q.isEmpty() && problem.numExpanded < maxExpanded) {
	    Node n = (Node)q.elementAt(0);
	    q.removeElementAt(0);
	    problem.currentNode = n;
	    problem.updateProbCanvas();
	    
	    if (problem.goalReached(n.state)) 
		return n;
	    else {
		if (algorithm.equals("Breadth First Search"))
		    enqueueAtEnd(q,n.expand(problem));
		else if (algorithm.equals("Depth First Search"))
		    enqueueAtFront(q,n.expand(problem));
		else if (algorithm.equals("No Returns Breadth First Search"))
		    enqueueWithoutReturns(q,n.expand(problem));
		else if (algorithm.equals("No Duplicates Breadth First Search"))
		    enqueueWithoutDuplicates(q,n.expand(problem));
		else if (algorithm.equals("No Cycles Depth First Search"))
		    enqueueWithoutCycles(q,n.expand(problem));
		else if (algorithm.equals("Iterative Deepening Search")){
		    enqueueWithLimit(q,n.expand(problem));
		    if (q.size()==0) {
			limit++;
			q.addElement(problem.createStartNode());
		    }
		}
		else if (algorithm.equals("Greedy Search"))
		    enqueueByPriority(q,n.expand(problem),"hCost");
		else if (algorithm.equals("Tree A* Search"))
		    enqueueByPriority(q,n.expand(problem),"fCost");
		else if (algorithm.equals("Uniform Cost Search"))
		    enqueueByPriority(q,n.expand(problem),"depth");
		else if (algorithm.equals("A* Search"))
		    enqueueByEliminatingPriority(q,n.expand(problem));	
	    }
	    problem.updateProbPanel();
	  }	
	  return null;   
	}
	
	void enqueueAtEnd(Vector q, Vector nodes)
	{
		for (int i=0; i<nodes.size(); i++)
			q.addElement(nodes.elementAt(i));
	}

	void enqueueAtFront(Vector q, Vector nodes)
	{
		for (int i=nodes.size() -1 ; i>=0; i--)
			q.insertElementAt(nodes.elementAt(i),0);
	}
	
	void enqueueWithLimit(Vector q, Vector nodes)
	{
		for (int i=nodes.size() -1 ; i>=0; i--){
		    Node n = (Node)nodes.elementAt(i);
		    if (n.depth <= limit)
			q.insertElementAt(nodes.elementAt(i),0);
		}
	}
	
	void enqueueWithoutReturns(Vector q, Vector nodes)
	{
	    for (int i=0; i<nodes.size(); i++){
		Node n = (Node)nodes.elementAt(i);
		if (! n.returnNode(problem))
			q.addElement(n);
	    }
	}
	
	void enqueueWithoutDuplicates(Vector q, Vector nodes)
	{
	    for (int i=0; i<nodes.size(); i++){
		Node n = (Node)nodes.elementAt(i);
		if (inTable(n)==null){
			q.addElement(n);
			nodeTable.addElement(n);
		}
	    }
	}
	
	void enqueueWithoutCycles(Vector q, Vector nodes)
	{
	    for (int i=nodes.size() -1 ; i>=0; i--){
		Node n = (Node)nodes.elementAt(i);
		if (! n.loopingNode(problem,10000))
			q.insertElementAt(nodes.elementAt(i),0);
	    }
	}
	
	
	void enqueueByPriority(Vector q, Vector nodes, String key)
	{
	    for (int i=0; i<nodes.size(); i++){
		Node n = (Node)nodes.elementAt(i);
		int pos = insertPos(q,n,key);
		q.insertElementAt(n,pos);
	    }
	}
	
	int insertPos(Vector q, Node n, String key)
	{
	    for (int i=0; i<q.size(); i++)
		if (priorityCompare((Node)q.elementAt(i),n,key))
		    return i;
	    return q.size();
	}
	
	boolean priorityCompare(Node qi, Node n, String key)
	{
	    if (key.equals("hCost"))
		return qi.hCost > n.hCost;
	    else if (key.equals("fCost"))
		return qi.fCost > n.fCost;
	    else if (key.equals("depth"))
		return qi.depth > n.depth;
	    return false;
	}
	
	void enqueueByEliminatingPriority(Vector q,Vector nodes)
	{
	    Vector newNodes = new Vector();
	    for (int i=0; i<nodes.size(); i++){
		Node n = (Node)nodes.elementAt(i);
		Node ntable = inTable(n);
		if (ntable != null){
		    // update node in table with new info
		    if (n.fCost < ntable.fCost) {
			// replace old node with new in table
			ntable.fCost = n.fCost;
			newNodes.addElement(n);
		    }
		}
		else {
		    // node first seen
		    nodeTable.addElement(n);
		    newNodes.addElement(n);
		}
	    }
	    enqueueByPriority(q,newNodes,"fCost");
	}
	
	Node inTable(Node n1)
	{
	    for (int i=0; i<nodeTable.size(); i++) {
		Node n2 = (Node)nodeTable.elementAt(i);
		if (problem.equalState(n1.state,n2.state))
		    return n2;
	    }
	    return null;
	}
	    
}

