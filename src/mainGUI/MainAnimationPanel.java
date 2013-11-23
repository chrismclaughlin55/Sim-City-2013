package mainGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import city.Building;
import city.Building.BuildingType;
import Gui.*;
import city.gui.PersonGui;

public class MainAnimationPanel extends JPanel implements ActionListener {

	private int frameDisplay = 2;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	public List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());

	private int WIDTH = 100;
	private int HEIGHT = 100;
	
	private MainGui mainGui;
	
	public MainAnimationPanel(MainGui mainGui) {
		//Add buildings
		this.mainGui = mainGui;
		for (int i = 0; i < 2; i++) {
			Building b = new Building(10, 140+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui);
			buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(10, 410+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui);
			buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(190+i*130, 680, WIDTH, HEIGHT, "home", BuildingType.home, mainGui);
			buildings.add(b);
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Building(500, 410+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui);
			buildings.add(b);
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Building(500, 140+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui);
			buildings.add(b);
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Building(190+i*130, 0, WIDTH, HEIGHT, "home", BuildingType.home, mainGui);
			buildings.add(b);
		}
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 2; i++) {
				Building b = new Building(190+i*130, 140+j*130, WIDTH, HEIGHT, mainGui);
				buildings.add(b);
			}
		}
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 2; i++) {
				Building b = new Building(190+i*130, 410+j*130, WIDTH, HEIGHT, mainGui);
				buildings.add(b);
			}
		}
		
		//setBackground(Color.WHITE);
		
		setVisible(true);
		Timer timer = new Timer(frameDisplay, this );
		timer.start();

	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		//g2.setColor(getBackground());
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, screenSize.width/2, screenSize.height );
		ImageIcon background = new ImageIcon("res/background.png");
		g2.drawImage(background.getImage(), 0, 0, null);
		
		//Draw houses
		ImageIcon house = new ImageIcon("res/house.png");
		for (int i = 0; i < 12; i++) {
			g2.drawImage(house.getImage(), (int) buildings.get(i).x, (int) buildings.get(i).y, null);
			g2.drawString(buildings.get(i).name, (int) buildings.get(i).x, (int) buildings.get(i).y+10);
		}
		
		//Draw restaurants
		ImageIcon rest = new ImageIcon("res/restaurant.png");
		for (int i = 12; i < 16; i++) {
			g2.drawImage(rest.getImage(), (int) buildings.get(i).x, (int) buildings.get(i).y, null);
			buildings.get(i).setType(BuildingType.restaurant);
			buildings.get(i).setName("restaurant");
			g2.drawString(buildings.get(i).name, (int) buildings.get(i).x, (int) buildings.get(i).y+10);
		}
        g2.drawImage(rest.getImage(), (int) buildings.get(18).x, (int) buildings.get(18).y, null);
        g2.drawImage(rest.getImage(), (int) buildings.get(19).x, (int) buildings.get(19).y, null);
        buildings.get(18).setType(BuildingType.restaurant);
        buildings.get(19).setType(BuildingType.restaurant);
		buildings.get(18).setName("restaurant");
		buildings.get(19).setName("restaurant");
		g2.drawString(buildings.get(18).name, (int) buildings.get(18).x, (int) buildings.get(18).y+10);
		g2.drawString(buildings.get(19).name, (int) buildings.get(19).x, (int) buildings.get(19).y+10);
		
		//Draw bank
        ImageIcon bank = new ImageIcon("res/bank.png");
        g2.drawImage(bank.getImage(), (int) buildings.get(16).x, (int) buildings.get(16).y, null);
        buildings.get(16).setType(BuildingType.bank);
		buildings.get(16).setName("bank");
		g2.drawString(buildings.get(16).name, (int) buildings.get(16).x, (int) buildings.get(16).y+10);
		
		//Draw market
        ImageIcon market = new ImageIcon("res/market.png");
        g2.drawImage(market.getImage(), (int) buildings.get(17).x, (int) buildings.get(17).y, null);
        buildings.get(17).setType(BuildingType.market);
		buildings.get(17).setName("market");
		g2.drawString(buildings.get(17).name, (int) buildings.get(17).x, (int) buildings.get(17).y+10);
        
		//height 900, width 720
        //Draw road
        ImageIcon road1 = new ImageIcon("res/road1.png");
        for (int i = 0; i < screenSize.width/2; i++) {
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

		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(PersonGui gui) {
		guis.add(gui);
		System.out.println ("added gui!");
	}



}