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
	public ConfigParser(MainGui m) throws FileNotFoundException {
		//ConfigParser cp = new ConfigParser();
		scan = new Scanner( new File ("./config.txt"));
		main = m;
	}
	
	public void ParseAndCreatePeople() {
		if(scan.hasNextLine()) {
			String name = scan.next();
			String title = scan.next();
			main.addPerson(name,title);
			//MAKE A New Person OBJECT
			//WORK WITH MAINGUI TO CREATE OBJECT WITH THESE PROPERTIES THAT ARE PARSED IN
			
			
		}
	}
	
}
