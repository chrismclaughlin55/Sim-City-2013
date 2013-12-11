package bank;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import bank.gui.BankRobberGui;
import bank.interfaces.DaBankRobber;
import city.PersonAgent;
import city.Role;

public class BankRobber extends Role implements DaBankRobber {

	private Semaphore isMoving = new Semaphore(0, true);
	private PersonAgent me;
	private enum RobState {aboutToRob, robbed};
	RobState robState;
	private BankRobberGui gui;
	private Bank bank;
	
	BankRobber(PersonAgent p, Bank b) {
		super(p);
		me = p;
		bank = b;
		robState = RobState.aboutToRob;
		gui = new BankRobberGui(this);
		bank.bankGui.animationPanel.addGui(gui);
	}
	
	/* (non-Javadoc)
	 * @see bank.DaBankRobber#msgPleaseDontShoot(int)
	 */
	@Override
	public void msgPleaseDontShoot(int n) {
		me.cash += n;
		System.err.println(me.cash);
		robState = RobState.robbed;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see bank.DaBankRobber#msgDoneMoving()
	 */
	@Override
	public void msgDoneMoving() {
		isMoving.release();
	}
	
	/* (non-Javadoc)
	 * @see bank.DaBankRobber#pickAndExecuteAnAction()
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		if (robState == RobState.aboutToRob) {
			robBank();
			return true;
		}
		if (robState == RobState.robbed) {
			leaveBank();
			return true;
		}
		return false;
	}
	
	private void robBank() {
		gui.DoGoToTeller();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!bank.currentManager.tellers.isEmpty()) {
			bank.currentManager.tellers.get(0).t.msgStickEmUp(this);
			AlertLog.getInstance().logError(AlertTag.BANK, me.getName(), "Rob the bank!");
		}
		else {
			System.err.println("Aww, nobody to rob here.");
			AlertLog.getInstance().logError(AlertTag.BANK, me.getName(), "Aww, nobody to rob here.");
		}
	}
	
	private void leaveBank() {
		gui.DoGoToDoorway();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoLeaveBuilding();
		bank.bankGui.animationPanel.removeGui(gui);
		me.msgDoneWithJob();
		me.msgDoneWithRole();
		me.exitBuilding();
		me.setJob("Unemployed");
		doneWithRole();
	}

}
