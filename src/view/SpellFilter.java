package view;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SpellFilter extends JPanel{

	private static final long serialVersionUID = 1L;
	private static SpellFilter filter = null;
	private String[] levels;
	private String[] schools;
	private String[] classes;


	private JLabel title;
	private JComboBox<String> levelsBox;
	private JComboBox<String> schoolsBox;
	private JComboBox<String> classesBox;
	private JRadioButton dmgBtn;
	private JRadioButton matrlBtn;
	private JLabel dmgLabel;
	private JLabel matLabel;
	private JPanel radioBtnPanel;

	private JPanel content;
	private JButton goBtn;
	private JButton clear;

	private SpellFilter() {
		createComponents();
		setComponents();
	    setLayouts();
		addComponents();
		addListeners();
		
		this.add(content, BorderLayout.WEST);
		this.setVisible(false);
	}

	private void setComponents() {
		content.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
	}

	private void setLayouts() {
		this.setLayout(new BorderLayout());
		content.setLayout(new GridBagLayout());
		radioBtnPanel.setLayout(new GridBagLayout());
		
	}

	private void addListeners() {
		Action action = new Action();
		goBtn.addActionListener(action);
		clear.addActionListener(action);
	}

	public static SpellFilter getFilter() {
		if (filter == null) {
			filter = new SpellFilter();
		}
		return filter;
	}

	private void addComponents() {
		radioBtnPanel.add(dmgBtn);
		radioBtnPanel.add(dmgLabel);
		radioBtnPanel.add(matrlBtn);
		radioBtnPanel.add(matLabel);
		content.add(title);
		content.add(levelsBox);
		content.add(schoolsBox);
		content.add(classesBox);
		content.add(radioBtnPanel);
		content.add(goBtn);
		content.add(clear);
		
	}

	private void createComponents() {
		title = new JLabel("Filter");
		levels = new String[] { "Level", "Cantrip", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		schools = new String[] { "School", "Abjuration", "Conjuration", "Divination", "Enchantment", "Evocation",
				"Illusion", "Necromancy", "Transmutation" };
		classes = new String[] { "Class", "Bard", "Cleric", "Druid", "Paladin", "Ranger", "Warlock", "Sorcerer",
				"Wizard" };

		levelsBox = new JComboBox<>(levels);
		schoolsBox = new JComboBox<>(schools);
		classesBox = new JComboBox<>(classes);

		goBtn = new JButton("Go");
		clear = new JButton("Clear");
		
		content = new JPanel();
		
		dmgBtn = new JRadioButton();
		matrlBtn = new JRadioButton();
		dmgLabel = new JLabel("Deals damage");
		matLabel = new JLabel("Needs Material");
		radioBtnPanel = new JPanel();
		
	}

	private class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if( ((JButton) e.getSource()).getText().equals("Go")) {
				String levelPick = (String) levelsBox.getSelectedItem();
				String classPick = (String) classesBox.getSelectedItem();
				String schoolPick = (String) schoolsBox.getSelectedItem();

				String l = levelPick.equals("Level") ? null : levelPick;
				String c = classPick.equals("Class") ? null : classPick.trim();
				String s = schoolPick.equals("School") ? null : schoolPick;
				
				HomeScreen.applyFilter(l, c, s);
			} else if(((JButton) e.getSource()).getText().equals("Clear")) {
				HomeScreen.refreshList();
				levelsBox.setSelectedIndex(0);
				classesBox.setSelectedIndex(0);
				schoolsBox.setSelectedIndex(0);
			}
		}
	}

	public void showFilter() {
		this.setVisible(true);
	}

	public void hideFilter() {
		this.setVisible(false);
	}
}
