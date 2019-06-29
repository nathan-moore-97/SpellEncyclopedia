package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import encyclopedia.Spell;
import encyclopedia.SpellEncyclopedia;

public class FileHandler {
	
	/**
	 * Method build from file - builds the encyclopedia from different
	 * files on the machine. 
	 */
	public static void buildFromFile() {
		
		// Tries to build the Spell Encyclopedia from a serialized version found
		// in the local directory. 
		System.out.println("Deserializing Spell Encyclopedia...");
		try {
			FileInputStream fis = new FileInputStream("spells.dnd");
			ObjectInputStream ois = new ObjectInputStream(fis);
			SpellEncyclopedia.readIn(ois.readObject());
			ois.close();
			
			// Success, leave the build from file method. 
			System.out.println("Success.");
			System.out.println("Spells Deserialized: " + SpellEncyclopedia.getInstance().size());
			return; 
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		
		// Failure. Attempts to build the Spell Encyclopedia from the 
		// raw JSON and text files on the local directory. 
		System.err.println("spells.dnd missing. Running build script");
		SpellEncyclopedia se = SpellEncyclopedia.getInstance();
		JSONParser parser = new JSONParser();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader("masterSpellList.txt"));
			Object obj = parser.parse(new FileReader("newSpells.json"));
			JSONObject jo = (JSONObject) obj;
			String line = "";
			
			// Each line is a spell name
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 3) { // Newline
					Spell s = new Spell(line);
					s.populate((JSONObject) jo.get(s.getName()));
					se.add(s);
				}
			}
			System.out.println("Success.");
			System.out.println("Spells built: " + se.size());
		} catch(Exception e) {
			// It is hopeless. rip in pepperonis.
			// TODO view.ShowError(); ?
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method export to TXT
	 * 
	 * This method builds a large string with all of the spells in the 
	 * SE and writes it out as a Plaintext file. 
	 * @param fileName 
	 */
	public static void exportToPlainText(String fileName) {
		SpellEncyclopedia se = SpellEncyclopedia.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("\n------- SPELL ENCYCLOPEDIA ------\n\n");
		//TODO put something real here
		build.append("Something something Nathan Moore link to my github yadayda\n\n"); 
		build.append("******* Start ********\n\n");
		for(Spell s : se) {
			build.append(s.toString() + "\n");
			build.append("***\n\n");
		}
		build.append("*******  End  ********\n");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(build.toString());
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method export to JSON
	 * 
	 * This method builds a large String in JSON file format, 
	 * then writes that string to a file in the current directory. 
	 * @param fileName 
	 */
	public static void exportToJSON(String fileName) {
		SpellEncyclopedia se = SpellEncyclopedia.getInstance();
		
		StringBuilder build = new StringBuilder();
		build.append("{");
		int counter = 0;
		int size = se.size();
		
		// This goes in a loop
		for(Spell s : se) {
			build.append("\n\t\"" + s.getName() + "\" : {\n");
			build.append("\t\t\"classes\" : \"");
			String[] arr = s.getClasses();
			for(int i = 0; i < arr.length; i++) {
				build.append(arr[i]);
				if( i != arr.length - 1) {
					build.append(",");
				}
			}
			build.append("\",\n");
			build.append("\t\t\"casting_time\" : \"" + s.getCastingTime() + "\",\n");
			build.append("\t\t\"components\" : \"" + s.getComponents() + "\",\n");
			build.append("\t\t\"description\" : \"" + s.getDescription() + "\",\n");
			build.append("\t\t\"higher_levels\" : \"" + s.getHigherLevels() + "\",\n");
			build.append("\t\t\"duration\" : \"" + s.getDuration() + "\",\n");
			build.append("\t\t\"level\" : " + s.getLevel() + ",\n");
			build.append("\t\t\"range\" : \"" + s.getRange() + "\",\n");
			build.append("\t\t\"school\" : \"" + s.getSchool() + "\",\n");
			build.append("\t\t\"ritual\" : " + s.isRitual() +"\n");
			build.append("\t}");
			
			counter++;
			
			if(counter != size - 1) {
				build.append(",");
			}
		}
		
		build.append("\n}");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(build.toString());
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the inst out to spells.dnd
	 */
	public static void writeOut(String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(SpellEncyclopedia.getInstance());
			oos.flush();
			oos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
