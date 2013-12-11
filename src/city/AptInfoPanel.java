package city;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AptInfoPanel extends JPanel implements ActionListener {

	public AptInfoPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Tenants", JLabel.CENTER));
	}
	
	public void addTenant(PersonAgent p) {
		add (new JLabel(p.roomNumber + 1 +  " " + p.getName()));
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {

		
	}

	
	
	
}
