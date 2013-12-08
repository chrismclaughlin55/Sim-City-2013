package restaurantKC.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationPanel extends JPanel implements ActionListener {

	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 2;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public AnimationPanel() {
		setBackground(Color.WHITE);
		
		setVisible(true);

		bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this );
		timer.start();

	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Color tableColor = new Color(149, 165, 166);
		Color grillColor = new Color(241, 196, 15);
		Color platingColor = new Color(26, 188, 156);
		Color fridgeColor = new Color(155, 89, 182);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		//g2.fillRect(0, 0, 400, 400);
		int n = 50;
		for (int i = 0; i < 3; i++) {
			g2.setColor(tableColor);
			g2.fillRect(n, 400, 65, 65);
			n += 150;
		}
		
		n = 340;
		for (int i = 0; i < 3; i++) {
			g2.setColor(platingColor);
			g2.fillRect(n, 150, 50, 50);
			n += 55;
		}
		
		n = 340;
		for (int i = 0; i < 3; i++) {
			g2.setColor(grillColor);
			g2.fillRect(n, 10, 50, 50);
			n += 55;
		}
		
		g2.setColor(fridgeColor);
		g2.fillRect(230,10, 75, 150);

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

	public void addGui(CustomerGui gui) {
		guis.add(gui);
	}

	public void removeGui(FoodGui gui) {
		guis.remove(gui);
	}

	public void addGui(HostGui gui) {
		guis.add(gui);
	}

	public void addGui(WaiterGui gui) {
		guis.add(gui);
	}

	public void addGui(CookGui gui) {
		guis.add(gui);
	}

	public void addGui(FoodGui gui) {
		guis.add(gui);
	}

	public void addGui(CookFoodGui gui) {
		
		guis.add(gui);
	}


}
