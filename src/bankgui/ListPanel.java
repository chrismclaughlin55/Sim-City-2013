package bankgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class ListPanel extends JPanel implements ActionListener{
	private BankPanel bankPanel;

	ListPanel(BankPanel bp){
		this.bankPanel = bp;


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
