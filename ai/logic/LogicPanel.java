
package ai.logic;

// The propositional logic panel.  This module allows the creation of a 
// propositional logic knowledge base and the testing of the validity of
// propositional logic statements.
import java.awt.*;
import java.awt.event.*;


import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

public class LogicPanel extends JPanel implements ActionListener {
	static final Color metalColor = MetalLookAndFeel.getTextHighlightColor();
	static final Dimension buttonDimension = new Dimension(220, 50);
	
    JTextField expression;
    JTextArea output;
    PropKnowledgeBase propKB;
    HornKnowledgeBase hornKB;
    
    JButton tell = new JButton("Tell Knowledge-Base");
    JButton ask = new JButton("Ask Knowledge-Base");
    JButton clear = new JButton("Clear Knowledge-Base");
    JButton delete = new JButton("Delete Knowledge-Base Entry");
    JButton tt = new JButton("Display Truth-Table");
    JButton valid = new JButton("Check Validity");
    JButton open = new JButton("Open Knowledge-Base");
    JButton save = new JButton("Save Knowledge-Base");
    
    JButton propLogicButton = new JButton("Propositional Logic");
    JButton hornLogicButton = new JButton("First-Order Logic");
    
    JPanel northPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel southPanel = new JPanel();
    
    JPanel inputPanel = new JPanel();
    JPanel outputPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    
    JLabel title = new JLabel("Logic");
    JLabel kbTitle = new JLabel();
    
    JScrollPane propScrollPane;
    JScrollPane hornScrollPane;
    
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    
    public LogicPanel() {
    	title.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 18));
    	northPanel.setBackground(metalColor);
    	northPanel.setPreferredSize(new Dimension(1000, 75));
    	northPanel.setLayout(gridbag);
    	northPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	propLogicButton.setBackground(metalColor);
    	propLogicButton.addActionListener(this);
    	hornLogicButton.setBackground(metalColor);
    	hornLogicButton.addActionListener(this);
    	constraints.insets = new Insets(4, 4, 4, 4);
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	gridbag.setConstraints(title, constraints);
    	northPanel.add(title);
    	constraints.gridwidth = 1;
    	constraints.insets = new Insets(4, 4, 4, 60);
    	gridbag.setConstraints(propLogicButton, constraints);
    	northPanel.add(propLogicButton);
    	constraints.insets = new Insets(4, 60, 4, 4);
    	gridbag.setConstraints(hornLogicButton, constraints);
    	northPanel.add(hornLogicButton);
    	
    
    	expression = new JTextField(50);
    	inputPanel.setBackground(Color.white);
    	inputPanel.add(new JLabel("Input Statement:"));
    	inputPanel.add(expression);

    	output = new JTextArea("                          ");
    	output.setEditable(false);
    	output.setFont(new Font("Serif",Font.BOLD,14));
    	output.setForeground(Color.red);
    	outputPanel.setBackground(Color.white);
    	outputPanel.add(output);

    	tell.addActionListener(this);
    	tell.setPreferredSize(buttonDimension);
    	ask.addActionListener(this);
    	ask.setPreferredSize(buttonDimension);
    	clear.addActionListener(this);
    	clear.setPreferredSize(buttonDimension);
    	delete.addActionListener(this);
    	delete.setPreferredSize(buttonDimension);
    	tt.addActionListener(this);
    	tt.setPreferredSize(buttonDimension);
    	valid.addActionListener(this);
    	valid.setPreferredSize(buttonDimension);
    	open.addActionListener(this);
    	open.setPreferredSize(buttonDimension);
    	save.addActionListener(this);
    	save.setPreferredSize(buttonDimension);
    	GridBagLayout gridbag = new GridBagLayout();
    	GridBagConstraints constraints = new GridBagConstraints();
    	constraints.insets = new Insets(4,8,4,8);
    	constraints.gridwidth = 1;
    	constraints.fill = GridBagConstraints.BOTH;
    	buttonPanel.setLayout(gridbag);
    	gridbag.setConstraints(tell, constraints);
    	buttonPanel.add(tell);
    	gridbag.setConstraints(ask, constraints);
    	buttonPanel.add(ask);
    	gridbag.setConstraints(clear, constraints);
    	buttonPanel.add(clear);
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	gridbag.setConstraints(delete, constraints);
    	buttonPanel.add(delete);
    	constraints.gridwidth = 1;
    	gridbag.setConstraints(tt, constraints);
    	buttonPanel.add(tt);
    	gridbag.setConstraints(valid, constraints);
    	buttonPanel.add(valid);
    	gridbag.setConstraints(open, constraints);
    	buttonPanel.add(open);
    	gridbag.setConstraints(save, constraints);
    	buttonPanel.add(save);
    
    	centerPanel.setBackground(Color.white);
    	centerPanel.setPreferredSize(new Dimension(980, 250));
    	centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    
    	propKB = new PropKnowledgeBase();
    	hornKB = new HornKnowledgeBase();
    	propScrollPane = new JScrollPane(propKB.sentenceList);
    	propScrollPane.setPreferredSize(new Dimension(980, 250));
    	hornScrollPane = new JScrollPane(hornKB.sentenceList);
    	hornScrollPane.setPreferredSize(new Dimension(980, 250));
    	kbTitle.setText("Knowledge Base");
    	kbTitle.setFont(new Font("SansSerif", Font.ITALIC+Font.BOLD, 14));
    	gridbag = new GridBagLayout();
    	constraints = new GridBagConstraints();
    	constraints.insets = new Insets(4,4,4,4);
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	southPanel.setLayout(gridbag);
    	southPanel.setBackground(Color.white);
    	gridbag.setConstraints(kbTitle, constraints);
    	southPanel.add(kbTitle);
    	constraints.gridwidth = 1;
    	gridbag.setConstraints(propScrollPane, constraints);
    	southPanel.add(propScrollPane);
    	southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    
    	setBackground(Color.white);
    	setLayout(new BorderLayout());
    	add("North", northPanel);
    	add("Center",centerPanel);
    	add("South",southPanel);
    }
    
    public void actionPerformed(ActionEvent e) {
    	String action = e.getActionCommand();
    	if(action.equals("Propositional Logic")) {
    		title.setText(action);
    		propLogicButton.setBackground(metalColor);
    		hornLogicButton.setBackground(Color.gray.brighter());
    		remove(southPanel);
    		kbTitle.setText("Propositional Logic Knowledge Base");
    		gridbag = new GridBagLayout();
        	constraints = new GridBagConstraints();
        	constraints.insets = new Insets(4,4,4,4);
        	constraints.gridwidth = GridBagConstraints.REMAINDER;
        	southPanel = new JPanel(gridbag);
        	southPanel.setBackground(Color.white);
        	gridbag.setConstraints(kbTitle, constraints);
        	southPanel.add(kbTitle);
        	constraints.gridwidth = 1;
        	gridbag.setConstraints(propScrollPane, constraints);
        	southPanel.add(propScrollPane);
        	southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    		add(southPanel, BorderLayout.SOUTH);

    		remove(centerPanel);
    		tt.setEnabled(true);
    		valid.setEnabled(true);
    		centerPanel = new JPanel(new BorderLayout());
    		centerPanel.setBackground(Color.white);
        	centerPanel.setPreferredSize(new Dimension(980, 250));
        	centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    		centerPanel.add(inputPanel, BorderLayout.NORTH);
        	centerPanel.add(buttonPanel, BorderLayout.CENTER);
        	centerPanel.add(outputPanel, BorderLayout.SOUTH);
    		add("Center", centerPanel);
        	setVisible(false);
    		setVisible(true);
    	}
    	else if(action.equals("First-Order Logic")) {
    		title.setText(action);
    		hornLogicButton.setBackground(metalColor);
    		propLogicButton.setBackground(Color.gray.brighter());
    		
    		remove(southPanel);
    		kbTitle.setText("Horn Knowledge Base");
    		gridbag = new GridBagLayout();
        	constraints = new GridBagConstraints();
        	constraints.insets = new Insets(4,4,4,4);
        	constraints.gridwidth = GridBagConstraints.REMAINDER;
        	southPanel = new JPanel(gridbag);
        	southPanel.setBackground(Color.white);
        	gridbag.setConstraints(kbTitle, constraints);
        	southPanel.add(kbTitle);
        	constraints.gridwidth = 1;
        	gridbag.setConstraints(hornScrollPane, constraints);
        	southPanel.add(hornScrollPane);
        	southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
			add(southPanel, BorderLayout.SOUTH);
			
    		remove(centerPanel);
    		tt.setEnabled(false);
    		valid.setEnabled(false);
    		centerPanel = new JPanel(new BorderLayout());
    		centerPanel.setBackground(Color.white);
        	centerPanel.setPreferredSize(new Dimension(980, 250));
        	centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    		centerPanel.add(inputPanel, BorderLayout.NORTH);
        	centerPanel.add(buttonPanel, BorderLayout.CENTER);
        	centerPanel.add(outputPanel, BorderLayout.SOUTH);
    		add("Center", centerPanel);
        	setVisible(false);
    		setVisible(true);
    	}
    	else if (action.equals("Tell Knowledge-Base")) {
    		output.setText(" ");
    		Logic log = new Logic("");
    		try {
    			log = Logic.parse(expression.getText());
    		}
    		catch(IllegalLogicClause ilc) {
    			output.setText("Illegal logic clause");
    			return;
    		}
    		if(propLogicButton.getBackground() == metalColor) propKB.tell(log);
    		else hornKB.tell(log);
    	}
    	else if (action.equals("Ask Knowledge-Base")) {
    		output.setText(" ");
    		Logic log = new Logic("");
    		try{
    			log = Logic.parse(expression.getText());
    		}
    		catch(IllegalLogicClause ilc) {
    			output.setText("Illegal logic clause");
    			return;
    		}
    		if(propLogicButton.getBackground() == metalColor) {
    			if (propKB.ask(log)) output.setText("True");
    			else output.setText("False");
    		}
    		else {
    			Vector answers =  hornKB.ask(log);
    	        Vector vars = new Vector();
    	        log.variablesIn(vars);
    	        if (succeeded(answers)) 
    	        	if (vars.size()==0) output.setText("Yes" + '\n');
    	        	else
    	        		for (int i=0; i<answers.size(); i++) {
    	        			Hashtable ht = (Hashtable)answers.elementAt(i);
    	        			if (!ht.containsKey("fail")) {
    	        				for (int j=0; j<vars.size(); j++) {
    	        					String varname = (String)vars.elementAt(j);
    	        					output.append( varname + ": " + getBinding(ht,varname) + " ");
    	        				}
    	        				output.append("" +'\n' + " ");    
    	        			} 
    	        		}
    	        else
    	        output.append( "Fail" + '\n');
    		}
    	} 
    	else if (action.equals("Clear Knowledge-Base")) {
    		if(propLogicButton.getBackground() == metalColor) {
    			propKB.sentence = null;
    			propKB.listModel.clear();
    		}
    		else {
    			 hornKB.listModel.clear();
    			 hornKB.table.clear();
    			 hornKB.variableCounter = 0;
    		}
    		output.setText(" ");
    	} 
    	else if (action.equals("Delete Knowledge-Base Entry")) {
    		if(propLogicButton.getBackground() == metalColor) {
    			int index = propKB.sentenceList.getSelectedIndex();
    			if (index >=0) {
        			propKB.listModel.removeElementAt(index);
        			propKB.sentence.operands.removeElementAt(index);
        			if (propKB.sentence.operands.size() == 0) propKB.sentence = null;
        		}
    		}
    		else {
    			int index = hornKB.sentenceList.getSelectedIndex();
    	        if (index >=0) {
    	        	Logic log = new Logic("");
    	        	try {
    	        		log = Logic.parse((String)hornKB.sentenceList.getSelectedValue());
    	        	}
    	        	catch(Exception ex) {};
    	        	if (log.value.equals("->")) log = (Logic)log.operands.elementAt(1);
    	        	Vector match = (Vector)hornKB.table.get(log.value);
    	        	int pos = ((Integer)hornKB.linetable.elementAt(index)).intValue();
    	        	match.removeElementAt(pos);
    	        	hornKB.listModel.removeElementAt(index);
    	        }
    		}
    		output.setText(" ");
    	}  
    	else if (action.equals("Display Truth-Table")) {
    		output.setText(" ");
    		Logic log = new Logic("");
    		try {
    			log = Logic.parse(expression.getText());
    		}
    		catch(IllegalLogicClause ilc) {
    			output.setText("Illegal Logic Clause");
    			return;
    		}
    		TruthTable tt = new TruthTable(log);
    		tt.display();
    	}
    	else if (action.equals("Check Validity")) {
    		Logic log = new Logic("");
    		try {
    			log = Logic.parse(expression.getText());
    		}
    		catch(IllegalLogicClause ilc) {
    			output.setText("Illegal logic clause");
    			return;
    		}
    		if (log.isValid()) output.setText("Valid");
    		else if (log.isSatisfiable()) output.setText("Satisfiable");
    		else output.setText("Not satisfiable");
    	}
    	else if (action.equals("Open Knowledge-Base")) {
    		try {
    			JFileChooser chooser = new JFileChooser();
    			ExtensionFileFilter filter = new ExtensionFileFilter();
    			if(propLogicButton.getBackground() == metalColor) {
    				filter.addExtension("prop");
    				filter.setDescription("Propositional Logic Database");
    			}
    			else {
    				filter.addExtension("horn");
    		        filter.setDescription("First-Order Logic Database");
    			}
    			chooser.setFileFilter(filter);
    			int returnVal = chooser.showOpenDialog(this);
    			if (returnVal == JFileChooser.APPROVE_OPTION) {
    				String path = chooser.getSelectedFile().getPath();
    				String name = chooser.getSelectedFile().getName();
    				if (name.endsWith(".prop")) {
    					BufferedReader in = new BufferedReader(new FileReader(path));              
    					String s = in.readLine();
    					propKB.sentence = null;
    					propKB.listModel.clear();
    					output.setText(" ");
    					while(s != null) {
    						try {
    							propKB.tell(Logic.parse(s));
    						} catch(IllegalLogicClause ilc){}
    						s = in.readLine();
    					}
    				}
    				else if (name.endsWith(".horn")){
    	                BufferedReader in = new BufferedReader(new FileReader(path));              
    	                String s = in.readLine();
    	                hornKB.listModel.clear();
    	                hornKB.table.clear();
    	                hornKB.variableCounter = 0;
    	                output.setText(" ");
    	                while(s != null) {
    	                  try {
    	                    hornKB.tell(Logic.parse(s));
    	                  } catch(IllegalLogicClause ilc){}
    	                    s = in.readLine();
    	                }
    	            }
    			} 
    		} catch(IOException ex) {}  
    	}
    	else if (action.equals("Save Knowledge-Base")) {
    		try {
    			JFileChooser chooser = new JFileChooser();
    			ExtensionFileFilter filter = new ExtensionFileFilter();
    			if(propLogicButton.getBackground() == metalColor) {
    				filter.addExtension("prop");
    				filter.setDescription("Propositional Logic Database");
    			}
    			else {
    				filter.addExtension("horn");
    		        filter.setDescription("First-Order Logic Database");
    			}
    			chooser.setFileFilter(filter);
    			int returnVal = chooser.showSaveDialog(this);
    			if (returnVal == JFileChooser.APPROVE_OPTION) {
    				if(propLogicButton.getBackground() == metalColor) {
    					String path = chooser.getSelectedFile().getPath();
    					String name = chooser.getSelectedFile().getName();
    					if (!name.endsWith(".prop")) path = path + ".prop";
    					FileWriter fw = new FileWriter(path);
    					int size = propKB.listModel.getSize();
    					for (int i=0; i<size; i++){
    						fw.write((String)propKB.listModel.getElementAt(i)+'\n');
    					}
    					fw.close();
    				}
    				else {
    					String path = chooser.getSelectedFile().getPath();
    		            String name = chooser.getSelectedFile().getName();
    		            if (!name.endsWith(".horn")) path = path + ".horn";
    		            FileWriter fw = new FileWriter(path);
    		            int size = hornKB.listModel.getSize();
    		            for (int i=0; i<size; i++){
    		                fw.write((String) hornKB.listModel.getElementAt(i)+'\n');
    		            }
    		            fw.close();
    				}
    			} 
    		} catch(IOException ex) {}
    	}    
    }
    boolean succeeded(Vector answers) {
    	for (int i=0; i<answers.size(); i++)
    		if ( !((Hashtable)answers.elementAt(i)).containsKey("fail")) return true;
    	return false;
    }
    String getBinding(Hashtable ht, String varname) {
    	Logic bind = (Logic) ht.get(varname);
    	if (bind.isVariable()) return getBinding(ht,bind.value);
    	else return bind.value;
    }
}
