package city;

public abstract class Role 
{
	/*DATA MEMBERS*/
	protected PersonAgent person;
	private boolean isActive = false;
	
	/*CONSTRUCTORS*/
	public Role(PersonAgent person)
	{
		this.person = person;
	}
	
	
	/*GETTERS/SETTERS*/
	
	//PersonAgent will only run a particular Role's scheduler if the Role is active
	public boolean isActive()
	{
		return isActive;
	}
	
	//Activate the role, to be called when the Role 'starts'
	protected void setActive()
	{
		isActive = true;
	}
	
	//Deactivate the role, to be called when the Role is 'done'
	protected void setInactive()
	{
		isActive = false;
	}
	
	//To be called when a role leaves the building it is associated with
	protected void doneWithRole()
	{
		isActive = false;
		person.msgDoneWithRole();
	}
	
	//Pause the PersonAgent
	public void msgPause()
	{
		person.msgPause();
	}
	
	
	/*Person-Role interaction methods*/
	
	//Release the PersonAgent's semaphore
	protected void stateChanged()
	{
		person.stateChanged();
	}
	
	//All roles must have a scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	/*GETTERS*/
	public String getName()
	{
		return person.getName();
	}
}
