package ai.worlds; 

/*  The Agents and Environment module panel.  This module allows 
    the creation of both vacuum and wumpus agents.  The worlds can
    be built and viewed or several agents within a world can be
    compared 
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import ai.logic.ExtensionFileFilter;
import ai.worlds.vacuum.*;
import ai.worlds.wumpus.*;
/**
 * A Panel containing the information for world creation.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

@SuppressWarnings("serial")
public class WorldCreatePanel extends JPanel implements ActionListener, ItemListener {
	private static final Color metalColor = MetalLookAndFeel.getTextHighlightColor(); 
	
    @SuppressWarnings("rawtypes")
	private JComboBox envs;
    @SuppressWarnings("rawtypes")
	private JComboBox agents;
    private JTextField xsize;
    private JTextField ysize;
    @SuppressWarnings("rawtypes")
	private JList trialAgents;
    JTextField numTrials;
    private JCheckBox randomSizes;
    
    private JPanel northPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JPanel northCenterPanel = new JPanel();
    
    private JScrollPane scroll;
    private JTextArea results;
    private JLabel title;
    private JPanel p = new JPanel();
    @SuppressWarnings("unused")
	private String s = new String();
    private String filepath, filename;
    
    public JButton load = new JButton();
    public JButton rebuild = new JButton();
    public JButton buildWorld = new JButton();
    public JButton simWorld = new JButton();
    
    private JButton trials;
    private boolean isLoaded = false;
    
    /**
     * The list of worlds.
     */
    String[] worldStrings = {"Vacuum World", "Wumpus World"};
    /**
     * The list of vacuum agents.
     */
    String[] vacuumStrings = {"Random Vacuum Agent", "Reactive Vacuum Agent"};
    /**
     * The list of wumpus agents.
     */
    String[] wumpusStrings = {"Random Wumpus Agent", "Aimless Wumpus Agent", "Logic Testing Agent"};
    
    private GridBagLayout gridbag;
    private GridBagConstraints constraints;
    
    private WumpusWorldEditor wwe = new WumpusWorldEditor();
    
    private JFrame holder;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public WorldCreatePanel(JFrame f)
    {
    	holder = f;
    	setLayout(new BorderLayout());
    	//setup NorthPanel
    	envs = new JComboBox(worldStrings);
    	envs.addItemListener(this);
    	agents = new JComboBox(vacuumStrings);
    	xsize = new JTextField("8",2);
    	ysize = new JTextField("8",2);
    	
    	
    	rebuild = new JButton("Rebuild Loaded World");
    	rebuild.setActionCommand("Rebuild World");
    	rebuild.addActionListener(this);
    	rebuild.setEnabled(false);
    	buildWorld.setText("Create a Wumpus World");
    	buildWorld.addActionListener(this);
    	simWorld.setText("Simulate an Agent in an Environment");
    	simWorld.addActionListener(this);
    	load = new JButton("Load Wumpus World");
    	load.addActionListener(this);
    	gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.BOTH;
    	
	    gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.BOTH;
		northPanel.setBackground(metalColor);
		northPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		northPanel.setLayout(new BorderLayout());
		
		JPanel northNorthPanel = new JPanel(gridbag);
		
		constraints.gridwidth = 2;
		constraints.insets = new Insets(4, 35, 4, 35);
    	gridbag.setConstraints(buildWorld, constraints);
    	northNorthPanel.add(buildWorld);
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	gridbag.setConstraints(simWorld, constraints);
    	northNorthPanel.add(simWorld);
    	constraints.gridwidth = 1;
    	constraints.insets = new Insets(8, 4, 8, 4);
		northNorthPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		northPanel.add(northNorthPanel, BorderLayout.NORTH);
		
		northCenterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		northCenterPanel.setPreferredSize(new Dimension(750, 75));
		northPanel.add(northCenterPanel, BorderLayout.CENTER);
    	
	    add(northPanel, BorderLayout.NORTH);
		trialAgents = new JList(vacuumStrings);
		JScrollPane scrollPane = new JScrollPane(trialAgents);
		scrollPane.setPreferredSize(new Dimension(175,95));
		scrollPane.setBackground(metalColor);
		numTrials = new JTextField("10",2);
		randomSizes = new JCheckBox("",false);
		randomSizes.setBackground(metalColor);
		trials = new JButton("Run Trials");
		trials.addActionListener(this);
	    
		JPanel trialChoicePanel = new JPanel();
		trialChoicePanel.add(new JLabel(" Select Trial Agents: "));
		trialChoicePanel.add(scrollPane);
		trialChoicePanel.setBackground(metalColor);
	    
		gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.insets = new Insets(2,8,2,8);
	    constraints.gridwidth = 1;
		JPanel numTrialsPanel = new JPanel(gridbag);
		JLabel l = new JLabel("Random Sizes");
		gridbag.setConstraints(l, constraints);
		numTrialsPanel.add(l);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(randomSizes, constraints);
		numTrialsPanel.add(randomSizes);
		constraints.gridwidth = 1;
		JLabel label = new JLabel("Number of Trials");
		gridbag.setConstraints(label, constraints);
		numTrialsPanel.add(label);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(numTrials, constraints);
		numTrialsPanel.add(numTrials);
		numTrialsPanel.setBackground(metalColor);
	    
		p.setBackground(metalColor);
		p.setLayout(new BorderLayout());
		results = new JTextArea();
		results.setEditable(false);
		scroll = new JScrollPane(results);
		scroll.setPreferredSize(new Dimension(250, 75));
		title = new JLabel("Scores:");
		title.setFont(new Font("SansSerif",Font.ITALIC + Font.BOLD,14));
		p.add("North",title);
		p.add("Center",scroll);
		
		gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.insets = new Insets(8,8,8,8);
		southPanel.setBackground(metalColor);
		southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		southPanel.setLayout(gridbag);
		JLabel trialTitle = new JLabel("Agent Trials",0);
		trialTitle.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 14));
		gridbag.setConstraints(trialTitle, constraints);
		southPanel.add(trialTitle);
		gridbag.setConstraints(trialChoicePanel, constraints);
		southPanel.add(trialChoicePanel);
		gridbag.setConstraints(numTrialsPanel, constraints);
		southPanel.add(numTrialsPanel);
		gridbag.setConstraints(trials, constraints);
		southPanel.add(trials);
		gridbag.setConstraints(p, constraints);
		southPanel.add(p);
		add(southPanel, BorderLayout.SOUTH);
		centerPanel.setPreferredSize(new Dimension(750, 400));
		centerPanel.setBackground(Color.white);
		centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		add(centerPanel, BorderLayout.CENTER);
		load.setEnabled(false);
    }
	
    public void actionPerformed(ActionEvent e) {
    	String action = e.getActionCommand();
		if (action.equals("Build Random")) build();
		else if (action.equals("Run Trials")) trials();
		else if(action.equals("Load Wumpus World")) load();
		else if(action.equals("Rebuild World")) rebuild();
		else if(action.equals("Create a Wumpus World")) createWorld();
		else if(action.equals("save")) wwe.save();
		else if(action.equals("Simulate an Agent in an Environment")) simulate();
    }
    
    public void simulate() {
    	buildWorld.setBackground(Color.gray.brighter());
    	simWorld.setBackground(metalColor);
    	northPanel.remove(northCenterPanel);
    	gridbag = new GridBagLayout();
    	constraints = new GridBagConstraints();
    	
    	JButton build = new JButton("Build Random");
    	build.addActionListener(this);
    	
		JPanel choicePanel = new JPanel(gridbag);
    	JLabel label1 = new JLabel(" Environment: ");
    	JLabel label2 = new JLabel(" Agent: ");
    	gridbag.setConstraints(label1, constraints);
    	choicePanel.add(label1);
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	gridbag.setConstraints(envs, constraints);
    	choicePanel.add(envs);
    	constraints.gridwidth = 1;
    	gridbag.setConstraints(label2, constraints);
    	choicePanel.add(label2);
    	gridbag.setConstraints(agents, constraints);
    	choicePanel.add(agents);
    	choicePanel.setBackground(metalColor);
    	gridbag = new GridBagLayout();
	    constraints = new GridBagConstraints();
	    constraints.insets = new Insets(0,2,0,2);
	    constraints.fill = GridBagConstraints.BOTH;
		JPanel sizePanel = new JPanel(gridbag);
		JLabel xlabel = new JLabel(" x size: ");
		JLabel ylabel = new JLabel(" y size: ");
		gridbag.setConstraints(xlabel, constraints);
		sizePanel.add(xlabel);
		gridbag.setConstraints(xsize, constraints);
		sizePanel.add(xsize);
		gridbag.setConstraints(build, constraints);
		sizePanel.add(build);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(rebuild, constraints);
		sizePanel.add(rebuild);		
		constraints.gridwidth = 1;
		gridbag.setConstraints(ylabel, constraints);
		sizePanel.add(ylabel);
		gridbag.setConstraints(ysize, constraints);
		sizePanel.add(ysize);
		gridbag.setConstraints(load, constraints);		
		sizePanel.add(load);
		sizePanel.setBackground(metalColor); 
		JLabel buildTitle = new JLabel("Create an Environment and Agent",0);
		buildTitle.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 14));
		
		gridbag = new GridBagLayout();
		constraints = new GridBagConstraints();
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(2,2,2,2);
		northCenterPanel = new JPanel(gridbag);
    	northCenterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		northCenterPanel.setPreferredSize(new Dimension(750, 75));
		northCenterPanel.setBackground(metalColor);
		gridbag.setConstraints(buildTitle, constraints);
		northCenterPanel.add(buildTitle);
		gridbag.setConstraints(choicePanel, constraints);
		northCenterPanel.add(choicePanel);
		gridbag.setConstraints(sizePanel, constraints);
		northCenterPanel.add(sizePanel);
		northPanel.add(northCenterPanel, BorderLayout.CENTER);
		northPanel.setVisible(false);
		northPanel.setVisible(true);
		
		remove(centerPanel);
    	centerPanel = new JPanel();
    	centerPanel.setPreferredSize(new Dimension(750, 400));
    	centerPanel.setBackground(Color.white);
    	centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	centerPanel.setLayout(new BorderLayout());
    	add(centerPanel);
		centerPanel.setVisible(false);
		centerPanel.setVisible(true);
    }
	
    public void itemStateChanged(ItemEvent e) {
    	String world = (String)envs.getSelectedItem();
    	if (world.equals("Vacuum World")) {
    		changeAgentChoices(vacuumStrings);
    		xsize.setText("8");
    		ysize.setText("8");
    		load.setEnabled(false);
    		rebuild.setEnabled(false);
    		randomSizes.setSelected(true);
    	}
    	else if (world.equals("Wumpus World")) {
    		changeAgentChoices(wumpusStrings);
    		xsize.setText("4");
    		ysize.setText("4");
    		load.setEnabled(true);
    		if(isLoaded) rebuild.setEnabled(true);
    		randomSizes.setSelected(false);
    	}
    }
    
    private void build() {
    	Agent[] a = new Agent[1];
    	a[0] = createAgent((String)agents.getSelectedItem());
    	GridEnvironment world;
    	if (envs.getSelectedItem() ==  "Vacuum World") {
    		int x = Integer.parseInt(xsize.getText());
        	int y = Integer.parseInt(ysize.getText());
    		world = new VacuumWorld(a, x, y, .25, holder);
    		holder.setTitle("Artificial Intelligence - Agents and Environments - Vacuum World");
    	}
    	else {
    		int x = 4;
    		int y = 4;
    		world = new WumpusWorld(a, x, y, holder);
    		holder.setTitle("Artificial Intelligence - Agents and Environments - Wumpus World");
    	}
    	remove(centerPanel);
    	centerPanel = new JPanel();
    	centerPanel.setPreferredSize(new Dimension(750, 400));
		centerPanel.setBackground(Color.white);
		centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	centerPanel.setLayout(new BorderLayout());
    	JPanel center2 = new JPanel(new BorderLayout());
    	center2.add(world.canvas, BorderLayout.CENTER);
		centerPanel.add(center2, BorderLayout.CENTER);
		centerPanel.add("North",world.gridPanel);
		add(centerPanel);
		setVisible(false);
		setVisible(true);
    }
    
    private void trials() {
    	TrialSet s = new TrialSet();
    	s.start();
    	trials.setEnabled(false);
    	envs.setEnabled(false);
    }
    
    private void load() {
    	Agent[] a = new Agent[1];
    	a[0] = createAgent((String)agents.getSelectedItem());
    	GridEnvironment world;
    	int x = Integer.parseInt(xsize.getText());
    	int y = Integer.parseInt(ysize.getText());
    	boolean gotFile = getFileName();
    	if(gotFile) {
    		world = new WumpusWorld(a, x, y, holder, filepath);
    		holder.setTitle("Artificial Intelligence - Agents and Environments - Wumpus World");
    		rebuild.setEnabled(true);
    		isLoaded = true;
    		remove(centerPanel);
    		centerPanel = new JPanel();
    		centerPanel.setPreferredSize(new Dimension(750, 400));
    		centerPanel.setBackground(Color.white);
    		centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    		centerPanel.setLayout(new BorderLayout());
    		JPanel center2 = new JPanel(new BorderLayout());
    		center2.add(world.canvas, BorderLayout.CENTER);
    		centerPanel.add(center2, BorderLayout.CENTER);
    		centerPanel.add("North",world.gridPanel);
    		add(centerPanel);
    		setVisible(false);
    		setVisible(true);
    	}
    }
    
    private void rebuild() {
    	Agent[] a = new Agent[1];
    	a[0] = createAgent((String)agents.getSelectedItem());
    	int x = Integer.parseInt(xsize.getText());
    	int y = Integer.parseInt(ysize.getText());
    	GridEnvironment world = new WumpusWorld(a, x, y, holder, filepath);
    	remove(centerPanel);
		centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(750, 400));
		centerPanel.setBackground(Color.white);
		centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		centerPanel.setLayout(new BorderLayout());
		JPanel center2 = new JPanel(new BorderLayout());
		center2.add(world.canvas, BorderLayout.CENTER);
		centerPanel.add(center2, BorderLayout.CENTER);
		centerPanel.add("North",world.gridPanel);
		add(centerPanel);
		setVisible(false);
		setVisible(true);
    }
    
    private void createWorld() {
    	simWorld.setBackground(Color.gray.brighter());
    	buildWorld.setBackground(metalColor);
    	JButton saveButton = new JButton("Click here to save your world.");
    	saveButton.setActionCommand("save");
    	saveButton.addActionListener(this);
    	northPanel.remove(northCenterPanel);
    	gridbag = new GridBagLayout();
    	northCenterPanel = new JPanel(gridbag);
    	northCenterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		northCenterPanel.setPreferredSize(new Dimension(750, 75));
		northCenterPanel.add(saveButton);
		northPanel.add(northCenterPanel, BorderLayout.CENTER);
    	remove(centerPanel);
    	centerPanel = new JPanel();
    	centerPanel.setPreferredSize(new Dimension(750, 400));
    	centerPanel.setBackground(Color.white);
    	centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	centerPanel.setLayout(new BorderLayout());
    	centerPanel.add(wwe, BorderLayout.CENTER);
    	centerPanel.add(new JLabel("Directions: Right-click in a cell you'd like to add something to.  Check what you'd like to add.  When you are satisfied with your world, save it."), BorderLayout.NORTH);
    	add(centerPanel);
    	centerPanel.setVisible(false);
    	centerPanel.setVisible(true);
    }
    
    private boolean getFileName() {
    	try {
			JFileChooser chooser = new JFileChooser();
			ExtensionFileFilter filter = new ExtensionFileFilter();
			filter.addExtension("ww");
		    filter.setDescription("Wumpus World");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filepath = chooser.getSelectedFile().getPath();
				filename = chooser.getSelectedFile().getName();
				if (filename.endsWith(".ww")) {
					return true;
				}
			} 
		} catch(Exception ex) {}
		return false;
    }
    
    @SuppressWarnings("unchecked")
	private void changeAgentChoices(String[] agentName)
    // changes the agents listed in the agents and trialAgents
    // lists to the Strings given in the array agentName
    {
	agents.removeAllItems();
	trialAgents.removeAll();
	trialAgents.setListData(agentName);
	for (int i=0; i<agentName.length; i++)
	    agents.addItem(agentName[i]);
	
    }
    
    private Agent createAgent(String agentName) {
    	if (agentName == "Random Vacuum Agent") return new RandomVacuumAgent();
    	else if (agentName == "Reactive Vacuum Agent") return new ReactiveVacuumAgent();
    	else if (agentName == "Random Wumpus Agent") return new RandomWumpusAgent(); 
    	else if (agentName == "Logic Testing Agent") return new LogicTestingAgent();
    	else  return new AimlessWumpusAgent();
    }
    
    private class TrialSet extends Thread {
    	@SuppressWarnings("deprecation")
		public void run() {
    		Object[] agts = trialAgents.getSelectedValues();
        	int[] scores = new int[agts.length+1]; 
        	Agent[] a = new Agent[1];
        	a[0] = new RandomVacuumAgent();
        	GridEnvironment world, w;
        	int x = Integer.parseInt(xsize.getText());
    		int y = Integer.parseInt(ysize.getText());
    	
    		for (int j=0; j<Integer.parseInt(numTrials.getText()); j++) {
    		// perform a trial    
    			if (randomSizes.isSelected()){
    				x = (int) (Math.random()*18 + 2);
    				y = (int) (Math.random()*18 + 2);
    			}    	    
    			if ((String)envs.getSelectedItem() ==  "Vacuum World") {  
    				world = new VacuumWorld(a,x,y,.25, holder);
    				w = new VacuumWorld(a,x,y,.25, holder);
    			}
    			else {
    				world = new WumpusWorld(a,x,y, holder);
    				w = new WumpusWorld(a,x,y, holder);
    			}
    	    
    			for (int i=0; i<agts.length; i++) {
    				a[0] = createAgent((String)agts[i]);
    				GridEnvironment.copyGrid (world,w,x+2, y+2);
    				if ((String)envs.getSelectedItem()== "Wumpus World") ((WumpusWorld)w).w = ((WumpusWorld)world).w;
    				// put the agent in the world
    				w.step = 0;
    				w.agents = a;
    				// run the agent without displaying
    				w.display = false;
    				w.run();
    				// record agent score
    				scores[i] += w.agents[0].score;
    			}
    		}
    		p.remove(scroll);
    		results.setText("");
    		for (int i=0; i<agts.length; i++) {
    			if (i>0) results.append("\n");
    			results.append( agts[i] + ":   " + scores[i]/Integer.parseInt(numTrials.getText()) + "  ");
    		}
    		trials.setEnabled(true);
    		envs.setEnabled(true);
    		p.add(scroll);
    		p.setVisible(false);
    		p.setVisible(true);
    	}
    }
}
