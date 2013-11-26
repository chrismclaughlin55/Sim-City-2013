package mainGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import restaurantMQ.gui.MQRestaurantBuilding;
import bank.Bank;
import market.Market;
import Gui.Gui;
import city.Apartment;
import city.Building;
import city.Building.BuildingType;
import city.BusAgent;
import city.BusStopAgent;
import city.CityData;
import city.Home;
import city.gui.BusGui;
import city.gui.PersonGui;

public class MainAnimationPanel extends JPanel implements ActionListener {

	private int frameDisplay = 2;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	//public List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	public CityData cd;

	private int WIDTH = 100;
	private int HEIGHT = 100;
	public static final int GLOBALINTERVAL = 50;
	
	private MainGui mainGui;
	

	public MainAnimationPanel(MainGui mainGui) {
		//Add buildings
		this.mainGui = mainGui;
		cd = new CityData();
		for (int i = 0; i < 2; i++) {
			Building b = new Apartment(10, 140+i*130, WIDTH, HEIGHT, "apartment", BuildingType.apartment, mainGui, cd);
			cd.buildings.add(b);
			cd.apartments.add((Apartment) b);
			BusStopAgent bs = new BusStopAgent(10+95+25, 140+i*130+35+5,cd);
			cd.busStops.add(bs);
			b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Apartment(10, 410+i*130, WIDTH, HEIGHT, "apartment", BuildingType.apartment, mainGui, cd);
			cd.buildings.add(b);
			cd.apartments.add((Apartment)b);
			BusStopAgent bs = new BusStopAgent(10+95+25, 410+i*130+35+5,cd);
			cd.busStops.add(bs);
			b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Home(190+i*130, 680, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(190+i*130+95-50, 680-45,cd);
			cd.busStops.add(bs);
			b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(500, 410+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(500-10-50, 410+i*130+35+5,cd);
			cd.busStops.add(bs);
			b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(500, 140+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(500-10-50, 140+i*130+35+5,cd);
			cd.busStops.add(bs);
			b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(190+i*130, 0, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(190+i*130+95-50, 0+35+5+55,cd);
			cd.busStops.add(bs);
			b.setBusStop(bs);
			bs.startThread();
		}
		
		
		//create restaurants
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 2; i++) {
				Building b = new MQRestaurantBuilding(190+i*130, 140+j*130, WIDTH, HEIGHT, "", BuildingType.restaurant, mainGui);
				cd.buildings.add(b);
			}
		}
		for (int i = 0; i < 2; i++) {
			Building b = new MQRestaurantBuilding(190+i*130, 540, WIDTH, HEIGHT, "", BuildingType.restaurant, mainGui);
			cd.buildings.add(b);
		}
		//done creating restaurants
		
		
		Bank b = new Bank(190, 410, WIDTH, HEIGHT,"bank", BuildingType.bank, mainGui, cd);
		cd.buildings.add(b);

		Market market = new Market(320, 410, WIDTH, HEIGHT, "market", BuildingType.market, mainGui, cd);
		cd.buildings.add(market);
		
		cd.buildings.get(12).setBusStop(cd.busStops.get(0));
		cd.buildings.get(13).setBusStop(cd.busStops.get(9));
		cd.buildings.get(14).setBusStop(cd.busStops.get(1));
		cd.buildings.get(15).setBusStop(cd.busStops.get(8));
		cd.buildings.get(16).setBusStop(cd.busStops.get(2));
		cd.buildings.get(17).setBusStop(cd.busStops.get(7));
		cd.buildings.get(18).setBusStop(cd.busStops.get(3));
		cd.buildings.get(19).setBusStop(cd.busStops.get(6));
		
		cd.setBusStopRoute();
		
		//Set apartment parameters
		for (int i = 0; i < 4; i++) {
			cd.buildings.get(i).setBuildingNumber(i);
		}

		//Set house parameters
		for (int i = 4; i < 12; i++) {
			cd.buildings.get(i).setBuildingNumber(i);
		}
		
		//Set restaurant parameters
		for (int i = 12; i < 18; i++) {
			cd.buildings.get(i).setType(BuildingType.restaurant);
			cd.buildings.get(i).setName("restaurant"+(i-11));
			cd.buildings.get(i).setBuildingNumber(i);
		}
		
		//Set bank parameters
		cd.buildings.get(18).setType(BuildingType.bank);
		cd.buildings.get(18).setName("bank");
		cd.buildings.get(18).setBuildingNumber(18);
		
		//Set market parameters
		cd.buildings.get(19).setType(BuildingType.market);
		cd.buildings.get(19).setName("market");
		cd.buildings.get(19).setBuildingNumber(19);
		cd.market = (Market) cd.buildings.get(19);
		//setBackground(Color.WHITE);
		cd.globalTimer = new Timer(GLOBALINTERVAL,(ActionListener) this);
        
		setVisible(true);
		cd.globalTimer.start();

	}

	public void actionPerformed(ActionEvent e) {
		cd.incrementTime();
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.WHITE);

		//Clear the screen by painting a rectangle the size of the frame
		ImageIcon background = new ImageIcon("res/background.png");
		g2.drawImage(background.getImage(), 0, 0, null);

		
		ImageIcon busStop = new ImageIcon("res/busstop.png");
		//Draw apartments
		ImageIcon apartment = new ImageIcon("res/apartment.png");
		for (int i = 0; i < 4; i++) {
			g2.drawImage(apartment.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
			//Draw bus stop for each apartment
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+95, (int)cd.buildings.get(i).y+35, null);
		}

		//Draw houses
		ImageIcon house = new ImageIcon("res/house.png");
		for (int i = 4; i < 12; i++) {
			g2.drawImage(house.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
		}

		//Draw bus stop for each house
		for (int i = 4; i < 6; i++) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+95, (int)cd.buildings.get(i).y, null);
		}
		for (int i = 6; i < 10; i++) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x-10, (int)cd.buildings.get(i).y+35, null);
		}
		for (int i = 10; i < 12; i++) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+95, (int)cd.buildings.get(i).y+35, null);
		}
		
		//Draw restaurants
		ImageIcon rest = new ImageIcon("res/restaurant.png");
		for (int i = 12; i < 18; i++) {
			g2.drawImage(rest.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
		}
		
		//Draw bank
		ImageIcon bank = new ImageIcon("res/bank.png");
		g2.drawImage(bank.getImage(), (int) cd.buildings.get(18).x, (int) cd.buildings.get(18).y, null);
		g2.drawString(cd.buildings.get(18).name, (int) cd.buildings.get(18).x, (int) cd.buildings.get(18).y+10);
		
		//Draw market
		ImageIcon market = new ImageIcon("res/market.png");
		g2.drawImage(market.getImage(), (int) cd.buildings.get(19).x, (int) cd.buildings.get(19).y, null);
		g2.drawString(cd.buildings.get(19).name, (int) cd.buildings.get(19).x, (int) cd.buildings.get(19).y+10);

		//Draw road
		ImageIcon road1 = new ImageIcon("res/road1.png");
		for (int i = 0; i < 615; i++) {
			g2.drawImage(road1.getImage(), i, 365, null);
		}
		for (int i = 130; i < 470; i++) {
			g2.drawImage(road1.getImage(), i, 95, null);
		}
		for (int i = 130; i < 470; i++) {
			g2.drawImage(road1.getImage(), i, 635, null);
		}
		ImageIcon road2 = new ImageIcon("res/road2.png");
		for (int i = 95; i < 660; i++) {
			g2.drawImage(road2.getImage(), 130, i, null);
		}
		for (int i = 95; i < 660; i++) {
			g2.drawImage(road2.getImage(), 440, i, null);
		}
		ImageIcon road3 = new ImageIcon("res/road3.png");
		g2.drawImage(road3.getImage(), 130, 365, null);
		g2.drawImage(road3.getImage(), 440, 365, null);
		g2.drawImage(road3.getImage(), 130, 95, null);
		g2.drawImage(road3.getImage(), 440, 95, null);
		g2.drawImage(road3.getImage(), 130, 635, null);
		g2.drawImage(road3.getImage(), 440, 635, null);
        
        
		synchronized(cd.guis){
			for(Gui gui : cd.guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		synchronized(cd.guis){
			for(Gui gui : cd.guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(PersonGui gui) {
		cd.guis.add(gui);
		System.out.println ("added gui!");
	}
	
	public void addGui(BusGui gui) {
		cd.guis.add(gui);
		
	}

	public List getBuildings() {
		return cd.buildings;
	}

}