import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Progress Meter - This class contains a script to help determine 
 * which spells are missing from a given file. It also has a breakdown 
 * by individual spell, with the spell printing iff it is missing from 
 * either the spells.txt file (containing the spell name and which can cast it) 
 * or the spells.json file (containing all of the information of the spell)
 * 
 * Spells missing from the JSON file will have to be added either through an input
 * form on the front end or through manually typing the information in the file 
 * itself. 
 * 
 * Flags -
 * 	SCN - (spell class no) missing from spells.txt 
 *  SCY - (spell class yes) present in spells.txt
 *  JN  - (json no) missing from spells.json
 *  JY  - (json yes) present in spells.json
 *  
 * @author nathanmoore
 *
 */
public class ProgressMeter {
	public static void main(String[] args) {
		int mCount = 0;
		int scCount = 0;
		int scMissing = 0;
		ArrayList<String> mList = new ArrayList<>();
		ArrayList<String> scList = new ArrayList<>();
		ArrayList<String> mfjList = new ArrayList<>();
		ArrayList<String> missing = new ArrayList<>();
		File master = new File("masterSpellList.txt");
		File spellsWithClasses = new File("spells.txt");
		JSONParser parser = new JSONParser();
		BufferedReader read;
		JSONObject jo = new JSONObject();
		try {
			Object obj = parser.parse(new FileReader("newSpells.json"));
	         jo = (JSONObject) obj;
			read = new BufferedReader(new FileReader(master));
			String line; 
			while( (line = read.readLine()) != null) {
				if (line.trim().length() > 0) {
					mList.add(line.trim());
				}
			}
			mCount = mList.size();
			
			read = new BufferedReader(new FileReader(spellsWithClasses)); 
			while( (line = read.readLine()) != null) {
				if (line.trim().length() > 3) {
					String spellName = line.substring(0, line.indexOf("(")).trim();
					scList.add(spellName);
				}
			}
			scCount = scList.size();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for(String s : mList) {
			boolean sc = false;
			boolean json = false;
			if (!scList.contains(s)) {
				sc = true;
				scMissing++;
			}
			JSONObject spell = (JSONObject) jo.get(s); 
			if( spell == null ) {
				json = true;
				mfjList.add(s);
			} 
			if( sc && json ) {
				missing.add(s);
			}
		}
		
	
		System.out.println("Master Count:          " + mCount);
		System.out.println("Spell classes count:   " + scCount);
		System.out.println("Spell classes missing: " + scMissing);
		System.out.println("json missing:          " + mfjList.size());
		System.out.println("Missing completetly:   " + missing.size() +"\n");
		
		for( String s: mList) {
			boolean temp = false;
			String build = s;
			for(int i = 0; i < 35 - s.length(); i++) {
				build+=" ";
			}
			if (scList.contains(s)) {
				build+=(" SCY");
			} else {
				temp=true;
				build+=(" SCN");
			}
			
			if (mfjList.contains(s)) {
				temp=true;
				build+=(" JN");
			} else {
				build+=(" JY");
			}
			
			if(temp){
				System.out.println(build);
			}
			
		}
	}
}
