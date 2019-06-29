package encyclopedia;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.DefaultListModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.FileHandler;

/**
 * Class SpellEncyclopedia - used to organize and use all of the spell objects
 * 
 * @author nathanmoore
 * 
 */
public class SpellEncyclopedia implements Iterable<Spell>, Serializable {
	
	

	// Defualt ID
	private static final long serialVersionUID = 1L;

	// Singleton
	private static SpellEncyclopedia inst = null;

	// Interior Data Structure for holding all of the spell objects.
	private ArrayList<Spell> spells;

	// Returns the singleton instance.
	public static SpellEncyclopedia getInstance() {
		if (inst == null) {
			inst = new SpellEncyclopedia();
		}
		return inst;
	}

	// ============================= SETUP =================================

	// Default Constructor.
	private SpellEncyclopedia() {
		spells = new ArrayList<>();
	}

	

	public boolean add(Spell spell) {
		if (!spells.contains(spell)) {
			spells.add(spell);
		}
		return false;
	}

	// =============================== Function ===============================
	/**
	 * This method is the current method used for searching the spell list.
	 * 
	 * After a search term is typed in the search bar, this method refines the list
	 * and returns a new ArrayList with all of the spells that match the search
	 * term.
	 * 
	 * This is not very efficient, (2n + r) but as of right now there is no slowdown in 
	 * the searching of the list with 468 items in it so its nbd.
	 * 
	 * @param term
	 * @return
	 */
	public DefaultListModel<String> refreshDefListModel(String term) {

		ArrayList<Spell> ret = new ArrayList<>();
		DefaultListModel<String> model = new DefaultListModel<>();
		
		for (Spell s : this) {
			if (s.getName().toLowerCase().startsWith(term.toLowerCase())) {
				ret.add(s);
			}
		}
		
		for(Spell s : this) {
			if (s.getName().toLowerCase().contains(term.toLowerCase()) && !ret.contains(s)) {
				ret.add(s);
			}
		}
		
		if (!ret.isEmpty()) {
			for (Spell s : ret) {
				model.addElement(s.getName());
			}

			return model;
		} else {
			return null;
		}
	}

	public int size() {
		return spells.size();
	}

	/**
	 * returns the spell object that EXACTLY matches the spellName sent through the
	 * parameter. Returns null if the spell cannot be found.
	 * 
	 * @param spellName (String)
	 * @return a spell (Spell)
	 */
	public Spell get(String spellName) {
		for (Spell sp : this) {
			if (spellName.equals(sp.getName())) {
				return sp;
			}
		}
		return null; // TODO binary search?
	}

	/**
	 * Returns a Default list model for use in the HomeScreen GUI. Used to populate
	 * the original spell list and for completly resetting Search results.
	 * 
	 * @return
	 */
	public DefaultListModel<String> getDefaultListModel() {
		DefaultListModel<String> model = new DefaultListModel<>();

		for (Spell s : this) {
			String spell = s.getName();
			if(s.wasParsed()) {
				model.addElement(spell);
			}
		}
		return model;
	}

	
	/**
	 * Returns a Default list model for use in the HomeScreen GUI. Used to populate
	 * the original spell list and for completly resetting Search results.
	 * 
	 * @return
	 */
	public DefaultListModel<String> getEditorListModel() {
		DefaultListModel<String> model = new DefaultListModel<>();

		for (Spell s : this) {
			String spell = s.getName();
			if(!s.wasParsed()) {
				spell = ("<html><font color = red>" + spell + "</font></html>");
			}
			model.addElement(spell);
			
		}
		return model;
	}
	/**
	 * Returns an iterator for the SE.
	 */
	public Iterator<Spell> iterator() {
		return spells.iterator();
	}

	// =============================== FILE I/O =============================

	/**
	 * Used for desserializing the SE from the spell.dnd file.
	 * 
	 * @param obj
	 */
	public static void readIn(Object obj) {
		if (obj instanceof SpellEncyclopedia) {
			inst = (SpellEncyclopedia) obj;
		}
	}		
}
