package city;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HomeInfoPanel extends JPanel implements ActionListener {

	public HomeInfoPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(200, 400));
		add(new JLabel("Home"));
	}
	
	public void addResident(PersonAgent p) {
		add (new JLabel("Resident: " + p.getName()));
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {

		
	}

	
	
	
}
