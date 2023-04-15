package ai.logic;

// A Logic object represents a logic statement
import java.util.Vector;
import java.util.StringTokenizer;
/**
 * A logic statement.
 * @author Jill Zimmerman -- jill.zimmerman@goucher.edu
 *
 */

public class Logic implements Cloneable 
{

    String value;
    Vector operands;
    
    public Logic(String v)
    {
	value = v;
	operands = new Vector();
    }
    
    boolean isAtom()
    {
	return Character.isLetter(value.charAt(0)) || value.charAt(0)=='$';
    }
    
    boolean isVariable()
    {
	return value.charAt(0)=='$';
    }
    
	/**
	 * Determines if logic statement is valid
	 */
    public boolean isValid()
    {
	TruthTable tt = new TruthTable(this);
	return tt.valid();
    }
    
	/**
	 * Determines if logic statement is satisfiable
	 */
    public boolean isSatisfiable()
    {
	TruthTable tt = new TruthTable(this);
	return tt.satisfiable();
    }
    
    void propSymbolsIn(Vector symbols)
    {
	if (isAtom() && !value.equals("true") && !value.equals("false"))
	    addString(symbols,value);
	else
	    for (int i=0; i<operands.size(); i++) {
		Logic sentence = (Logic)operands.elementAt(i);
		sentence.propSymbolsIn(symbols);
	    }
    }
    
    void variablesIn(Vector variables)
    {
	if (isVariable())
	    addString(variables,value);
	else
	    for (int i=0; i<operands.size(); i++) {
		Logic sentence = (Logic)operands.elementAt(i);
		sentence.variablesIn(variables);
	    }
    }
    
    boolean hasVariable(String varName)
    {
	Vector vars = new Vector();
	variablesIn(vars);
	for (int i=0; i<vars.size(); i++)
	    if (varName.equals((String)vars.elementAt(i))) return true;
	return false;
    }
	    
    
    void subSentencesIn(Vector subSent)
    {
	if (!isAtom()) { 
	    for (int i=0; i<operands.size(); i++)
		((Logic)operands.elementAt(i)).subSentencesIn(subSent);
	    subSent.addElement(this);
	}    
    }
    
    public String toString()
    {
	if (operands.size()==0)
	    return value;
	else if (value.equals("~")) {
	    Logic oper = (Logic)operands.elementAt(0);
	    if (oper.isAtom())
		return "~" + oper.toString() ;
	    else
		return "~(" + oper.toString() + ")";
	}
	else {
	    if (Character.isLetter(value.charAt(0))) { // predicate
		String s = value + "(" + 
		    ((Logic)operands.elementAt(0)).toString();
		for (int i=1; i<operands.size(); i++) {
		    Logic oper = (Logic)operands.elementAt(i);
		    s = s + "," + oper.toString();
		}
		return s + ")";
	    }
	    
	    // operator
	    String s =   ((Logic)operands.elementAt(0)).toString();
	    for (int i=1; i<operands.size(); i++) {
		Logic oper = (Logic)operands.elementAt(i);
		if (Logic.precedence(value) < 
		    Logic.precedence(oper.value))
		    s = s +  " " + value +  " " + oper.toString();
		else 
		    s = s + " " + value + " (" + oper.toString() + ")";
	    }
	    return s ;
	}
    }
    
    void addString(Vector v, String value)
    {
	for (int i=0; i<v.size(); i++) {
	    String s = (String) v.elementAt(i);
	    if (s.equals(value)) return;
	}
	v.addElement(value);
    }
    
	/**
	 * Parse an expression string
	 * @return a logic object representing that string
	 */
    public static Logic parse(String expr) throws IllegalLogicClause
    {
	StringTokenizer st = new StringTokenizer(expr," ~()|&<-",true);
	return parse(st,new Vector(), new Vector(),null);
    }
    
    public static Logic parse(StringTokenizer expr, Vector operators,
		 Vector operands, String lookahead) throws IllegalLogicClause
    {
	String t;
	String token;
	if (! expr.hasMoreTokens())
	    if (operators.size() == 0)
		return (Logic)operands.elementAt(0);
	    else return handleOp(null,expr,operators,operands);
	else {
	    if (lookahead == null)  
		 token = expr.nextToken(" ~()|&<-=");
	    else {
		 token = lookahead;
		 lookahead = null;
	    }
	    if (Character.isWhitespace(token.charAt(0)))
		return parse(expr,operators,operands,lookahead);
	    if (Character.isLetter(token.charAt(0)) ||
		token.charAt(0)=='$') {// atom
		Logic atom = new Logic(token);
		operands.insertElementAt(atom,0);
		if (expr.hasMoreTokens()) {
		    do {
			lookahead = expr.nextToken(" ~()|&<-=");
		    }
		    while (expr.hasMoreTokens() && Character.isWhitespace(lookahead.charAt(0)));
		    if (lookahead.equals("(")) { // atom is name of predicate
			token = expr.nextToken(")"); // gather parenthesized operands
			lookahead = null;
			t = expr.nextToken(" ~()|&<-="); // skip )

			StringTokenizer e = new StringTokenizer(token," ,",false);
			handleOperands(atom,e);
		    }
		}
		return parse(expr,operators, operands,lookahead);
	    }
	    else if (token.equals("(")) { // parenthesized group
		token = expr.nextToken(")"); // gather parenthesized expression
		t = expr.nextToken(); // skip )
		StringTokenizer e = new StringTokenizer(token," ~()|&<-=",true);
		Logic log = parse(e,new Vector(),new Vector(),lookahead);
		operands.insertElementAt(log,0);
		return parse(expr,operators,operands,lookahead);
	    }
	    else if (token.equals("~") || token.equals("|") || token.equals("&") ||
		     token.equals("<") || token.equals("-") || token.equals("=") ) { // operator
		     if (token.equals("<")){
			t = expr.nextToken(" ~$(abcdefghijklmpnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
			if(!(t.equals("=>")))
			    throw new IllegalLogicClause("unknown operator <" + t);
			else token = "<=>";
		     }
		     if (token.equals("-")){
			t = expr.nextToken(" ~$(abcdefghijklmpnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
			if (!(t.equals(">")))
			    throw new IllegalLogicClause("unknown operator -" + t);
			else token = "->";
		     }
		     if (operators.size() == 0 || token.equals("~") || 
			(precedence(token) > 
			 precedence((String)operators.elementAt(0)))) {
			operators.insertElementAt(token,0);
			return parse(expr,operators,operands,lookahead);
		     }
		     else 
			return handleOp(token,expr,operators,operands);
	    }
	    else 
		throw new IllegalLogicClause("unknown symbol " + token);  
	}   
    }
    
    static Logic handleOp(String op, StringTokenizer expr, Vector operators,
	 Vector operands) throws IllegalLogicClause
    {
	String op1 = (String)operators.elementAt(0);
	operators.removeElementAt(0);
	Logic log = new Logic(op1);
	Logic oper1, oper2;
	if (op1.equals("~")) {
	    oper1 = (Logic)operands.elementAt(0);
	    operands.removeElementAt(0);
	    log.operands.addElement(oper1);
	}
	else {
	    if (operands.size()>1) {
		oper1 = (Logic)operands.elementAt(0);
		oper2 = (Logic)operands.elementAt(1);
		operands.removeElementAt(1);
		operands.removeElementAt(0);
		log.operands.addElement(oper2);
		log.operands.addElement(oper1);
	    }
	    else throw new IllegalLogicClause("insufficient number of operands");
	}
	operands.insertElementAt(log,0);
	
	if (op == null) 
	    return parse(expr,operators,operands,null);
	else if (operators.size() == 0 || 
			(precedence(op) > 
			 precedence((String)operators.elementAt(0)))){
	    operators.insertElementAt(op,0);
	    return parse(expr,operators,operands,null);
	}
	else 
	    return handleOp(op,expr,operators,operands);
    }
    
    
    static int precedence(String op)
    {
	if (op.equals("<=>")) return 1;
	else if (op.equals("->")) return 2;
	else if (op.equals("|")) return 3;
	else if (op.equals("&")) return 4;
	else if (op.equals("=")) return 5;
	else return 5;
    }
    
    static void handleOperands(Logic atom, StringTokenizer operands) throws IllegalLogicClause
    {
	while (operands.hasMoreTokens()) {
	    String token = operands.nextToken();
	    atom.operands.addElement(new Logic(token));
	}
    }
    
    public Object clone()
    {
	String val = new String(value);
	Logic log = new Logic(val);
	for (int i=0; i<operands.size(); i++)
	    log.operands.addElement(((Logic)operands.elementAt(i)).clone());
	return log;
    }
	   
}


