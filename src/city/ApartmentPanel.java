package city;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Gui.Gui;
import city.gui.PersonGui;

public class ApartmentPanel extends JPanel implements ActionListener, MouseListener {

	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 2;
	private int width = 400;
	private int height = 400;
	private List<HomeGui> homeGuis = new ArrayList<HomeGui>();
	

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public ApartmentPanel() {
		setBackground(Color.WHITE);
		setVisible(true);
		bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this);
		timer.start();
		addMouseListener(this);
		
	}
	
	public void addRoomGui(HomeGui h) {
		homeGuis.add(h);
	}
	
	public HomeGui getRoom(int rn) {
		return homeGuis.get(rn);
	}

	public void actionPerformed(ActionEvent e) {
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.CYAN);
		g2.fillRect(0, 173, 20, 30);
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, 100, 140);
		g2.setColor(Color.BLACK);
		g2.drawLine(0, 140, 400, 140);
		g2.drawLine(0, 238, 400, 238);
		g2.drawLine(100, 0, 100, 140);
		g2.drawLine(200, 0, 200, 140);
		g2.drawLine(300, 0, 300, 140);
		g2.drawLine(100, 238, 100, 400);
		g2.drawLine(200, 238, 200, 400);
		g2.drawLine(300, 238, 300, 400);
		g2.drawString("1", 48, 67);
		g2.drawString("2", 148, 67);
		g2.drawString("3", 248, 67);
		g2.drawString("4", 348, 67);
		g2.drawString("5", 48, 305);
		g2.drawString("6", 148, 305);
		g2.drawString("7", 248, 305);
		g2.drawString("8", 348, 305);
		

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
		gui.setXPos(0);
		gui.setYPos(176);
		gui.setPresent(true);
	}
	
	public void addGui(PersonGui gui, int roomNumber) {
		int xPos;
		int yPos;
		if (roomNumber < 4) {
			xPos = 45 + 100*roomNumber;
			yPos = 140;
		}
		else {
			xPos = 40 + 100*(roomNumber - 4);
			yPos = 217;
		}
		gui.setXPos(xPos);
		gui.setYPos(yPos);
		guis.add(gui);
		gui.setPresent(true);
	}
	
	public void removeGui(PersonGui gui, int roomNumber) {
		guis.remove(gui);
		gui.setPresent(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int posX = e.getX();
		int posY = e.getY();
		if (posY >= 0 && posY <= 140) {
			if (posX >= 0 && posX< 100) {
				homeGuis.get(0).setVisible(true);
			}
			else if (posX >= 100 && posX < 200) {
				homeGuis.get(1).setVisible(true);
			}
			else if (posX >= 200 && posX < 300) {
				homeGuis.get(2).setVisible(true);
			}
			else if (posX >= 300 && posX <= 400) {
				homeGuis.get(3).setVisible(true);
			}
		}
		else if (posY >= 238 && posY <= 400) {
			if (posX >= 0 && posX< 100) {
				homeGuis.get(4).setVisible(true);
			}
			else if (posX >= 100 && posX < 200) {
				homeGuis.get(5).setVisible(true);
			}
			else if (posX >= 200 && posX < 300) {
				homeGuis.get(6).setVisible(true);
			}
			else if (posX >= 300 && posX <= 400) {
				homeGuis.get(7).setVisible(true);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}