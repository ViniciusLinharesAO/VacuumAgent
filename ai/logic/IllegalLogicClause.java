package ai.logic;


class IllegalLogicClause extends Exception
{
    public IllegalLogicClause()
    {	
	super();
    }
    
    public IllegalLogicClause(String s)
    {
	super(s);
    }
}