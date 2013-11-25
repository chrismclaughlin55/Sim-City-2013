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
	boolean fileExist = false;
	public ConfigParser(MainGui m,String filename) throws FileNotFoundException {
		Properties props = new Properties();
		
		//ConfigParser cp = new ConfigParser();
		try {
			scan = new Scanner( new File (filename));
			fileExist=true;
		}
		catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		main = m;
	}
	
	public ArrayList<HashMap<String,String>> ParseAndCreatePeople() throws FileNotFoundException {
		ArrayList<HashMap<String,String>> peopleToCreate = new ArrayList<HashMap<String,String>>();
		
		
		return peopleToCreate;
	}
	
}
