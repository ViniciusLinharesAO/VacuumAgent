package ai.logic;

import java.util.Vector;

import java.util.Hashtable;
import javax.swing.*;
/**
 * A Knowledge base of Horn clauses.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */
public class HornKnowledgeBase
{
	/**
	 * A table of sentences indexed by the inferred clause
	 */
    public Hashtable table;
    JList sentenceList;
    DefaultListModel listModel = new DefaultListModel();
    int variableCounter;
    Vector linetable;
    
    public HornKnowledgeBase()
    {
	table = new Hashtable();
	sentenceList = new JList(listModel);
	variableCounter = 0;
	linetable = new Vector();
    }
    
	/**
	 * Add a statement to the knowledge base
	 */
    public void tell(Logic statement)
    {
	Logic rhs = statement;
	Vector match;
	if (rhs.value.equals("->")) 
	    rhs = (Logic)statement.operands.elementAt(1);
	if (table.containsKey(rhs.value)){
	    match = (Vector)table.get(rhs.value);
	    match.addElement(statement);
	    linetable.addElement(new Integer(match.size()-1));
	}
	else {
	    match = new Vector();
	    match.addElement(statement);
	    table.put(rhs.value,match);
	    linetable.addElement(new Integer(match.size()-1));
	}
	listModel.addElement(statement.toString());
    }
    
	/**
	 * Query the knowledge base
	 * This uses the backChain algorithm to return a list of answers
	 */
    public Vector ask(Logic query)
    {
	Vector variables = new Vector();
	query.variablesIn(variables);
	Vector goals = new Vector();
	getClauses(query,goals);
	return backChain(goals,new Hashtable());
    }
    
    void getClauses(Logic query, Vector v)
    {
	if (query.isAtom() || query.value.equals("=") || query.value.equals("~"))
	    v.insertElementAt(query,0);
	else if (query.value.equals("&"))
	    for (int i=query.operands.size()-1; i>=0; i--)
		getClauses((Logic)query.operands.elementAt(i), v);
    } 
    
    Vector backChain(Vector goals, Hashtable bindings)
    {
	Vector answer = new Vector();
	if (bindings.containsKey("fail") || goals.size()==0) {
	    answer.addElement(bindings);
	    return answer;
	}
	else {
	    Logic goal = (Logic)goals.elementAt(0);
	    goals.removeElementAt(0);
	    if (goal.value.equals("false")){
		bindings.put("fail", "");
		answer.addElement(bindings);
		return answer;
	    }
	    else if (goal.value.equals("true"))	
		return backChain(goals,bindings);
	    else if (goal.value.equals("=")){
		Logic x1 = (Logic)goal.operands.elementAt(0);
		Logic x2 = (Logic)goal.operands.elementAt(1);
		unify(x1,x2,bindings);
		return backChain(goals,bindings);
	    }
	    else if (goal.value.equals("&")){
		getClauses(goal,goals);
		return backChain(goals,bindings);
	    }
	    else if (goal.value.equals("~")){
		Vector clause = new Vector();
		clause.addElement(goal.operands.elementAt(0));
		Vector refute = backChain(clause,bindings);
		if (succeeded(refute)){
		    bindings.put("fail", "");
		    answer.addElement(bindings);
		    return answer;
		}
		else {
		    return backChain(goals,bindings);
		}
	    }    
		
	    else {// look at all clauses the conclude the goal
		Hashtable b = new Hashtable();
		Logic clause = new Logic("");
		Vector clauses = (Vector)table.get(goal.value);
		Vector gls = new Vector();
		
		if (clauses !=null) {
		    for (int i=0; i<clauses.size(); i++) {
			Logic c = (Logic)clauses.elementAt(i);
			try{clause = (Logic)c.clone(); }
			//try{clause = ((Logic)clauses.elementAt(i)).clone(); }
			catch(Exception e) {System.out.println("Can't clone clause");};
			try { gls = (Vector)goals.clone(); }
			catch (Exception e) {System.out.println("Can't clone goals"); };
			try{b = (Hashtable)bindings.clone();}
			catch(Exception e){};
			renameVariables(clause);
			if (clause.value.equals("->")){
			    Logic x1 = (Logic) clause.operands.elementAt(0);
			    Logic x2 = (Logic) clause.operands.elementAt(1);
			    getClauses(x1,gls);
			    unify(goal,x2,b);
			    appendTo(answer, backChain(gls,b));
			}
			else {
			    
			    unify(goal,clause,b);
			    appendTo(answer, backChain(gls,b));
			}
		    }
		}
		return answer;
	    }
	}
    }
    
    
    void unify(Logic clause1, Logic clause2, Hashtable bindings)
    {
	if (bindings.containsKey("fail")) return;
	else if (clause1.isVariable())
	    unifyVar(clause1,clause2,bindings);
	else if (clause2.isVariable())
	    unifyVar(clause2,clause1,bindings);
	else if (clause1.value.equals(clause2.value) &&
		 clause1.operands.size() == clause2.operands.size())
	    for (int i=0; i<clause1.operands.size(); i++)
		unify((Logic)clause1.operands.elementAt(i),
		      (Logic)clause2.operands.elementAt(i), bindings);
	else
	    bindings.put("fail","");
    }
    
    void unifyVar(Logic variable, Logic clause, Hashtable bindings)
    {
	if (bindings.containsKey(variable.value))
	    unify((Logic)bindings.get(variable.value), clause, bindings);
	else if (clause.isVariable() && bindings.containsKey(clause.value))
	    unify(variable,(Logic)bindings.get(clause.value),bindings);
	else if (clause.hasVariable(variable.value))
	    bindings.put("fail","");
	else
	    bindings.put(variable.value,clause);
    }
    
    void renameVariables(Logic clause)
    { // Replace all variables in clause with new ones
	Hashtable t = new Hashtable();
	Vector vars = new Vector();
	clause.variablesIn(vars);
	for (int i=0; i<vars.size(); i++) {
	    String varname = (String)vars.elementAt(i);
	    t.put(varname, varname + "." + (variableCounter++));
	}
	renameVariables(clause,t);
    }
    
    void renameVariables(Logic clause, Hashtable t)
    {
	if(clause.isVariable())
	    clause.value = (String)t.get(clause.value);
	else
	    for (int i=0; i<clause.operands.size(); i++)
		renameVariables((Logic)clause.operands.elementAt(i),t);
    }
    
    void appendTo(Vector v1, Vector v2)
    {
	for (int i=0; i<v2.size(); i++)
	    v1.addElement(v2.elementAt(i));
    }
    
    boolean succeeded(Vector answers)
    {
	for (int i=0; i<answers.size(); i++){
	    Hashtable h = (Hashtable)answers.elementAt(i);
	    if ( !((Hashtable)answers.elementAt(i)).containsKey("fail"))
		return true;
	}
	return false;
    }  
}
