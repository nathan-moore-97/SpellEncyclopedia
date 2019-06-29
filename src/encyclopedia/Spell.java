package encyclopedia;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class Spell implements Serializable, Comparable<Spell> {

	// Default
	private static final long serialVersionUID = 1L;

	// Flag to indicate it was successfully parsed from the JSON file.
	private boolean parsed = false;

	// Flags for what class can cast this spell.
	private boolean bardable = false;
	private boolean clericable = false;
	private boolean druidable = false;
	private boolean paladinable = false;
	private boolean rangerable = false;
	private boolean warlockable = false;
	private boolean sorcerable = false;
	private boolean wizardable = false;

	// Spell Information. Getters required for all of these fields.
	private boolean isRitual;
	private boolean needsMaterial = false;
	private long spellLevel;

	private String[] classes;
	private String name;
	private String description;
	private String castTime;
	private String duration;
	private String higherLvls;
	private String components;
	private String school;
	private String range;

	/**
	 * Sets the boolean class attributes. 
	 * 
	 * @param classes
	 */
	private void setFlags(String[] classes) {
		for (String s : classes) {
			switch (s.trim()) {
			case "bard":
				bardable = true;
				break;
			case "cleric":
				clericable = true;
				break;
			case "ranger":
				rangerable = true;
				break;
			case "warlock":
				warlockable = true;
				break;
			case "sorcerer":
				sorcerable = true;
				break;
			case "wizard":
				wizardable = true;
				break;
			case "druid":
				druidable = true;
				break;
			case "paladin":
				paladinable = true;
				break;
			default:
				System.err.println("Something went wrong creating spell with String:" + s + "\n Spell " + this.name);
				break;
			}
		}
	}
	
	/**
	 * Sets the classes field of the Spell. Currently no input sanitaization other 
	 * than checking if the input array is not null. 
	 * 
	 * @param newClasses
	 */
	public void setClasses(String[] newClasses) {
		if(classes != null) {
			this.classes = newClasses;
		}
	}

	public Spell(String line) {
		this.name = line;
		spellLevel = -1;
		classes = new String[] {"Classes Needed"};
		description = "Description needed";
		castTime = "Casting time needed";
		duration = "duration needed";
		higherLvls = "Higher Levels needed";
		school = "School needed";
		range = "Range needed";
	}

	// Returns title field
	public String getName() {
		return this.name;
	}

	// Returns the description for casting the spell at higher levels
	public String getHigherLevels() {
		if (parsed) {
			String[] spl = higherLvls.split("\n");
			
			String build = "";
			// Removes all newlines from the description. Used to fix errors in the
			// JSON writer class.
			for (int i = 0; i < spl.length; i++) {
				if (i > 0) {
					build += ("\\n");
				}
				build += spl[i];
			}
			return build;
		}

		return higherLvls;
	}

	/**
	 * Returns a formatted string representation of the current spell object.
	 */
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append(name);
		build.append(" (");
		for (int i = 0; i < classes.length; i++) {
			build.append(classes[i]);
			if (i != classes.length - 1) {
				build.append(", ");
			} else {
				build.append(")\n");
			}
		}
		
		if (spellLevel == 0) {
			build.append(school + " ");
			build.append("Cantrip");
		} else {
			build.append(spellLevel);
			if (spellLevel == 1) {
				build.append("st ");
			} else if (spellLevel == 2) {
				build.append("nd ");
			} else if (spellLevel == 3) {
				build.append("rd ");
			} else {
				build.append("th ");
			}
			build.append("level " + school);
		}
		if (isRitual) {
			build.append(" <R>");
		}
		build.append("\n");
		build.append("Casting Time: " + castTime + "\n");
		build.append("Duration: " + duration + "\n");
		build.append("Range: " + range + "\n");
		build.append(format("Components: " + components) + "\n");
		build.append("\n");
		build.append(format(description));
		if (higherLvls != null) {
			build.append("\n   - At Higher Levels -\n");
			build.append(format(higherLvls));
		}
		return build.toString();
	}

	/**
	 * Step 2 in building a spell object. Takes in a parsed JSON object as a
	 * parameter and uses it to fill the fields of this spell instance.
	 * 
	 * Sets all remaining fields
	 * 
	 * @param spell (JSONObject)
	 */
	public void populate(JSONObject spell) {
		// Spell will be null if the this.title could not be found
		// in the JSON file as a spell name
		if (spell != null) {
			classes = ((String) spell.get("classes")).split(", ");
			setFlags(classes);
			description = (String) spell.get("description");
			higherLvls = (String) spell.get("higher_lvls");
			spellLevel = ((long) spell.get("level"));
			components = (String) spell.get("components");

			if (components.contains("M")) {
				needsMaterial = true;
			}
			school = (String) spell.get("schools");
			castTime = (String) spell.get("casting_time");
			duration = (String) spell.get("duration");
			range = (String) spell.get("range");
			isRitual = (boolean) spell.get("ritual");
			parsed = true;
		} else {
			System.err.println("Populate Error: " + name);
		}
	}

	/**
	 * Not the same as equals(), this function returns true if the filled out fields
	 * of the incoming spell match the current one.
	 * 
	 * Used for querying the spell list.
	 * 
	 * @param other
	 * @return
	 */
	public boolean matches(Spell other) {
		return false; // TODO implement matches
	}

	public boolean needsMaterial() {
		return this.needsMaterial;
	}

	public long getLevel() {
		return spellLevel;
	}

	public String getDescription() {
		if (parsed) {
			String[] spl = description.split("\n");
			String build = "";
			// Removes all newlines from the description. Used to fix errors in the
			// JSON writer class.
			for (int i = 0; i < spl.length; i++) {
				if (i > 0) {
					build += ("\\n");
				}
				build += spl[i];
			}
			return build;
		}

		return description;
	}

	/**
	 * Returns an array of all of the classes that can cast this spell.
	 * 
	 * @return classes
	 */
	public String[] getClasses() {
		return this.classes;
	}

	/**
	 * Helper method for toString()
	 * 
	 * @param s
	 * @return
	 */
	private String format(String s) {
		StringBuilder retVal = new StringBuilder();

		for (String spl : s.split("\n")) {

			int n = 0;
			for (String i : spl.split(" ")) {

				if (n + i.length() < 45) {
					n += i.length();
					retVal.append(i + " ");
				} else {
					retVal.append(i + "\n");
					n = 0;
				}
			}
			retVal.append("\n");
		}
		return retVal.toString();
	}

	public boolean equals(Object other) {
		if (other instanceof Spell) {
			Spell o = (Spell) other;

			if (!name.equals(o.getName())) {
				return false;
			}

			if (!description.equals(o.getDescription())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Compare To
	 * 
	 * @param other Another spell to compare to this one.
	 */
	public int compareTo(Spell other) {
		if (this.equals(other)) {
			return 0;
		}

		// No two spells have the same name. This is enough for sorting.
		return name.compareTo(other.getName());
	}

	/**
	 * Recieves a class string as a parameter, and returns true if this spell can be
	 * cast by that class.
	 * 
	 * @param classToCast
	 * @return true that class can cast this spell.
	 */
	public boolean canClassCast(String classToCast) {
		switch (classToCast.toLowerCase()) {
		case "bard":
			return this.bardable;
		case "cleric":
			return this.clericable;
		case "druid":
			return this.druidable;
		case "paladin":
			return this.paladinable;
		case "ranger":
			return this.rangerable;
		case "warlock":
			return this.warlockable;
		case "sorcerer":
			return this.sorcerable;
		case "wizard":
			return this.wizardable;
		}
		return false;
	}

	public String getSchool() {
		return this.school;
	}

	public String getCastingTime() {
		return this.castTime;
	}

	public String getComponents() {
		return this.components;
	}

	public String getDuration() {
		return this.duration;
	}

	public String getRange() {
		return this.range;
	}

	public boolean isRitual() {
		return this.isRitual;
	}
	
	public boolean wasParsed() {
		return parsed;
	}

	/**
	 * Sets the level to be whatever the user enters. Level must be between 
	 * 
	 * @param i
	 */
	public void setLevel(int i) {
		if(i >= 0 && i < 10) {
			spellLevel = i;
		} else {
			// throw new LevelException
		}
	}

	public void setSchool(String string) {
		if( string != null) {
			this.school = string;
		}
	}

	public void setCastTime(String string) {
		if(string != null) {
			castTime = string;
		}
	}

	public void setDuration(String string) {
		if(string != null) {
			duration = string;
		}
	}

	public void setRange(String string) {
		if(string != null) {
			range = string;
		}
	}
}
