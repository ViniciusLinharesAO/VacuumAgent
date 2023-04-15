package ai.logic;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import javax.swing.*;
/**
 * A Truth table.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class TruthTable
{
	/**
	 * Variables in the table
	 */
    Vector symbols;
	/**
	 * List of sentences
	 */
    Vector sentences;
	/**
	 * The table
	 */
    boolean rows[][];
	/**
	 * The number of rows
	 */
    int numRows;
    
    TruthTable(Logic sentence)
    {
	symbols = new Vector();
	sentences = new Vector();
	sentence.propSymbolsIn(symbols);
	for (int i=0; i<symbols.size(); i++)
	    sentences.addElement(new Logic((String)symbols.elementAt(i)));
	sentence.subSentencesIn(sentences);
	
	numRows = (int)(Math.pow(2.0, (double)symbols.size()));
	rows = new boolean[numRows][sentences.size()];
	int k = numRows;
	for (int j=0; j<symbols.size(); j++){
	    k = k / 2;
	    for (int i=0; i<numRows; i++)
		if ((i/k)%2 == 0)
		    rows[i][j] = true;
		else 
		    rows[i][j] = false;
	}
	
	for (int i=0; i<numRows; i++)
	    for (int j=symbols.size(); j<sentences.size(); j++)
		rows[i][j] = evalTruth(i,j);
	
		 
    }
    
    boolean evalTruth(int rowNum,int sentNum)
    {
	Hashtable h = new Hashtable();
	for (int i=0; i<symbols.size(); i++)
	    h.put((String)symbols.elementAt(i), new Boolean(rows[rowNum][i]));
	Logic sentence = (Logic)sentences.elementAt(sentNum);
	return evalTruth(sentence, h);
    }
    
    boolean evalTruth(Logic sentence, Hashtable h)
    { 
	if (sentence.isAtom())
	    if (sentence.value.equals("true")) return true;
	    else if (sentence.value.equals("false")) return false;
	    else
		return ((Boolean)h.get(sentence.value)).booleanValue();
	else 
	    if (sentence.value.equals("~"))
		return !evalTruth((Logic)sentence.operands.elementAt(0),h);
	    else if (sentence.value.equals("->"))
		return !evalTruth((Logic)sentence.operands.elementAt(0),h) ||
		       evalTruth((Logic)sentence.operands.elementAt(1),h);
	    else if (sentence.value.equals("<=>"))
		return evalTruth((Logic)sentence.operands.elementAt(0),h) ==
		       evalTruth((Logic)sentence.operands.elementAt(1),h);
	    else if (sentence.value.equals("|")) {
		boolean result = evalTruth((Logic)sentence.operands.elementAt(0),h);
		for (int i=1; i<sentence.operands.size(); i++)
		    result = result || evalTruth((Logic)sentence.operands.elementAt(i),h);
		return result;
	    }
	    else if (sentence.value.equals("&")) {
		boolean result = evalTruth((Logic)sentence.operands.elementAt(0),h);
		for (int i=1; i<sentence.operands.size(); i++)
		    result = result && evalTruth((Logic)sentence.operands.elementAt(i),h);
		return result;
	    }
	    return false;
    }
    
    boolean valid()
    {
	for (int i=0; i<numRows; i++)
	    if (!rows[i][sentences.size()-1]) return false;
	return true;
    }
    
    boolean satisfiable()
    {
	for (int i=0; i<numRows; i++)
	    if (rows[i][sentences.size()-1]) return true;
	return false;
    }
    
    void display()
    {
	JTextArea results = new JTextArea();
	results.setFont(new Font("Monospaced",Font.PLAIN,12));
	results.setEditable(false);
	results.append(""+'\n' + " ");
	int width = 0;
	for (int j=0; j<sentences.size(); j++){
	    String sent = ((Logic)sentences.elementAt(j)).toString();
	    width += sent.length() + 3;
	    results.append(sent + "   ");
	}
	results.append('\n'+print_repeated("_",width)+'\n');
	    
	for (int i=0; i<numRows; i++){
	    for (int j=0; j<sentences.size(); j++){
		String sent = ((Logic)sentences.elementAt(j)).toString();
		String t = "F";
		if (rows[i][j]) t = "T";
		results.append(print_centered(t,sent.length()+3));
	    }
	    results.append(""+'\n');
	}
	
	JFrame f = new JFrame("Truth Table");
	JPanel p = new JPanel();
	JLabel title = new JLabel("Truth Table");
	title.setFont(new Font("SansSerif",Font.ITALIC+Font.BOLD,14));
	p.setLayout(new BorderLayout());
	p.setBackground(Color.white);
	p.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createEtchedBorder(),
		BorderFactory.createEmptyBorder(5,10,10,10)));
	p.add("North",title);
	p.add("Center",results);
	f.getContentPane().add(p);
	f.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		e.getWindow().dispose();
	    }
	});
	f.pack();
	f.setLocation(10,10);
	f.setVisible(true);
    }
    
    String print_repeated(String s, int n)
    {
	String s1 = new String(s);
	for (int i=1; i<n; i++)
	    s1 = s1 + s;
	return s1;
    }
    
    String print_centered(String s, int n)
    {
	int numBlanks = n - s.length();
	return print_repeated(" ", numBlanks/2) + s + 
	       print_repeated(" ", (int)Math.ceil(numBlanks/2.0));
    }
		
}