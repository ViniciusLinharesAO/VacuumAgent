package ai.worlds.wumpus;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.Vector;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ai.logic.ExtensionFileFilter;
import ai.worlds.Location;
import ai.worlds.Obj;
/**
 * A wumpus world editor which allows building a test worlds.
 * @author Jim Segedy
 *
 */

public class WumpusWorldEditor extends JPanel implements ActionListener
{
    WumpusGrid wumpgrid;
    public Object grid[][];
    public Wumpus w;
    
    private JPopupMenu popup;
    JCheckBoxMenuItem pitItem;
	JCheckBoxMenuItem wumpusItem;
	JCheckBoxMenuItem goldItem;
	
	private boolean hasWumpus = false;
	private boolean hasGold = false;
	
	Location clickPoint = new Location(-1, -1);
    
    
    public WumpusWorldEditor() {
    	grid = new Vector[6][6];
    	for(int i = 0; i < 6; i++) {
    		for(int j = 0; j < 6; j++) {
    			Vector v = new Vector();
    			grid[i][j] = v;
    		}
    	}
    	wumpgrid = new WumpusGrid();
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		popup = new JPopupMenu();
		pitItem = new JCheckBoxMenuItem("Pit");
		wumpusItem = new JCheckBoxMenuItem("Wumpus");
		goldItem = new JCheckBoxMenuItem("Gold");
		pitItem.addActionListener(this);
		wumpusItem.addActionListener(this);
		goldItem.addActionListener(this);
		popup.add(pitItem);
		popup.add(wumpusItem);
		popup.add(goldItem);
		
		MouseListener popupListener = new PopupListener(popup);
		wumpgrid.addMouseListener(popupListener);
		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		add("Center", wumpgrid);
    }
    
    public void actionPerformed(ActionEvent e) {
    	String command = e.getActionCommand();
    	if(command.equals("Pit")) {
    		if(contains((Vector) grid[clickPoint.x][clickPoint.y], (new Pit()).getClass())) {
    			Vector v = (Vector) grid[clickPoint.x][clickPoint.y];
    			for (int i=0; i<v.size(); i++) {
    				Obj obj = (Obj) v.elementAt(i);
    				if(obj instanceof Pit) {
    					removeObj(obj.loc, obj);
    				}
    			}
    		}
    		else fillLoc(clickPoint, (new Pit(clickPoint)).getClass());
    		wumpgrid.paint(wumpgrid.getGraphics());
    	}
    	else if(command.equals("Wumpus")) {
    		if(contains((Vector) grid[clickPoint.x][clickPoint.y], (new Wumpus()).getClass())) {
    			Vector v = (Vector) grid[clickPoint.x][clickPoint.y];
    			for (int i=0; i<v.size(); i++) {
    				Obj obj = (Obj) v.elementAt(i);
    				if(obj instanceof Wumpus) {
    					hasWumpus = false;
    					removeObj(obj.loc, obj);
    				}
    			}
    		}
    		else if(!hasWumpus) {
    			addObj(clickPoint,w = new Wumpus(clickPoint));
    			hasWumpus = true;
    		}
    		wumpgrid.paint(wumpgrid.getGraphics());
    	}
    	else if(command.equals("Gold")) {
    		if(contains((Vector) grid[clickPoint.x][clickPoint.y], (new Gold()).getClass())) {
    			Vector v = (Vector) grid[clickPoint.x][clickPoint.y];
    			for (int i=0; i<v.size(); i++) {
    				Obj obj = (Obj) v.elementAt(i);
    				if(obj instanceof Gold) {
    					hasGold = false;
    					removeObj(obj.loc, obj);
    				}
    			}
    		}
    		else if(!hasGold) {
    			addObj(clickPoint, new Gold(clickPoint));
    			hasGold = true;
    		}
    		wumpgrid.paint(wumpgrid.getGraphics());
    	}
    }
    
    public void save() {
    	JFileChooser chooser = new JFileChooser();
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("ww");
		filter.setDescription("Wumpus World");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getPath();
			String name = chooser.getSelectedFile().getName();
			if (!name.endsWith(".ww")) path = path + ".ww";
			try {
				FileWriter fw = new FileWriter(path);
				for (int i=4; i>=1; i--){
					for(int j=1; j<=4; j++) {
						boolean P = false, W = false, G = false;
						if(contains((Vector) grid[j][i], (new Pit()).getClass())) P = true;
						if(contains((Vector) grid[j][i], (new Wumpus()).getClass())) W = true;
						if(contains((Vector) grid[j][i], (new Gold()).getClass())) G = true;
						if(P) {
							if(W) {
								if(G) fw.write("A ");
								else fw.write("X ");
							}
							else if(G) fw.write("Y ");
							else fw.write("P ");
						}
						else if(W) {
							if(G) fw.write("Z ");
							else fw.write("W ");
						}
						else if(G) fw.write("G ");
						else fw.write("N ");
					}
					fw.write("\n");
				}
				fw.close();
			} catch(Exception exception){};
		}
    }
    
    public Location pixelsToLoc(int x, int y) {
    	if((x >=25) && (x <=225) && (y >= 25) && (y <= 225)) {
    		x -= wumpgrid.startx;
    		y -= wumpgrid.starty;
    		x /= wumpgrid.cellSize;
    		y /= wumpgrid.cellSize;
    		x++;
    		y = 4-y;
    		return new Location(x, y);
    	}
    	return new Location(0,0);
    }
    public void addObj(Location loc, Obj ob)
	// add object ob at location loc
	{
		Vector v = (Vector)grid[loc.x][loc.y];
		v.addElement(ob);
		ob.loc=loc;
	}
    public void fillLoc(Location loc, Class c) {
		try {
			addObj(loc, (Obj) c.newInstance());
		} catch(Exception e) {System.out.println(e);}
	}
    public boolean contains(Vector v, Class c)
	// Vector v contains an instance of class c
	{
	    boolean holds = false;
	    for (int i=0; i<v.size(); i++) 
		if ( (c.isInstance(v.elementAt(i))) )
		    holds = true;
	    return holds;
	}
    
    public void removeObj(Location loc, Obj ob)
	// remove object ob from grid at location loc
	{
		Vector v = (Vector)grid[loc.x][loc.y];
		v.removeElement(ob);
	}
    
    class WumpusGrid extends Canvas {
    	int cellSize = 50;
    	int startx = 25;
    	int starty = 25;
    	int endx, endy;
    	int numCols, numRows;
        
    	public WumpusGrid() {
    	    numRows = 4;
    	    numCols = 4;
    	    endx = startx + numCols*cellSize;
    	    endy = starty + numRows*cellSize;
    	    Color metalColor = MetalLookAndFeel.getDesktopColor();
    	    setBackground(metalColor);
    	    setSize(400,400);
    	}
        
    	public void paint(Graphics g) {
    	    g.setColor(Color.white);
    	    g.fillRect(startx, starty, numCols*cellSize, numRows*cellSize);
    	
    	    // draw row and col lines
    	    g.setColor(Color.black);
    	    for (int i=0; i<=numCols; i++)
    		g.drawLine(startx+i*cellSize, starty, startx+i*cellSize, endy);
    	    for (int i=1; i<=numCols; i++)
    		g.drawString(Integer.toString(i),startx+i*cellSize-cellSize/2, endy+15);
    	    for (int i=0; i<=numRows; i++)
    		g.drawLine(startx, starty+i*cellSize, endx, starty+i*cellSize);
    	    for (int i=0; i<numRows; i++)
    		g.drawString(Integer.toString(numRows-i), startx-15, 
    		    starty+(i+1)*cellSize-cellSize/2);
    	    // draw grid objects   
    	    for (int i=0; i<numCols + 2; i++)
    		for (int j=0; j<numRows + 2; j++){
    		    Vector v = (Vector) grid[i][j];
    		    for (int k=0; k<v.size(); k++) {
    			((Obj)v.elementAt(k)).draw(g, screenpos(i,j),
    						    cellSize);
    		}
    		}
    	}
        
    	Point screenpos(int x, int y) {
    	    return new Point(startx+cellSize*(x-1), endy-cellSize*y);
    	}
        
    	void addtext(String text, int x, int y, int pos) {
    	    Graphics g = getGraphics();
    	    g.setColor(Color.black);
    	    Point p = screenpos(x,y);
    	    g.drawString(text, p.x + 10, p.y + (pos+1)*10 + 5);
    	}
    }
    class PopupListener extends MouseAdapter {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
        	if(e.getButton() == MouseEvent.BUTTON3) {
            	clickPoint = pixelsToLoc(e.getX(), e.getY());
            	if(contains((Vector) grid[clickPoint.x][clickPoint.y], (new Pit()).getClass())) pitItem.setState(true);
            	else pitItem.setState(false);
            	if(contains((Vector) grid[clickPoint.x][clickPoint.y], (new Wumpus()).getClass())) wumpusItem.setState(true);
            	else wumpusItem.setState(false);
            	if(contains((Vector) grid[clickPoint.x][clickPoint.y], (new Gold()).getClass())) goldItem.setState(true);
            	else goldItem.setState(false);
            	if(clickPoint.x!=1 || clickPoint.y!=1) pitItem.setEnabled(true);
            	else pitItem.setEnabled(false);
            	if((hasWumpus && contains((Vector) grid[clickPoint.x][clickPoint.y], (new Wumpus()).getClass())) || !hasWumpus && (clickPoint.x!=1 || clickPoint.y!=1) ) wumpusItem.setEnabled(true);
            	else wumpusItem.setEnabled(false);
            	if((hasGold && contains((Vector) grid[clickPoint.x][clickPoint.y], (new Gold()).getClass())) || !hasGold && (clickPoint.x!=1 || clickPoint.y!=1)) goldItem.setEnabled(true);
            	else goldItem.setEnabled(false);
                maybeShowPopup(e);
        	}
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
}
