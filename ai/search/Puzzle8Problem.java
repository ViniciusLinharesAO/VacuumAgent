package ai.search;


import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
/**
 * The 8-puzzle Problem
  
   In this implementation of the 8-puzzle we have a mix of priorities
   between efficiency and simplicity.  The representation of states
   is not the obvious one (a 3x3 array), but it is both efficient
   and fairly easy to manipulate.  We represent each tile as an
   integer from 0 to 8, arranged as follows:
  
   0 1 2
   3 4 5
   6 7 8
 
   Finally, we represent a state (i.e. a complete puzzle) as the sum
   of the tile numbers times 9 to the power of the tile's square number.
   For example, the state state from p63:
   
   1 2 3			1*9^0 + 2*9^1 + 3*9^2
   8 . 4  is represented by:  + 8*9^3 + 0*9^4 + 4*9^5
   7 6 5		      + 7*9^6 + 6*9^7 + 5*9^8 = 247893796
   
   We represent actions with the four symbols <, >, ^, V to stand
   for moving the blank tile left, right, up and down respectively.
 *
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class Puzzle8Problem extends Problem
		     implements ActionListener
{
	int[] power9 = {1,9,81,729,6561,59049,531441,4782969,43046721,387420489};
	int displayState, newState;
	JButton solution;
	
	Puzzle8Problem(String h)
	{
		super(null);
		heuristic = h;
		initialState = randomPuzzleState();
		displayState = ((Integer)initialState).intValue();
		goal = puzzleGoal();
		display = true;
	}

	/**
	 * Is the state a goal state?
	 */
	public boolean goalReached(Object state)
	{
	    return ((Integer)state).intValue() == ((Integer)goal).intValue();
	}

	/**
	 * Determine all the states which are successors of the given state.
	 * @param state is the given state
	 * @return a vector of successors
	 */
	public Vector successors(Object state)
	{
		int s = ((Integer)state).intValue();
		Vector succ = new Vector();
		int blank = blankPos(s);  // position of the blank
		Vector blankMoves = legalMoves(blank);
		for (int i=0; i<blankMoves.size(); i++){
		    Move m = (Move)blankMoves.elementAt(i);
		    int destination = m.destination;
		    succ.addElement(new StateActionPair(m.action,moveBlank(s,blank,m.destination)));
		}
		return succ;
	}
	
	/**
	 * Estimate the number of moves to the goal using heuristic.
	 * @param state is the given state
	 */
	public int hCost(Object state)
	{
	    int sum=0;
	    int s = ((Integer)state).intValue();
	    
	    if (heuristic.equals("Manhattan Distance")) {
		for (int square=0; square<9; square++)
		{
		    int tile =  tileAtPos(s,square);
		    if (tile != 0) sum += 
			xyDistance(tileLoc(square),tileGoalLoc(tile)); 
		}
	    }
	    else if (heuristic.equals("Number of Misplaced Tiles"))
	    {
		for (int square=0; square<9; square++)
		{
		    int tile = tileAtPos(s,square);
		    int goaltile = tileAtPos(puzzleGoal().intValue(),square);
		    if (tile != 0 &&  goaltile != tile)
			sum++;
		}
	    }
	    return sum; 
	}
	
	public boolean equalState(Object state1, Object state2)
	{
	    return ((Integer)state1).intValue() == ((Integer)state2).intValue();
	}
	
	public String stateToString(Object state)
	{
	    int s = ((Integer)state).intValue();
	    String str = new String();
	    for (int i=0; i<9; i++){
		if (i%3 == 0) str = str + '\n' ;
		int tile = tileAtPos(s,i);
		if (tile==0) str = str + ".";
		else str = str + tile;
	    }
	    return str;
	}
	
	public String actionToString(Object action)
	{
	    if (action != null)
		return (String)action;
	    else return new String("");
	}
	
	int xyDistance(Point p, Point q)
	{
	    return Math.abs(p.x-q.x) + Math.abs(p.y-q.y);
	}
	
	Integer moveBlank(int state, int from, int to)
	{
	   return new Integer(state + tileAtPos(state,to) * (power9[from] - power9[to]));
	}
	
	int blankPos(int state)
	{
	    for (int i=0; i<9; i++)
		if (tileAtPos(state,i)==0) return i;
	    return 9;
	}
	
	int tileAtPos(int state, int pos)
	{  // return the tile at position pos
	   return ((int)Math.floor(state / power9[pos])) % 9;
	}
	
	Integer puzzleState(int[] pieces)
	{   // return a new state with the specified tiles
	    int sum=0;
	    for (int i=0; i<pieces.length; i++)
		sum += pieces[i]*power9[i];
	    return new Integer(sum);
	}
	
	/**
	 * Give the legal moves from the position.
	 * @return a vector of legal moves
	 */
	Vector legalMoves(int pos)
	{   
	    Vector moves = new Vector();
	    switch (pos) {
	    case 0: moves.addElement(new Move(">",1));
		    moves.addElement(new Move("V",3));
		    break;
	    case 1: moves.addElement(new Move("<",0));
		    moves.addElement(new Move(">",2));
		    moves.addElement(new Move("V",4));
		    break; 
	    case 2: moves.addElement(new Move("<",1));
		    moves.addElement(new Move("V",5));
		    break; 
	    case 3: moves.addElement(new Move("^",0));
		    moves.addElement(new Move(">",4));
		    moves.addElement(new Move("V",6));
		    break; 
	    case 4: moves.addElement(new Move("^",1));
		    moves.addElement(new Move("<",3));
		    moves.addElement(new Move(">",5));
		    moves.addElement(new Move("V",7));
		    break;
	    case 5: moves.addElement(new Move("^",2));
		    moves.addElement(new Move("<",4));
		    moves.addElement(new Move("V",8));
		    break;
	    case 6: moves.addElement(new Move("^",3));
		    moves.addElement(new Move(">",7));
		    break;
	    case 7: moves.addElement(new Move("^",4));
		    moves.addElement(new Move("<",6));
		    moves.addElement(new Move(">",8));
		    break;
	    case 8: moves.addElement(new Move("^",5));
		    moves.addElement(new Move("<",7));
	    }
	    return moves; 
	}
	
	Integer randomPuzzleState()
	{
	  Integer state = puzzleGoal();
	  // make 100 random moves
	  for (int i=0; i<100; i++){
	    int blank = blankPos(state.intValue());
	    Vector neighbor = neighbors(blank);
	    int j = (int)Math.floor(Math.random()*neighbor.size());
	    state = moveBlank(state.intValue(),blank,
			    ((Integer)neighbor.elementAt(j)).intValue());
	  }
	  return state; 
	}
	
	Integer puzzleGoal()
	{
	    int[] pieces = {1,2,3,8,0,4,7,6,5};
	    return puzzleState(pieces);
	}
	
	Point tileLoc(int square)
	{   // the xy location of square position
	    switch (square) {
	    case 0: return new Point(0,2);
	    case 1: return new Point(1,2);
	    case 2: return new Point(2,2);
	    case 3: return new Point(0,1);
	    case 4: return new Point(1,1);
	    case 5: return new Point(2,1);
	    case 6: return new Point(0,0);
	    case 7: return new Point(1,0);
	    case 8: return new Point(2,0);
	    }
	    return null;
	}
	    
	Point tileGoalLoc(int tile)
	{   // the xy location of tile in the goal
	    switch (tile) {
	    case 0: return new Point(1,1);
	    case 1: return new Point(0,2);
	    case 2: return new Point(1,2);
	    case 3: return new Point(2,2);
	    case 4: return new Point(2,1);
	    case 5: return new Point(2,0);
	    case 6: return new Point(1,0);
	    case 7: return new Point(0,0);
	    case 8: return new Point(0,1);
	    }
	    return null;
	}
	
	/**
	 * Return the squares that can be reached from a given square.
	 */
	Vector neighbors(int pos)
	{ 
	    Vector n = new Vector();
	    switch (pos) {
	    case 0: n.addElement(new Integer(1));
		    n.addElement(new Integer(3));
		    break;
	    case 1: n.addElement(new Integer(0));
		    n.addElement(new Integer(2));
		    n.addElement(new Integer(4));
		    break; 
	    case 2: n.addElement(new Integer(1));
		    n.addElement(new Integer(5));
		    break; 
	    case 3: n.addElement(new Integer(0));
		    n.addElement(new Integer(4));
		    n.addElement(new Integer(6));
		    break; 
	    case 4: n.addElement(new Integer(1));
		    n.addElement(new Integer(3));
		    n.addElement(new Integer(5));
		    n.addElement(new Integer(7));
		    break;
	    case 5: n.addElement(new Integer(2));
		    n.addElement(new Integer(4));
		    n.addElement(new Integer(8));
		    break;
	    case 6: n.addElement(new Integer(3));
		    n.addElement(new Integer(7));
		    break;
	    case 7: n.addElement(new Integer(4));
		    n.addElement(new Integer(6));
		    n.addElement(new Integer(8));
		    break;
	    case 8: n.addElement(new Integer(5));
		    n.addElement(new Integer(7));
	    }
	    return n; 
	}
	
	void updateProbPanel()
	{
	    if (display) {
		probPanel.numExpanded.setText("   " + numExpanded);
		probPanel.paintImmediately(probPanel.getVisibleRect());
	    }
	}
	
	JPanel createControlPanel(ProblemPanel probPanel)
	{
	    JPanel cp = super.createControlPanel(probPanel);
	    probPanel.queuePanel.setVisible(false);
	    return cp;
	}
	
	Container createButtonPanel(ProblemPanel probPanel)
	{
	    JButton search = new JButton("Search");
	    JButton rebuild = new JButton("Rebuild");
	    JButton newprob = new JButton("New puzzle");
	    solution = new JButton("Show Solution");
	    solution.setEnabled(false);
	    search.addActionListener(this);
	    rebuild.addActionListener(this);
	    solution.addActionListener(this);
	    newprob.addActionListener(this);
	    JPanel p = new JPanel();
	    p.setLayout(new GridLayout(4,1));
	    p.add(search);
	    p.add(rebuild);
	    p.add(newprob);
	    p.add(solution);
	    return p;
	}
	
	Container createInputPanel(ProblemPanel probPanel)
	{
	    Color metalColor = MetalLookAndFeel.getTextHighlightColor();
	    String[] searchStrings = {"A* Search"};
	    String[] heuristicStrings = {"Manhattan Distance",
					  "Number of Misplaced Tiles"};
	    probPanel.searches = new JComboBox(searchStrings);
	    probPanel.heuristics = new JComboBox(heuristicStrings);
	    probPanel.maxExpanded = new JTextField("1000",4);
	    
	    Box b1 = new Box(BoxLayout.X_AXIS);
	    b1.setBackground(metalColor);
	    JLabel label1 = new JLabel("  Heuristic ");
	    label1.setForeground(Color.black);
	    b1.add(label1);
	    b1.add(probPanel.heuristics);
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
	    b.add(Box.createVerticalStrut(10));
	    b.add(b1);
	    b.add(Box.createVerticalStrut(20));
	    b.add(b2);
	    b.add(Box.createVerticalStrut(20));
	    return b;
	}
	
	void drawCanvas(Graphics g)
	{
	    int state = displayState;
	    if (searchResult == null)
		state = ((Integer)initialState).intValue();
	    canvas.setBackground(Color.darkGray.brighter());
	    displayPuzzle(state,g); 
	}
	
	void displaySolution(Graphics g)
	{
	    solution.setEnabled(true);
	}
	
	void displayPuzzleSolution(Graphics g)
	{
	    displayPuzzle(((Integer)initialState).intValue(),g);
	    displayNode(searchResult,g);
	    
	}
	
	void displayNode(Node n, Graphics g)
	{
	    if (n.parent == null) {
		newState = ((Integer)n.state).intValue();
		updateCanvas(g);
	    }
	    else {
		displayNode(n.parent,g);
		newState = ((Integer)n.state).intValue();
		updateCanvas(g);
	    }
	}
	
	void displayPuzzle(int state, Graphics g)
	{
	    int size = 75;
	    int x = 250;
	    int y = 100;
	    g.setFont(new Font("Monospaced",Font.BOLD,24));
	    g.setColor(Color.black);
	    g.fillRect(x-4,y-4,size*3+8,size*3+8);
	    for (int i=0; i<3; i++)
		for (int j=0; j<3; j++){
		    int tilenum = tileAtPos(state,3*i+j);
		    if (tilenum != 0) {
			g.setColor(Color.lightGray);
			g.fill3DRect(x+j*size,y+i*size, size,size,true);
			g.setColor(Color.black);
			g.drawString(""+tilenum,x+j*size+30,y+i*size+40);
		    }
		    else {
			g.setColor(Color.white);
			g.fillRect(x+j*size,y+i*size, size,size);
		    }
		}    
	}
	
	void updateCanvas(Graphics g)
	{
	    // change the squares in the canvas which need to be changed
	    int size = 75;
	    int x=250;
	    int y=100;
	    int pos1 = blankPos(displayState);
	    int pos2 = blankPos(newState);
	    int tilenum = tileAtPos(newState,pos1);
	    g.setFont(new Font("Monospaced",Font.BOLD,24));
	    g.setColor(Color.lightGray);
	    g.fill3DRect(x+(pos1%3)*size,y+(pos1/3)*size,size,size,true);
	    g.setColor(Color.white);
	    g.fillRect(x+(pos2%3)*size,y+(pos2/3)*size,size,size);
	    if(tilenum!=0) {
		g.setColor(Color.black);
		g.drawString(""+tilenum,x+(pos1%3)*size+30,y+(pos1/3)*size+40);
	    }
	    
	    try{Thread.sleep(500);}
	    catch(Exception e){};
	    displayState = newState;
	}
	
	public void actionPerformed(ActionEvent e)
	{
	    String action = e.getActionCommand();
	    if (action.equals("Show Solution")) {
		displayPuzzleSolution(canvas.getGraphics());
		solution.setEnabled(false);
	    }
	    else if (action.equals("New puzzle")) {
		search = null;
		searchResult = null;
		stop();
		initialState = randomPuzzleState();
		initializeProbPanel();
	    }
	    else
		super.actionPerformed(e);
	}  
	
    class Move
    {
	String action;
	int destination;
    
	Move(String a, int d)
	{
	    action = a;
	    destination = d;
	}
    }  
}
