import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ai.worlds.WorldCreatePanel;
import ai.search.SearchPanel;
import ai.logic.LogicPanel;
/**
 * The main class for the CS340 AI software package
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */


public class AI
{
    public static void main(String[] args)
    {
	try {
	    UIManager.setLookAndFeel("MetalLookAndFeel");
	} catch (Exception e) {}
	
	JFrame f = new JFrame("Artificial Intelligence");
	f.setSize(1024, 750);
	f.setResizable(false);
	f.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	f.getContentPane().setLayout(new BorderLayout());
	f.getContentPane().add(new AIPanel(f) , BorderLayout.NORTH);
	f.setVisible(true); 
    }
}

class AIPanel extends JPanel {
	public static final Color buttonColor = MetalLookAndFeel.getTextHighlightColor();
	private JFrame holder;
	public JTabbedPane centerPanel = new JTabbedPane();
	private WorldCreatePanel wcp;
	private SearchPanel sp;
	private LogicPanel lp;
    public AIPanel(JFrame h) {
    	holder = h;
    	wcp = new WorldCreatePanel(holder);
    	sp = new SearchPanel(holder);
    	lp = new LogicPanel();
    	JPanel aboutPanel = createAboutPanel();
    	centerPanel.addTab("Agents and Environments", wcp);
    	centerPanel.addTab("Searches", sp);
    	centerPanel.addTab("Logic", lp);
    	centerPanel.addTab("About", aboutPanel);
    	setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	holder.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }
    private JPanel createAboutPanel() {
    	JPanel aboutPanel = new JPanel(new BorderLayout());
    	JTextPane aboutText = new JTextPane();
    	SimpleAttributeSet set = new SimpleAttributeSet();
    	StyleConstants.setAlignment(set,StyleConstants.ALIGN_CENTER);
    	aboutText.setParagraphAttributes(set,true);
    	aboutText.setText("Artificial Intelligence Software Package\nfor CS 340 at Goucher College\n\nWritten by Jill Zimmerman in 2000\nRefactored by Jim Segedy (jim.segedy@gmail.com) in 2006");
    	aboutText.setBackground(MetalLookAndFeel.getTextHighlightColor());
    	aboutText.setEditable(false);
		aboutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    	aboutPanel.add(aboutText, BorderLayout.CENTER);
    	return aboutPanel;
    }
}


