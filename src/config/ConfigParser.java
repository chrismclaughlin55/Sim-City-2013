package config;
import java.io.*;
import java.util.*;
public class ConfigParser{
	/*
	 * Will take in an input file, most likely a .txt, .in, or .csv.
	 * Be linked to the MainGui to create people
	 */
	//File config = new File("config.txt");
	Scanner scan;
	public ConfigParser() throws FileNotFoundException {
		//ConfigParser cp = new ConfigParser();
		scan = new Scanner( new File ("config.txt"));
	}
	
	public void ParseAndCreatePeople() {
		if(scan.hasNext()) {
			//MAKE A New Person OBJECT
			//WORK WITH MAINGUI TO CREATE OBJECT WITH THESE PROPERTIES THAT ARE PARSED IN
			
			
		}
	}
	
}
