package ai.logic;


import javax.swing.*;
/**
 * A Knowledge base of Propositional logic.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class PropKnowledgeBase
{
    Logic sentence;
    JList sentenceList;
    DefaultListModel listModel = new DefaultListModel(); 
    
    PropKnowledgeBase()
    {
	sentenceList = new JList(listModel);
    }
    
	/**
	 * Add a statement to the knowledge base
	 */
    public void tell(Logic statement)
    {
	if (sentence == null) {
	    sentence = new Logic("&");
	}
	sentence.operands.addElement(statement);
	listModel.addElement(statement.toString());
    }
    
	/**
	 * Query the knowledge base
	 */
    public boolean ask(Logic statement)
    {
	Logic query = new Logic("->");
	if (sentence!=null)
	    query.operands.addElement(sentence);
	else
	    query.operands.addElement(new Logic("true"));
	query.operands.addElement(statement);
	return query.isValid();
    }    	    
}
