package mainGUI;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurantMQ.gui.RestaurantGui;
import market.gui.MarketGui;
import city.Building;
import city.HomeGui;
import city.PersonAgent;
import city.PersonAgent.BigState;
import city.gui.PersonGui;
import config.ConfigParser;
import bankgui.*;

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
    public MainAnimationPanel mainAnimationPanel;
   
    public MarketGui marketGui;
    public RestaurantGui restaurantGuis[] = {null, null, null, null, null, null};
    public BankGui bankGui;
    //public BusStopGui busStopGui will have a list of these and add them all
    
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
    	
    	ArrayList<HashMap<String,String>> configPeople;
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
        marketGui = new MarketGui();
        marketGui.setTitle("Market");
        marketGui.setVisible(false);
        marketGui.setResizable(false);
        marketGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        for (int i = 0; i < 6; i++) {
        	restaurantGuis[i] = new RestaurantGui();
        	restaurantGuis[i].setTitle("RestaurantMQ");
        	restaurantGuis[i].setVisible(false);
        	restaurantGuis[i].setResizable(false);
        	restaurantGuis[i].setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        
        bankGui = new BankGui();
        bankGui.setTitle("Bank");
        bankGui.setVisible(false);
        bankGui.setResizable(false);
        bankGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        try {    	
			parser = new ConfigParser(this,"src/config/config.txt");
			configPeople = parser.ParseAndCreatePeople();
			for(HashMap<String,String> person: configPeople) {
	        	addConfigPerson(person);
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
  
    public static void main(String[] args) { 
        MainGui gui = new MainGui();
        gui.setTitle("Sim City - Team 15");
        gui.setVisible(true);
        gui.setResizable(true);
    }
    
    public void addPerson(String name, String role) {
		PersonAgent p = new PersonAgent(name, this, mainAnimationPanel.cd);
		mainAnimationPanel.cd.addPerson(p);
		PersonGui personGui = new PersonGui(p, this);
		mainAnimationPanel.addGui(personGui);
		p.setGui(personGui);
		p.bigState = BigState.goHome;
		p.startThread();
		
	}
    public void addConfigPerson(HashMap<String,String> properties) {
		/* THIS WILL ADD IN ALL PROPERTIES IN THIS HASHMAP TO PERSON ATTRIBUTES
		 * PersonAgent p = new PersonAgent(, this, mainAnimationPanel.cd);
		mainAnimationPanel.cd.addPerson(p);
		PersonGui personGui = new PersonGui(p, this);
		mainAnimationPanel.addGui(personGui);
		p.setGui(personGui);
		p.personState = state.goHome;
		p.startThread();
		
		*/
		
	}
    
    @Override
    public void mouseClicked(MouseEvent e) {
    	//Check to see which building was clicked
    	for (int i = 0; i < mainAnimationPanel.cd.buildings.size(); i++) {
    		Building b = mainAnimationPanel.cd.buildings.get(i);
    		
    		if (b.contains(e.getX()-620, e.getY()-25)) {
    			System.out.print("Building " + i + " clicked\n");
    			b.display(b, i);
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