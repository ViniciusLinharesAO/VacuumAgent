package ai.search;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
/**
 * An abstract search problem.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public abstract class Problem implements Runnable,
				  ActionListener
{
	/**
	 * The initial state.
	 */
	public Object initialState;	
	/**
	 * An optional goal state.
	 */
	public Object goal;		
	/**
	 * The number of nodes expanded in the search.
	 */
	public int numExpanded;	
	/**
	 * Whether or not to display the search.
	 */
	public boolean display;	
	/**
	 * canvas to display the search.
	 */
	public Canvas canvas;		
	/**
	 * The search queue.
	 */
	public Vector q;		
	Thread runner;
	/**
	 * The search.
	 */
	public Search search;
	/**
	 * The search algorithm.
	 */
	public String algorithm;
	/**
	 * The search heuristic.
	 */
	public String heuristic;
	/**
	 * The node which is the search result.
	 */
	public Node searchResult;	
	/**
	 * The current node in the search.
	 */
	public Node currentNode;	// The current node in the search
	ProblemPanel probPanel; 

	public Problem(Object initState)
	{
		initialState = initState;
		numExpanded = 0;
		display = true;
		canvas = new ProblemCanvas();
		search = null;
	}

	/**
	 * Determine all the states which are successors of the given state.
	 * @param state is the given state
	 * @return a vector of successors
	 */
	public abstract Vector successors(Object state);
	/**
	 * Determine if two states are equivalent.
	 * @param state1 and states are the two states
	 * @return true if equal
	 */
	public abstract boolean equalState(Object state1, Object state2);
	/**
	 * Convert a state to a string.
	 * @param state is the given state
	 * @return a string expressing the state
	 */
	public abstract String stateToString(Object state); 
	/**
	 * Convert an action to a string.
	 * @param action is the given action
	 * @return a string expressing the action
	 */
	public abstract String actionToString(Object action);
	
	/**
	 * Draw the initial canvas.
	 */
	void drawCanvas(Graphics g){}
	/**
	 * Draw the current display after a search step.
	 */
	void currentDisplay(Graphics g){}
	/**
	 * Alter the display after a solution is found.
	 */
	void displaySolution(Graphics g){}

	/**
	 * Is the state a goal state?
	 */
	public boolean goalReached(Object state)
	{
		return equalState(state,goal);
	}
	
	/**
	 * Return the estimated cost from given state to a goal.
	 * A default value is 0.
	 */
	public int hCost(Object state)
	{
		return 0;
	}

	/**
	 * Return the cost of an edge.
	 * A default value is 1.
	 */
	public int edgeCost(Node node, Object action, Object state)
	{
		return 1;
	}

	Node createStartNode()
	{
		int h = hCost(initialState);
		return (new Node(initialState,null,null,0,0,h,h));
	}
	
	public void start()
	{
	    if (runner == null) {
		runner = new Thread(this);
		runner.start();
	    }
	}
	
	public void stop()
	{
	    if (runner!=null){
		runner = null;
	    }
	}
	
	public void run()
	{
	    searchResult = search.execute(algorithm);
	}

	/**
	 * Solve the problem with the specified search algorithm.
	 * @param alg is the algorithm.
	 * @param heur is an optional heuristic
	 * @param maxExpanded is the maximum number of nodes to expand
	 * @return the node of a solution.
	 */	
	public Node solve(String alg, String heur, int maxExpanded)
	{
		if (search == null)
		    createSearch(alg,heur,maxExpanded);
		if (searchResult == null)
		    run();
		return searchResult;
	}
	
	void createSearch(String alg,String heur, int maxExpanded)
	{
	    numExpanded = 0;
	    algorithm = alg;
	    heuristic = heur;
	    searchResult=null;
	    search = new Search(this,maxExpanded);
	}
	
	Node step(String alg, String heur, int maxExpanded)
	// perform one step in the search 
	{
	    if (search == null)
		createSearch(alg,heur, maxExpanded);
	    if (searchResult == null)
		searchResult = search.processNextNode(q,alg);
	    return searchResult;
	}
	
	void initialize()
	{
	    probPanel = new ProblemPanel();
	}

	void initializeProbPanel()
	{
	    canvas.setBackground(Color.white);
	    drawCanvas(canvas.getGraphics());
	    probPanel.numExpanded.setText("    0");
	    probPanel.cost.setText(" ");
	    q = new Vector();
	    q.addElement(createStartNode());
	    probPanel.queuePanel.update(q);
	}
	
	void updateProbCanvas()
	{
	    if (display && canvas.getGraphics() != null) {
		currentDisplay(canvas.getGraphics());
		try {Thread.sleep(100);}
		catch (Exception e){}
	    }
	}
	
	void updateProbPanel()
	{
	    if (display) {
		probPanel.queuePanel.update(q);
		probPanel.numExpanded.setText("   " + numExpanded);
		probPanel.repaint(0);
		//probPanel.paintImmediately(probPanel.getVisibleRect());
		try {Thread.sleep(500);}
		catch (Exception e){}
	    }
	}
	
	JPanel createControlPanel(ProblemPanel probPanel)
	{
	    JPanel cp = new JPanel();
	    Color metalColor = MetalLookAndFeel.getTextHighlightColor();
	    cp.setBackground(metalColor);
	
	    JPanel p = new JPanel();
	    p.setBackground(metalColor);
	    p.setLayout(new BorderLayout());
	    p.add("East",createResultsPanel(probPanel));
	    p.add("Center",createInputPanel(probPanel));
	
	    cp.setLayout(new BorderLayout());
	    cp.add("West",createButtonPanel(probPanel));
	    cp.add("East",p);
	    return cp;
	}
	
	Container createButtonPanel(ProblemPanel probPanel)
	{
	    JButton search = new JButton("Search");
	    JButton step = new JButton("Step");
	    search.addActionListener(this);
	    step.addActionListener(this);
	    JPanel p = new JPanel();
	    p.setLayout(new GridLayout(2,1));
	    p.add(search);
	    p.add(step);
	    return p;
	}
	
	Container createInputPanel(ProblemPanel probPanel)
	{
	    Color metalColor = MetalLookAndFeel.getTextHighlightColor();
	    String[] searchStrings = {"Breadth First Search",
				      "Depth First Search",
				      "Iterative Deepening Search",
				      "No Returns Breadth First Search",
				      "No Duplicates Breadth First Search",
				      "No Cycles Depth First Search",
				      "Greedy Search", "Tree A* Search",
				      "Uniform Cost Search", "A* Search"};
	    String[] heuristicStrings = {"none"};
	    probPanel.searches = new JComboBox(searchStrings);
	    probPanel.searches.setMaximumRowCount(3);
	    probPanel.heuristics = new JComboBox(heuristicStrings);
	    probPanel.maxExpanded = new JTextField("50",4);
	    
	    Box b1 = new Box(BoxLayout.X_AXIS);
	    b1.setBackground(metalColor);
	    JLabel label1 = new JLabel("  Search Algorithm ");
	    label1.setForeground(Color.black);
	    b1.add(label1);
	    b1.add(probPanel.searches);
	    b1.add(Box.createHorizontalStrut(50));
	
	    Box b2 = new Box(BoxLayout.X_AXIS);
	    b2.setBackground(metalColor);
	    label1 = new JLabel("  Max Expanded Nodes  ");
	    label1.setForeground(Color.black);
	    b2.add(label1);
	    b2.add(probPanel.maxExpanded);
	    b2.add(Box.createHorizontalStrut(200));
	    
	    Box b = new Box(BoxLayout.Y_AXIS);
	    b.setBackground(metalColor);
	    b.add(Box.createVerticalStrut(2));
	    b.add(b1);
	    b.add(Box.createVerticalStrut(2));
	    b.add(b2);
	    b.add(Box.createVerticalStrut(2));
	    return b;
	}
	
	Container createResultsPanel(ProblemPanel probPanel)
	{
	    Color metalColor = MetalLookAndFeel.getTextHighlightColor();
	    probPanel.numExpanded = new JLabel("    0");
	    probPanel.numExpanded.setForeground(Color.black);
	    probPanel.cost = new JLabel(" ");
	    probPanel.cost.setForeground(Color.black);
	    
	    JPanel p = new JPanel();
	    p.setBackground(metalColor);
	    p.setLayout(new GridLayout(2,3));
	    JLabel label1 = new JLabel("# Expanded Nodes    ");
	    label1.setForeground(Color.black);
	    JLabel label2 = new JLabel("Solution Cost");
	    label2.setForeground(Color.black);
	    p.add(label1);
	    p.add(label2);
	    p.add(probPanel.numExpanded);
	    p.add(probPanel.cost);
	    return p;
	}
	
	public void actionPerformed(ActionEvent e)
	{
	    String action = e.getActionCommand();
	    if (action.equals("Search")) {
	    	Solver s = new Solver();
	    	s.start();
	    }
	    else if (action.equals("Step")) {
		String s = (String)probPanel.searches.getSelectedItem();
		String h = (String)probPanel.heuristics.getSelectedItem();
		int max = Integer.parseInt(probPanel.maxExpanded.getText());
		Node n = step(s,h,max);
		handleSolution();
	    }
	}
	
	void handleSolution()
	{
	    if (searchResult != null && canvas.getGraphics() != null) {
		probPanel.cost.setText("" + searchResult.gCost);
		displaySolution(canvas.getGraphics());
	    }
	}
	
	class ProblemCanvas extends Canvas {
	    public void paint(Graphics g) {
	    	drawCanvas(g);
	    }
    }
    
    class ProblemPanel extends JPanel {
    	JComboBox searches;
    	JComboBox heuristics;
    	JTextField maxExpanded;
    	JLabel numExpanded;
    	JLabel cost;
    	QueuePanel queuePanel;
	
    	ProblemPanel() {
    		queuePanel = new QueuePanel();
    		JPanel cp =  createControlPanel(this);
	    	cp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
	    	canvas.setSize(new Dimension(650,380));
	    	setLayout(new BorderLayout());  
	    	setBackground(Color.white);
	    	add("Center",canvas);
	    	add("East",queuePanel);
	    	add("North",cp);
    	}
    }
    
    class QueuePanel extends JPanel
    {
	JTextArea currentQueue;
	Problem problem;
    
	QueuePanel()
	{
	    currentQueue = new JTextArea("     Queue     ",25,13);
	    currentQueue.setEditable(false);
	    currentQueue.append(""+'\n'+"___________________"+ '\n');
	    Node n = createStartNode();
	    currentQueue.append(" " + stateToString(n.state) + "  (f:" + n.fCost +
				 "  h:" + n.hCost + ")" + '\n');
	    setBackground(Color.white);
	    JScrollPane scrollPane = new JScrollPane(currentQueue,
		    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS ,
		    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
	    scrollPane.setSize(40,500);
	    add(scrollPane);
	} 
    
	void update(Vector q)
	{
	    currentQueue.setText("     Queue     ");
	    currentQueue.append(""+'\n'+"___________________"+ '\n');
	    for (int i=0; i<q.size(); i++){
		Node n = (Node)q.elementAt(i);
		currentQueue.append(stateToString(n.state) + "  (f:" + n.fCost +
				 "  h:" + n.hCost + ")" + '\n');
	    
	    }
	}    
    }
    private class Solver extends Thread {
    	public void run() {
    		String s = (String)probPanel.searches.getSelectedItem();
    		String h = (String)probPanel.heuristics.getSelectedItem();
    		int max = Integer.parseInt(probPanel.maxExpanded.getText());
    		Node n = solve(s,h,max);
    		handleSolution();
    	}
    }
}
