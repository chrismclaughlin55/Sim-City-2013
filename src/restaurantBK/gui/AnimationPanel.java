package restaurantBK.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private final int WIDTH = 50;
    private final int HEIGHT = 50;
    private final int XCOORD = 200;
    private final int YCOORD = 250;
    private final int XCOORD2 = 326;
    private final int YCOORD2 = 250;
    private final int XCOORD3 = 450;
    private final int YCOORD3 = 250;
    private final int COOKX = 500;
    private final int COOKY = 200;
    private final int TIMERINTERVAL = 20;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMERINTERVAL, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
       
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight() );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(XCOORD, YCOORD, WIDTH, HEIGHT);//200 and 250 need to be table params
        
        Graphics2D g3 = (Graphics2D)g;
        g3.setColor(Color.ORANGE);
        g3.fillRect(XCOORD2, YCOORD2, WIDTH, HEIGHT);
        
        Graphics2D g4 = (Graphics2D)g;
        g4.setColor(Color.ORANGE);
        g4.fillRect(XCOORD3, YCOORD3, WIDTH, HEIGHT);
        
        
        

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(OrderGui gui) {
    	guis.add(gui);
    }
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    public void removeGui(OrderGui gui) {
    	guis.remove(gui);
    }
}
