package city;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import mainGUI.MainGui;
import city.gui.PersonGui;
import agent.Agent;

public class PersonAgent extends Agent
{
	/*DATA MEMBERS*/
	String name;
	public double money;
	boolean ranonce = false;
	PersonGui personGui;
	MainGui gui;
	
	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)
	
	public enum state {doingNothing, goToRestaurant, goToBank, goToMarket, goHome, atHome};
	public state personState = state.doingNothing;
	
	private Semaphore atBuilding = new Semaphore(0, true);
	private Semaphore isMoving = new Semaphore(0, true);
	
	/*CONSTRUCTORS*/
	public PersonAgent(String name) {
		this.name = name;
	}
	
	public PersonAgent(String name, MainGui gui) {
		this.name = name;
		this.gui = gui;
		personGui = new PersonGui(this, gui);
	}
	
	/*MESSAGES*/
	public void msgDoneMoving() {
		isMoving.release();
	}
	
	public void msgAssignRole(Role role) {
		for (Role r : roles) {
			if (r == role) {
				r.setActive();
				super.stateChanged();
				return;
			}
		}
		
		//If this part is reached, then 'role' is not in the list of roles
		roles.add(role);
		role.setActive();
		super.stateChanged();
	}
	
	public void msgAtBuilding() {//from animation
		//print("msgAtBuilding() called");
		atBuilding.release();// = true;
		stateChanged();
	}
	
	/*SCHEDULER*/
	protected boolean pickAndExecuteAnAction() {
		if (personState == state.goToRestaurant) {
			goToRestaurant();
			return true;
		}
		if (personState == state.goHome) {
			goHome();
			return true;
		}
		if (personState == state.goToBank) {
			goToBank();
			return true;
		}
		if (personState == state.goToMarket) {
			goToMarket();
			return true;
		}
		
		for (Role role : roles) {
			if (role.isActive()) {
				if (role.pickAndExecuteAnAction()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected void goToRandomPlace() {
		//personGui.DoGoToRandomPlace();
	}
	
	protected void goToRestaurant() {
		int restNumber = (int)(12+(int)(Math.random()*17));
		personGui.DoGoToBuilding(restNumber);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	protected void goHome() {
		int homeNumber = (int)((int)(Math.random()*11));
		personGui.DoGoToBuilding(homeNumber);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	protected void goToBank() {
		personGui.DoGoToBuilding(18);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	protected void goToMarket() {
		personGui.DoGoToBuilding(19);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	/*METHODS TO BE USED FOR PERSON-ROLE INTERACTIONS*/
	protected void stateChanged() {
		super.stateChanged();
	}
	
	/*GETTERS*/
	public String getName() {
		return name;
	}
	
	public void setGui(PersonGui g) {
		personGui = g;
	}
	
	public PersonGui getGui() {
		return personGui;
	}
}
