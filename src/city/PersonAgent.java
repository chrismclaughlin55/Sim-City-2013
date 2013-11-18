package city;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class PersonAgent extends Agent
{
	/*DATA MEMBERS*/
	String name;
	
	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)
	
	
	/*CONSTRUCTORS*/
	public PersonAgent(String name)
	{
		this.name = name;
	}
	
	
	/*MESSAGES*/
	public void msgAssignRole(Role role)
	{
		for(Role r : roles)
		{
			if(r == role)
			{
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
	
	/*SCHEDULER*/
	protected boolean pickAndExecuteAnAction() 
	{
		boolean activeRole = false;
		//Iterate through the list of roles
		for(Role role : roles)
		{
			//If a role is active, attempt to run its scheduler
			if(role.isActive())
			{
				activeRole = true;
				if(role.pickAndExecuteAnAction())
				{
					return true;
				}
			}
		}
		
		if(!activeRole)
		{
			System.out.println("No active roles detected");
		}
		return false;
	}
	
	/*METHODS TO BE USED FOR PERSON-ROLE INTERACTIONS*/
	protected void stateChanged()
	{
		super.stateChanged();
	}
	
	/*GETTERS*/
	public String getName()
	{
		return name;
	}
}
