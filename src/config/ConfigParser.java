package config;
import mainGUI.MainGui;
import java.io.*;
import java.util.*;
public class ConfigParser{
	/*
	 * Will take in an input file, most likely a .txt, .in, or .csv.
	 * Be linked to the MainGui to create people
	 */
	//File config = new File("config.txt");
	Scanner scan;
	MainGui main;
	public ConfigParser(MainGui m,String filename) throws FileNotFoundException {
		Properties props = new Properties();
		
		//ConfigParser cp = new ConfigParser();
		scan = new Scanner( new File (filename));
		main = m;
	}
	
	public ArrayList<HashMap<String,String>> ParseAndCreatePeople() {
		ArrayList<HashMap<String,String>> peopleToCreate = new ArrayList<HashMap<String,String>>();
		try {
			FileInputStream in = new FileInputStream("config.txt");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}
		//scan for name property
		//while next line isnt name, get in properties
		HashMap<String,String> props = new HashMap<String,String>();
		peopleToCreate.add(props);
		if(scan.hasNextLine()) {
			String name = scan.next();
			String title = scan.next();
			main.addPerson(name,title);
			//MAKE A New Person OBJECT
			//WORK WITH MAINGUI TO CREATE OBJECT WITH THESE PROPERTIES THAT ARE PARSED IN	
			
		}
		return peopleToCreate;
	}
	
}
