package market.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* marketPanel holds 2 panels
     * 1) the staff listing, and lists of current customers all constructed
     *    in MarketPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private MarketPanel marketPanel = new MarketPanel(this);
    
    public static final int WINDOWX = 1000;
    public static final int WINDOWY = 550;
    private static final int MARGIN = 100;
    private static final int WIDTH = 50;

    /**
     * Constructor for MarketGui class.
     * Sets up all the gui components.
     */
    public MarketGui() {
        setBounds(WIDTH, WIDTH , WINDOWX, WINDOWY);
        setVisible(true);
    	
    }
    
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
    
}
