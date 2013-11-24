package mainGUI;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurantMQ.gui.RestaurantGui;
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
	private int WIDTH = 1230;
	private int HEIGHT = 800;
	
	//public AnimationPanel animationPanel;
	//JPanel InfoLayout;
	
	private ConfigParser parser;
    private PersonCreationPanel personPanel;
    private MainAnimationPanel mainAnimationPanel;
   
    public MarketGui marketGui1;
    public RestaurantGui restaurantGui1;
    
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
    	
    	setBounds(0, 0, WIDTH, HEIGHT);
    	setLocation(50, 50);
        
        setLayout(new GridLayout(0,2));
        
        
       
        add (personPanel);
        add (mainAnimationPanel);
      
        
        addMouseListener(this);
        
        // add gui for all buildings, set the guis to invisible initially
        marketGui1 = new MarketGui();
        marketGui1.setTitle("Market1");
        marketGui1.setVisible(false);
        marketGui1.setResizable(false);
        marketGui1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        restaurantGui1 = new RestaurantGui();
        restaurantGui1.setTitle("RestaurantMQ");
        restaurantGui1.setVisible(false);
        restaurantGui1.setResizable(false);
        restaurantGui1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
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
		PersonAgent p = new PersonAgent(name, this);
		PersonGui personGui = new PersonGui(p, this);
		mainAnimationPanel.addGui(personGui);
		p.setGui(personGui);
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