package ai.worlds.wumpus;

import java.util.Vector;

/**
 * A wumpus agent which can be used to test your knowledge base. 
 * @author Jim Segedy
 *
 */

public class LogicTestingAgent extends WumpusAgent {
	/**
	 * Plans for the future :-P
	 */
	Vector plan = new Vector(); 
	
	int timesMakingPlan = 0;
    
    public LogicTestingAgent() {
    	super();
    	logic = new DefaultWumpusLogic(4, 4); //Logic
    }
    
	/**
	 * Determines the next action to be taken by the agent.
	 * The action is stored as a string in the field 'action'.
	 */
    public void determineAction() {
    	//What should we do next??
    	action = "";
    	Vector p = (Vector) percept;
    	logic.move(body.loc.x, body.loc.y); //logical move
    	if (!plan.isEmpty()) {
    		//if we have a plan, execute it
    		action = (String) plan.elementAt(0);
    		plan.removeElementAt(0);
    		update(p);
    	}
    	else {
    		switch(timesMakingPlan) {
    			case 0: // move to (2,1)
    				    plan.addElement("turn left");
    				    plan.addElement("forward");
    					break;
    			case 1: //move to (1,2);
    				    plan.addElement("turn right");
       				    plan.addElement("turn right");   				    
    				    plan.addElement("forward");
       				    plan.addElement("turn left");
       				    plan.addElement("forward");
    					break;
    			case 2: // move to (1,1) and climb
   				    	plan.addElement("turn left");
   				    	plan.addElement("turn left");   				    
   				    	plan.addElement("forward");   				
    	    			plan.addElement("climb");
    	    			break;
    		}
    		timesMakingPlan++;
    		action = (String) plan.elementAt(0);
    		plan.removeElementAt(0);
    		update(p);
    	}
    }
    
	/**
	 * Update the knowledge base and the inferences about the world.
	 */
    private void update(Vector p) {
    	String b = "", s = "";
		if(p.elementAt(1)=="breeze") b = "breeze";
		if(p.elementAt(0)=="stench") s = "stench";
		logic.percept(s, b);
		logic.getStatus();
    }
}
