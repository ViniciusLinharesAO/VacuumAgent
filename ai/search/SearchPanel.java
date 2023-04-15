package ai.search;

/* The Search module panel.  This module provides several search problems
   which can be solved with different search algorithms.  The algorithms can
   be compared
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class SearchPanel extends JPanel implements ActionListener {	
	private static final Color metalColor = MetalLookAndFeel.getTextHighlightColor();
	
    JComboBox problems;
    JList searchList;
    JTextField numTrials;
    JTextField maxExpanded;
    JFrame holder;
    
    private JPanel northPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    
    private GridBagLayout gridbag;
    private GridBagConstraints constraints;

    public SearchPanel(JFrame f) {
    	String[] problemStrings = {	"Route Finding (Romania)",
				   					"Route Finding (Random Cities)",
				   					"Missionaries and Cannibals",
				   					"8 Puzzle"};
    	String[] searchStrings = {	"Breadth First Search",
				  					"Depth First Search",
				  					"Iterative Deepening Search",
				  					"No Returns Breadth First Search",
				  					"No Duplicates Breadth First Search",
				  					"No Cycles Depth First Search",
				  					"Greedy Search", "Tree A* Search",
				  					"Uniform Cost Search", "A* Search"};
				      
    	holder = f;	      
    	setLayout(new BorderLayout());	      
    	problems = new JComboBox(problemStrings);
        JButton buildProb = new JButton("Build Problem");
        buildProb.addActionListener(this);
        JLabel probTitle = new JLabel("Search Problems",0);
        probTitle.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 18));
        
        northPanel.setBackground(metalColor);
        northPanel.setPreferredSize(new Dimension(1000, 60));
        northPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.insets = new Insets(8,8,8,8);
	    
		JLabel label = new JLabel(" Problem:");
		northPanel.setLayout(gridbag);
		gridbag.setConstraints(probTitle, constraints);
		northPanel.add(probTitle);
		gridbag.setConstraints(label, constraints);
		northPanel.add(label);
		gridbag.setConstraints(problems, constraints);
		northPanel.add(problems);
		gridbag.setConstraints(buildProb, constraints);
		northPanel.add(buildProb);
		add(northPanel, BorderLayout.NORTH);
	    
		searchList = new JList(searchStrings);
		JScrollPane scrollPane = new JScrollPane(searchList);
		scrollPane.setPreferredSize(new Dimension(230, 90));
		JTextArea compareTitle = new JTextArea(" Compare\n  Search\nAlgorithms");
		compareTitle.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 13));
		numTrials = new JTextField("10",3);
		maxExpanded = new JTextField("100",3);
		JButton trials = new JButton("Search Compare");
		trials.addActionListener(this);
		southPanel.setBackground(Color.white);
		southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.insets = new Insets(2,4,2,4);
	    constraints.gridwidth = 1;
		JPanel p1 = new JPanel(gridbag);
		p1.setBackground(Color.white);
		JLabel label2 = new JLabel("Number of Trials");
		gridbag.setConstraints(label2, constraints);
		p1.add(label2);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(numTrials, constraints);
		p1.add(numTrials);
		JLabel label3 = new JLabel("Max Nodes Expanded");
		constraints.gridwidth = 1;
		gridbag.setConstraints(label3, constraints);
		p1.add(label3);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(maxExpanded, constraints);
		p1.add(maxExpanded);
		constraints.gridwidth = 2;
		gridbag.setConstraints(trials, constraints);
		p1.add(trials);
	    
		JPanel p2 = new JPanel();
		p2.setBackground(Color.white);
		JTextArea area = new JTextArea("   Search\nAlgorithms");
		area.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 14));
		p2.add(area);
		p2.add(scrollPane);
		
		
		gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.insets = new Insets(8,8,8,8);
		southPanel.setLayout(gridbag);
		gridbag.setConstraints(compareTitle, constraints);
		southPanel.add(compareTitle);
		gridbag.setConstraints(p2, constraints);
		southPanel.add(p2);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(p1, constraints);
		southPanel.add(p1);
		add(southPanel, BorderLayout.SOUTH);
		
		centerPanel.setPreferredSize(new Dimension(750, 480));
		centerPanel.setBackground(Color.white);
		centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		add(centerPanel, BorderLayout.CENTER);
    }
	
    public void actionPerformed(ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals("Build Problem")) build();
    	else if (action.equals("Search Compare")) trials();
    }
	
    private void build() {
    	String probTitle = (String)problems.getSelectedItem();
    	Problem problem = createProblem(probTitle);
    	problem.initialize();
		
    	remove(centerPanel);
    	centerPanel = new JPanel();
    	centerPanel.setPreferredSize(new Dimension(750, 480));
		centerPanel.setBackground(Color.white);
		centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	centerPanel.setLayout(new BorderLayout());
		centerPanel.add(problem.probPanel , BorderLayout.CENTER);
		add(centerPanel);
		centerPanel.setVisible(false);
		centerPanel.setVisible(true);	
    }
	
    private void trials() {
    	TrialRunner t = new TrialRunner();
    	t.start();
    }
    
    String precision(String num) {
    	int decpos = num.indexOf('.');
    	int lastpos = decpos + 3;
    	if (lastpos > num.length()) lastpos =num.length();
		String s = num.substring(0,lastpos);
		return s;
    }
    
    String repeated(String s, int n) {
    	String r = new String(s);
		for (int i=1; i<n; i++) r = r + s;
		return r;
    }
	
    private Problem createProblem(String probName) {
    	Problem problem = new RomanianProblem("Arad","Bucharest");
    	if (probName.equals("Route Finding (Romania)")) problem = new RomanianProblem("Arad","Bucharest");
    	else if (probName.equals("Route Finding (Random Cities)")) problem = new RouteProblem("A","B",null);
    	else if (probName.equals("Missionaries and Cannibals")) problem = new CannibalProblem();
    	else if (probName.equals("8 Puzzle")) problem = new Puzzle8Problem("Manhattan Distance");
    	return problem;
    }
    private class TrialRunner extends Thread {
    	public void run() {
    		String probTitle = (String)problems.getSelectedItem();
        	Object[] srches = searchList.getSelectedValues();
        	int[] numSolved = new int[srches.length];
        	int[] gCost = new int[srches.length];
        	int[] numExpanded = new int[srches.length];
        	int[] pathLength = new int[srches.length];
    	
        	JTextArea results = new JTextArea();
        	results.setText("");
        	results.append("" + '\n');
        	results.append("Solved  Cost  Length    Nodes      Algorithm" + '\n');
        	results.append("_________________________________________________" + '\n');
        	int numtrials = Integer.parseInt(numTrials.getText());
    	
        	for (int j=0; j<numtrials; j++) {
        		// perform a trial
        		Problem problem = createProblem((String)problems.getSelectedItem());
        		problem.display = false;
        		for (int i=0; i<srches.length; i++) {
        			problem.search=null;
        			Node n = problem.solve((String)srches[i],"Manhattan Distance", Integer.parseInt(maxExpanded.getText()));
        			numExpanded[i] += problem.numExpanded;
        			if (n != null) {
        				numSolved[i]++;
        				gCost[i] += n.gCost;
        				pathLength[i] += n.depth;
        			}
        		}
        	}
        	for (int i=0; i<srches.length; i++) {
        		double m = numSolved[i];
        		if (m==0) m=1;
        		String numSolvedStr = Integer.toString(numSolved[i]);
        		String gCostStr = precision(Double.toString(gCost[i]/m));
        		String pathLengthStr = precision(Double.toString(pathLength[i]/m));
        		String numExpandedStr = precision(Double.toString(numExpanded[i]/(double)numtrials));
        		results.append("  " + numSolvedStr + repeated(" ",6-numSolvedStr.length()) + 
        				gCostStr + repeated(" ",8 - gCostStr.length()) +
        				pathLengthStr + repeated(" ",9 - pathLengthStr.length()) + 
        				numExpandedStr + repeated(" ",9 - numExpandedStr.length()) +
        				(String)srches[i] + '\n');
        	}
    		
        	JFrame f = new JFrame("Search Compare Results");
        	f.addWindowListener(new WindowAdapter() {
        		public void windowClosing(WindowEvent e) {
        			e.getWindow().dispose();
        		}
        	});
    	
        	JPanel p = new JPanel();
        	p.setBackground(Color.white);
        	JLabel title = new JLabel(probTitle);
        	title.setFont(new Font("SansSerif",Font.ITALIC+Font.BOLD,14));
        	p.setLayout(new BorderLayout());
        	p.add("North",title);
        	p.add("Center",results);
        	p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5,10,10,10)));
        	f.getContentPane().add(p);
        	f.pack();
    		f.setLocation(10,10);
    		f.setVisible(true);
    	}
    }
}
