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
	
	
	/*Messages*/
	public void msgAssignRole(Role role)
	{
		for(Role r : roles)
		{
			if(r == role)
			{
				r.setActive();
				return;
			}
		}
		
		//If this part is reached, then 'role' is not in the list of roles
		roles.add(role);
		role.setActive();
	}
	
	/*SCHEDULER*/
	protected boolean pickAndExecuteAnAction() 
	{
		//Iterate through the list of roles
		for(Role role : roles)
		{
			//If a role is active, attempt to run its scheduler
			if(role.isActive())
			{
				if(role.pickAndExecuteAnAction())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/*METHODS TO BE USED FOR PERSON-ROLE INTERACTIONS*/
	protected void stateChanged()
	{
		super.stateChanged();
	}
}
