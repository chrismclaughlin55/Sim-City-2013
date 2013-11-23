package city;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import market.gui.AnimationPanel;

public class HomeGui extends JFrame implements ActionListener {

	public HomeGui() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width/2, screenSize.height/2);
		setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
		setLayout(new GridLayout(0,2));
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
