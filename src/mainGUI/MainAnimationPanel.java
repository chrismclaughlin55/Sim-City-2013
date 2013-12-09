package mainGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import restaurantBK.gui.BKRestaurantBuilding;
import restaurantKC.gui.KCRestaurantBuilding;
import restaurantLY.gui.LYRestaurantBuilding;
import restaurantMQ.gui.MQRestaurantBuilding;
import restaurantSM.gui.SMRestaurantBuilding;
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
import city.PersonAgent;
import city.gui.BusGui;
import city.gui.PersonGui;
import city.Grid;
import city.BGrid;
import city.RGrid;

public class MainAnimationPanel extends JPanel implements ActionListener {

	private int frameDisplay = 2;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	//public List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	public CityData cd;

	private int WIDTH = 100;
	private int HEIGHT = 100;
	public int GLOBALINTERVAL = 20;

	private MainGui mainGui;


	public MainAnimationPanel(MainGui mainGui) {
		//Add buildings
		this.mainGui = mainGui;
		cd = new CityData();
		for (int i = 0; i < 2; i++) {
			Building b = new Apartment(20, 140+i*120, WIDTH, HEIGHT, "apartment", BuildingType.apartment, mainGui, cd);
			cd.buildings.add(b);
			cd.apartments.add((Apartment) b);
			BusStopAgent bs = new BusStopAgent(20+100+20, 140+i*120+40+20,cd);
			cd.busStops.add(bs);
			//b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Apartment(20, 420+i*120, WIDTH, HEIGHT, "apartment", BuildingType.apartment, mainGui, cd);
			cd.buildings.add(b);
			cd.apartments.add((Apartment)b);
			BusStopAgent bs = new BusStopAgent(20+100+20, 420+i*120+40+20,cd);
			cd.busStops.add(bs);
			//b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Home(200+i*140, 680, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(200+i*140+100-40, 680-20,cd);
			cd.busStops.add(bs);
			//b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(520, 420+i*120, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(520-20-20, 420+i*120+40+20,cd);
			cd.busStops.add(bs);
			//b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(520, 140+i*120, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
			cd.homes.add((Home)b);
			BusStopAgent bs = new BusStopAgent(520-20-20, 140+i*120+40+20,cd);
			cd.busStops.add(bs);
			//b.setBusStop(bs);
			bs.startThread();
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Apartment(200+i*140, 0, WIDTH, HEIGHT, "apartment", BuildingType.apartment, mainGui, cd);
			cd.buildings.add(b);
			cd.apartments.add((Apartment)b);
			BusStopAgent bs = new BusStopAgent(200+i*140+100-20, 0+40+60,cd);
			cd.busStops.add(bs);
			//b.setBusStop(bs);
			bs.startThread();
		}


		//create restaurants

		for (int i = 0; i < 2; i++) {
			Building b = new SMRestaurantBuilding(200+i*140, 140, WIDTH, HEIGHT, "", BuildingType.restaurant, mainGui, cd);
			cd.buildings.add(b);
			cd.restaurants.add((SMRestaurantBuilding) b);
		}

		for (int i = 0; i < 1; i++) {
			Building b = new KCRestaurantBuilding(200+i*140, 260, WIDTH, HEIGHT, "", BuildingType.restaurant, mainGui, cd);
			cd.buildings.add(b);
			cd.restaurants.add((KCRestaurantBuilding) b);
		}
		for(int i=0; i<1; i++) {
			Building b;
			try {
				b = new BKRestaurantBuilding(200+140,260,WIDTH,HEIGHT,"",BuildingType.restaurant, mainGui,cd);
				cd.buildings.add(b);
				cd.restaurants.add((BKRestaurantBuilding) b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		for (int i = 0; i < 1; i++) {
			Building b = new SMRestaurantBuilding(200+i*140, 540, WIDTH, HEIGHT, "", BuildingType.restaurant, mainGui, cd);
			cd.buildings.add(b);
			cd.restaurants.add((SMRestaurantBuilding) b);
		}
		for (int i = 0; i < 1; i++) {
			Building b = new LYRestaurantBuilding(200+140, 540, WIDTH, HEIGHT, "", BuildingType.restaurant, mainGui, cd);
			cd.buildings.add(b);
			cd.restaurants.add((LYRestaurantBuilding) b);
		}
		//done creating restaurants
		
		
		Bank b1 = new Bank(200, 420, WIDTH, HEIGHT,"bank", BuildingType.bank, mainGui, cd);
		cd.buildings.add(b1);
		cd.bank = b1;
		//cd.bank.test();

		Market m = new Market(340, 420, WIDTH, HEIGHT, "market", BuildingType.market, mainGui, cd);
		cd.buildings.add(m);
		cd.market =  m;

        
        //create bus stop for restaurants/market/bank
		BusStopAgent bs12 = new BusStopAgent(200-20-20, 140+40+20, cd);
		cd.busStops.add(bs12);
		BusStopAgent bs13 = new BusStopAgent(200+120-20, 120, cd);
		cd.busStops.add(bs13);
		BusStopAgent bs14 = new BusStopAgent(200+140+120, 140+40+20, cd);
		cd.busStops.add(bs14);
		BusStopAgent bs15 = new BusStopAgent(200+140+120, 140+120+40+20, cd);
		cd.busStops.add(bs15);
		BusStopAgent bs16 = new BusStopAgent(200+140+120, 420+40+20, cd);
		cd.busStops.add(bs16);
		BusStopAgent bs17 = new BusStopAgent(200+140+120, 420+120+40+20, cd);
		cd.busStops.add(bs17);
		BusStopAgent bs18 = new BusStopAgent(200+120-20, 640, cd);
		cd.busStops.add(bs18);
		BusStopAgent bs19 = new BusStopAgent(200-20-20, 420+120+40+20, cd);
		cd.busStops.add(bs19);
		BusStopAgent bs20 = new BusStopAgent(200-20-20, 420+40+20, cd);
		cd.busStops.add(bs20);
		BusStopAgent bs21 = new BusStopAgent(200-20-20, 140+120+40+20, cd);
		cd.busStops.add(bs21);
		for (int i = 12; i < 22; i++) {
			cd.busStops.get(i).startThread();
		}

		// Construct bus stop for restaurants/bank/market
		/*for (int i = 0; i < 2; i++) {
			BusStopAgent bs = new BusStopAgent(190-10-30, 140+i*130+35+25, cd);
			cd.busStops.add(bs);
			bs.startThread();
		}
		for (int i = 0; i < 2; i++) {
			BusStopAgent bs = new BusStopAgent(190-10-30, 410+i*130+35+25, cd);
			cd.busStops.add(bs);
			bs.startThread();
		}
		 */

		//set bus stop for route 1
		for (int i = 0; i < 12; i++) {
			cd.buildings.get(i).setBusStop(cd.busStops.get(i), 1);
			cd.busStops.get(i).setStopNumber(i);
		}
		for (int i = 12; i < 22; i++) {
			cd.busStops.get(i).setStopNumber(i);
		}
		cd.buildings.get(12).setBusStop(cd.busStops.get(0), 1);
		cd.buildings.get(13).setBusStop(cd.busStops.get(9), 1);
		cd.buildings.get(14).setBusStop(cd.busStops.get(1), 1);
		cd.buildings.get(15).setBusStop(cd.busStops.get(8), 1);
		cd.buildings.get(16).setBusStop(cd.busStops.get(3), 1);
		cd.buildings.get(17).setBusStop(cd.busStops.get(6), 1);
		cd.buildings.get(18).setBusStop(cd.busStops.get(2), 1);
		cd.buildings.get(19).setBusStop(cd.busStops.get(7), 1);

		//set bus stop for route 2
		/*cd.buildings.get(0).setBusStop(cd.busStops.get(12), 2);
		cd.buildings.get(1).setBusStop(cd.busStops.get(21), 2);
		cd.buildings.get(2).setBusStop(cd.busStops.get(20), 2);
		cd.buildings.get(3).setBusStop(cd.busStops.get(19), 2);
		cd.buildings.get(4).setBusStop(cd.busStops.get(18), 2);
		cd.buildings.get(5).setBusStop(cd.busStops.get(18), 2);
		cd.buildings.get(6).setBusStop(cd.busStops.get(17), 2);
		cd.buildings.get(7).setBusStop(cd.busStops.get(16), 2);
		cd.buildings.get(8).setBusStop(cd.busStops.get(15), 2);
		cd.buildings.get(9).setBusStop(cd.busStops.get(14), 2);
		cd.buildings.get(10).setBusStop(cd.busStops.get(13), 2);
		cd.buildings.get(11).setBusStop(cd.busStops.get(13), 2);
		cd.buildings.get(12).setBusStop(cd.busStops.get(12), 2);
		cd.buildings.get(13).setBusStop(cd.busStops.get(14), 2);
		cd.buildings.get(14).setBusStop(cd.busStops.get(21), 2);
		cd.buildings.get(15).setBusStop(cd.busStops.get(15), 2);
		cd.buildings.get(16).setBusStop(cd.busStops.get(19), 2);
		cd.buildings.get(17).setBusStop(cd.busStops.get(17), 2);
		cd.buildings.get(18).setBusStop(cd.busStops.get(20), 2);
		cd.buildings.get(19).setBusStop(cd.busStops.get(16), 2);*/
		
		
		//cd.setBusStopRoute(1);
		//cd.setBusStopRoute(2);

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
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+100, (int)cd.buildings.get(i).y+20, null);
		}

		//Draw houses
		ImageIcon house = new ImageIcon("res/house.png");
		for (int i = 4; i < 10; i++) {
			g2.drawImage(house.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
		}

		//Draw apartments
		for (int i = 10; i < 12; i++) {
			g2.drawImage(apartment.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
			//Draw bus stop for each apartment
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+100, (int)cd.buildings.get(i).y+20, null);
		}

		//Draw bus stop for each house
		for (int i = 4; i < 6; i++) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+100, (int)cd.buildings.get(i).y, null);
		}
		for (int i = 6; i < 10; i++) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x-20, (int)cd.buildings.get(i).y+20, null);
		}
		
		//Draw bus stop for restaurants/bank/market
		for (int i = 12; i < 20; i+=2) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x-20, (int)cd.buildings.get(i).y+20, null);
		}
		for (int i = 13; i < 20; i+=2) {
			g2.drawImage(busStop.getImage(), (int) cd.buildings.get(i).x+100, (int)cd.buildings.get(i).y+20, null);
		}
		
		//Draw bus stop
		g2.drawImage(busStop.getImage(), (int) cd.buildings.get(11).x+120, (int)cd.buildings.get(11).y+140, null);
		g2.drawImage(busStop.getImage(), (int) cd.buildings.get(4).x+120, (int)cd.buildings.get(4).y-100, null);

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
		for (int i = 0; i < 620; i++) {
			g2.drawImage(road1.getImage(), i, 360, null);
		}
		for (int i = 140; i < 480; i++) {
			g2.drawImage(road1.getImage(), i, 100, null);
		}
		for (int i = 140; i < 480; i++) {
			g2.drawImage(road1.getImage(), i, 640, null);
		}
		ImageIcon road2 = new ImageIcon("res/road2.png");
		for (int i = 100; i < 660; i++) {
			g2.drawImage(road2.getImage(), 140, i, null);
		}
		for (int i = 100; i < 660; i++) {
			g2.drawImage(road2.getImage(), 460, i, null);
		}
		ImageIcon road3 = new ImageIcon("res/road3.png");

		g2.drawImage(road3.getImage(), 140, 360, null);
		g2.drawImage(road3.getImage(), 460, 360, null);
		g2.drawImage(road3.getImage(), 140, 100, null);
		g2.drawImage(road3.getImage(), 460, 100, null);
		g2.drawImage(road3.getImage(), 140, 640, null);
		g2.drawImage(road3.getImage(), 460, 640, null);
		
		String clock = null;
		if (cd.hour >= 0 && cd.hour < 12) {
			clock = "Clock:  " + cd.hour + " AM";
		}
		else if (cd.hour == 12) {
			clock = "Clock:  " + cd.hour + " PM";
		}
		else if (cd.hour > 12 && cd.hour < 24) {
			clock = "Clock:  " + (cd.hour-12) + " PM";
		}
		g2.drawString(clock, 20, 30);

		String day = null;
		if(cd.day==0) {
			day = "Monday";
		}
		else if(cd.day==1) {
			day = "Tuesday";
		} 
		else if(cd.day==2) {
			day = "Wednesday";
		}
		else if(cd.day==3) {
			day = "Thursday";
		}
		else if(cd.day==4) {
			day = "Friday";
		}
		else if(cd.day==5) {
			day = "Saturday";
		}
		else if(cd.day==6) {
			day = "Sunday";
		}
		g2.drawString(day, 20, 50);
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
	}

	public void addGui(BusGui gui) {
		cd.guis.add(gui);

	}

	public List getBuildings() {
		return cd.buildings;
	}

}