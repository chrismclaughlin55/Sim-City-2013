package mainGUI;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import restaurantMQ.gui.RestaurantGui;
import market.gui.MarketGui;
import city.Apartment;
import city.Building;
import city.Building.BuildingType;
import city.BusAgent;
import city.HomeGui;
import city.PersonAgent;
import city.PersonAgent.BigState;
import city.Room;
import city.gui.BusGui;
import city.gui.PersonGui;
import config.ConfigParser;
import Gui.Gui;
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
	
	private Scanner scan;
	private boolean fileExist;
    private PersonCreationPanel personPanel;
    public MainAnimationPanel mainAnimationPanel;
   
    public MarketGui marketGui;
    //public RestaurantGui restaurantGuis[] = {null, null, null, null, null, null};
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
        
        /*for (int i = 0; i < 6; i++) {
        	restaurantGuis[i] = new RestaurantGui();
        	restaurantGuis[i].setTitle("RestaurantMQ");
        	restaurantGuis[i].setVisible(false);
        	restaurantGuis[i].setResizable(false);
        	restaurantGuis[i].setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }*/
        
        bankGui.setTitle("Bank");
        bankGui.setVisible(false);
        bankGui.setResizable(false);
        bankGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
            	
        try {
    		scan = new Scanner( new File ("src/config/config.txt"));
    		fileExist=true;
    	}
    	catch(FileNotFoundException fnfe) {
    		fnfe.printStackTrace();
    	}
		if(fileExist) {
			while(scan.hasNextLine()) {
				String newPerson = scan.next();
				if(newPerson.equals("NewPerson")) {
					PersonAgent p = new PersonAgent(null, this, mainAnimationPanel.cd);
					mainAnimationPanel.cd.addPerson(p);
					PersonGui personGui = new PersonGui(p, this);
					mainAnimationPanel.addGui(personGui);
					p.setGui(personGui);
					for(int i=0; i<5; i++) {
						String property = scan.next();
						String temp = scan.next();
						if(property.equals("name")) {
							p.setName(temp);
						}
						if(property.equals("job")) { 
							p.setJob(temp);
							if(temp.equals("BankManager")) {
									
							}
							if(temp.equals("Host")) {
								//mainAnimationPanel.cd.
							}
							if(temp.equals("MarketManager")) {
								//if(mainAnimationPanel.cd.market)
								//mainAnimationPanel.cd.market.setManager(p);
							}		
						}
						if(property.equals("cash")) {
								p.setCash(Double.parseDouble(temp));
						}
						if(property.equals("bankMoney")) { 
							p.setBankMoney(Double.parseDouble(temp));
						}
						if(property.equals("hunger")) { 
							p.setHunger(Integer.parseInt(temp));
						}
					}
					p.startThread();
				 }
				 
			 }
		}
		
		//add bus agent
		BusAgent bus = new BusAgent(mainAnimationPanel.cd); 
		BusGui bg = new BusGui(bus,this,mainAnimationPanel.cd);
		bus.setGui(bg);
		mainAnimationPanel.addGui(bg);
		bus.startThread();
		
    }
    
  
    public static void main(String[] args) { 
        MainGui gui = new MainGui();
        gui.setTitle("Sim City - Team 15");
        gui.setVisible(true);
        gui.setResizable(true);
    }
    
    public void addPerson(String name, String role, String destination) {
    	if(mainAnimationPanel.cd.getPopulation() >= 40) {
    		JFrame frame = new JFrame();
    		JOptionPane.showMessageDialog(frame, "Population limit reached!");
    		return;
    	}
		PersonAgent p = new PersonAgent(name, this, mainAnimationPanel.cd);
		mainAnimationPanel.cd.addPerson(p);
		PersonGui personGui = new PersonGui(p, this);
		mainAnimationPanel.addGui(personGui);
		p.setGui(personGui);
		
		p.setDesiredRole(role);
		if(destination.equals("Restaurant"))
		{
			p.bigState = BigState.goToRestaurant;
		}
		else
		{
			p.bigState = BigState.goHome;
			p.assignHome(pickHome(p));
		}
		p.startThread();
		
	}
    
    public Building pickHome(PersonAgent p) {
    	for (Building b : mainAnimationPanel.cd.homes) {
    		if(!b.hasManager()) {
    			b.setManager(p);
    			return b;
    		}
    		//if(b.type==BuildingType.apartment) {
    			//for (b.)
    		//}
    	}
    	for (Building b: mainAnimationPanel.cd.apartments) {
    		Apartment a = (Apartment) b;
    		for (int i = 0; i < 8; i++) {
    			if (!a.rooms.get(i).isOccupied) {
    				a.rooms.get(i).setTenant(p);
    				p.setRoomNumber(i);
    				return b;
    			}
    		}
    	}
    	return null;
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
    
    public void removeGui(Gui gui)
    {
    	mainAnimationPanel.cd.removeGui(gui);
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