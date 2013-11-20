package mainGUI;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import market.gui.MarketGui;
import city.Building;
import city.PersonAgent;
import city.gui.PersonGui;
import config.ConfigParser;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MainGui extends JFrame implements MouseListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
	//public AnimationPanel animationPanel;
	//JPanel InfoLayout;
	
	private ConfigParser parser;
    private PersonCreationPanel personPanel;
    private MainAnimationPanel mainAnimationPanel;
   
    public MarketGui marketGui1;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public MainGui() {
    	    	
    	//1. create person
    	/*
    	try {
			parser = new ConfigParser(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("what");
		}
    	parser.ParseAndCreatePeople();
    	*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainAnimationPanel = new MainAnimationPanel(this);
        mainAnimationPanel.setVisible(true);
        personPanel = new PersonCreationPanel(this);
        personPanel.setVisible(true);
    	
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setBounds(0,0,screenSize.width-200, screenSize.height-100);
    	setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
        
        setLayout(new GridLayout(0,2));
        
        
       
        add (personPanel);
        add (mainAnimationPanel);
      
        
        addMouseListener(this);
        
        
        marketGui1 = new MarketGui();
        marketGui1.setTitle("Market 1");
        marketGui1.setVisible(false);
        marketGui1.setResizable(false);
        //marketGui1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
  
    public static void main(String[] args) {
    	
    	/*RestaurantGui restGui = new RestaurantGui();
        restGui.setTitle("csci201 Restaurant");
        restGui.setVisible(true);
        restGui.setResizable(false);
        restGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        restGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
    	
    	
         
        MainGui gui = new MainGui();
        gui.setTitle("Sim City - Team 15");
        gui.setVisible(true);
        gui.setResizable(true);        

        
       
    }
    
    public void addPerson(String name, String role) {
		PersonAgent p = new PersonAgent(name);
		mainAnimationPanel.addGui(new PersonGui(p));
		p.startThread();
	}
    
    @Override
    public void mouseClicked(MouseEvent e) {
    	//Check to see which building was clicked
    	for (int i = 0; i < mainAnimationPanel.buildings.size(); i++) {
    		Building b = mainAnimationPanel.buildings.get(i);
    		if (b.contains(e.getX()-620, e.getY()-25)) {
    			System.out.print("Building " + i + " clicked\n");
    			b.display(b);
    		}
    	}
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    	// TODO Auto-generated method stub	
    	
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    	// TODO Auto-generated method stub
	
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    	// TODO Auto-generated method stub
	
    }

    @Override
    public void mouseExited(MouseEvent e) {
    	// TODO Auto-generated method stub
	
    }
}