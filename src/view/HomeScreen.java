package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import encyclopedia.Spell;
import encyclopedia.SpellEncyclopedia;
import io.FileHandler;
// FIXME Filter is Broken?
@SuppressWarnings("serial")
public class HomeScreen extends JFrame {

	private JMenuItem editorMode;
	private JMenuItem jsonExport;
	private JMenuItem dndExport;
	private JMenuItem textExport;
	private JMenu menu;
	private JMenuBar menuBar;
	private JPanel menuPanel;
	
	private SpellListener listen;
	private SpellEncyclopedia encl;
	private SpellFilter filter;
	private JPanel homePanel;
	private JPanel mainPanel;
	private JPanel descPanel;
	private JPanel filterPanel;
	private JPanel nBuffer;
	private JPanel wBuffer;

	private JScrollPane descScrollPane;
	private JTextArea descContent;

	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private static JList<String> list;

	private JTextField searchBar;
	private JButton filterBtn;
	private JPanel editorControls;
	private JButton saveBtn;
	private JButton revertBtn;
	
	private Spell editing;
	private boolean isEditMode; 

	public HomeScreen() {

		createComponents();
		setComponents();
		setLayouts();
		addComponents();
		addListeners();

		setContentPane(mainPanel);
		setSize(800, 550);
		center();
		setVisible(true);

	}

	/**
	 * refreshes list to a clean start
	 */
	public static void refreshList() {
		list.setModel(SpellEncyclopedia.getInstance().getDefaultListModel());
	}

	/**
	 * rereshes list to an updated list of search results
	 * 
	 * @param toSet
	 */
	public static void refreshList(DefaultListModel<String> toSet) {
		if (toSet != null) {
			list.setModel(toSet);
		}
	}

	public JScrollPane getDescScrollPane() {
		return descScrollPane;
	}

	/**
	 * This method queries the underlying spell list and outputs a list to the
	 * screen that matches the fields passed through in the parameter.
	 * 
	 *  TODO rewrite this spaghetti
	 * 
	 * @param level
	 * @param cClass
	 * @param school
	 */
	public static void applyFilter(String level, String cClass, String school) {
		DefaultListModel<String> filtered = new DefaultListModel<>();
	
		for (Spell e : SpellEncyclopedia.getInstance()) {
			boolean add = true;
	
			if (level != null) {
				switch (level) {
				case "Cantrip":
					add = (e.getLevel() == 0);
					break;
				case "1":
					add = (e.getLevel() == 1);
					break;
				case "2":
					add = (e.getLevel() == 2);
					break;
				case "3":
					add = (e.getLevel() == 3);
					break;
				case "4":
					add = (e.getLevel() == 4);
					break;
				case "5":
					add = (e.getLevel() == 5);
					break;
				case "6":
					add = (e.getLevel() == 6);
					break;
				case "7":
					add = (e.getLevel() == 7);
					break;
				case "8":
					add = (e.getLevel() == 8);
					break;
				case "9":
					add = (e.getLevel() == 9);
					break;
				default:
					add = false;
					break;
				}
			}
	
			if (cClass != null) {
				if (!e.canClassCast(cClass)) {
					add = false;
				}
			}
	
			if (school != null) {
				// To protect against the spells not in the JSON file.
				// Currently only meld into stone
				if (e.getSchool() == null) {
					add = false;
				} else if (!(e.getSchool().equals(school))) {
					add = false;
				}
			}
	
			if (add) {
				filtered.addElement(e.getName());
			}
		}
		refreshList(filtered);
	
	}

	private void createComponents() {
		// Summons SE, if it is missing, builds it from my resoruces
		encl = SpellEncyclopedia.getInstance();
		filter = SpellFilter.getFilter(); // Instantiates the filter and prepares it for use
		homePanel = new JPanel();
		nBuffer = new JPanel();
		wBuffer = new JPanel();
		listen = new SpellListener();
		mainPanel = new JPanel();
		filterPanel = new JPanel();
		descPanel = new JPanel();
		searchBar = new JTextField();
		descContent = new JTextArea(1, 1);
		descScrollPane = new JScrollPane(descContent);
		listModel = encl.getDefaultListModel();
		list = new JList<>(listModel);
		filterBtn = new JButton();

		scrollPane = new JScrollPane(list);

		menuPanel = new JPanel();
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		editorMode = new JMenuItem("Open Editor Mode...");
		jsonExport = new JMenuItem("Export to JSON...");
		dndExport = new JMenuItem("Export to DND...");
		textExport = new JMenuItem("Export to TXT...");

		editorControls = new JPanel();
		revertBtn = new JButton("Revert");
		saveBtn = new JButton("Save");
		

	}

	private void addComponents() { // To layouts
		filterPanel.add(searchBar, BorderLayout.CENTER);
		filterPanel.add(filterBtn, BorderLayout.EAST);
		filterPanel.add(filter, BorderLayout.SOUTH);
	
		editorControls.add(revertBtn);
		editorControls.add(saveBtn);
	
		homePanel.add(filterPanel, BorderLayout.NORTH);
		homePanel.add(scrollPane, BorderLayout.WEST);
		homePanel.add(descPanel, BorderLayout.CENTER);
		homePanel.add(editorControls, BorderLayout.SOUTH);
	
		descPanel.add(wBuffer, BorderLayout.WEST);
		descPanel.add(nBuffer, BorderLayout.NORTH);
		descPanel.add(descScrollPane, BorderLayout.CENTER);
	
		menu.add(editorMode);
		menu.add(jsonExport);
		menu.add(dndExport);
		menu.add(textExport);
		menuBar.add(menu);
		menuPanel.add(menuBar);
	
		mainPanel.add(menuPanel, BorderLayout.NORTH);
		mainPanel.add(homePanel, BorderLayout.CENTER);
	}

	private void setComponents() {
		this.setTitle("Spell Encyclopedia");
		searchBar.setText("Search...");
		searchBar.setName("searchBar");
		filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		filterBtn.setText("Filter");
		editorControls.setVisible(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setPreferredSize(new Dimension(235, 500));
		nBuffer.setBackground(Color.WHITE);
		wBuffer.setBackground(Color.WHITE);
		descContent.setLineWrap(true);
		descContent.setEditable(false);
		descScrollPane.setBorder(null);

		menu.setBackground(Color.WHITE);
		editorMode.setToolTipText("[UNFINISHED] Edit the contents of the SE");
		jsonExport.setToolTipText("[BETA]");
		dndExport.setToolTipText("[BETA]");
		textExport.setToolTipText("[BETA]");
		revertBtn.setEnabled(false);
		saveBtn.setEnabled(false);
	}

	private void setLayouts() {
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		editorControls.setLayout(new FlowLayout(FlowLayout.RIGHT));
		filterPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		homePanel.setLayout(new BorderLayout());
		descPanel.setLayout(new BorderLayout());
	}

	private void addListeners() {
		this.addWindowListener(listen);
		searchBar.addFocusListener(listen);
		searchBar.addKeyListener(listen);
		list.addListSelectionListener(listen);
		filterBtn.addActionListener(listen);
		editorMode.addActionListener(listen);
		jsonExport.addActionListener(listen);
		dndExport.addActionListener(listen);
		textExport.addActionListener(listen);
		saveBtn.addActionListener(listen);
		revertBtn.addActionListener(listen);
		descContent.addKeyListener(listen);
	}

	/**
	 * Centers the frame on the center of the screen laterally, and a fourth of the
	 * way down from the top of the screen.
	 */
	private void center() {
		Dimension dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dimFrameSize = getSize();

		if (dimFrameSize.height > dimScreenSize.height) {
			dimFrameSize.height = dimScreenSize.height;
		}

		if (dimFrameSize.width > dimScreenSize.width) {
			dimFrameSize.width = dimScreenSize.width;
		}

		setLocation((dimScreenSize.width - dimFrameSize.width) / 2, (dimScreenSize.height - dimFrameSize.height) / 4);
	}

	//TODO issue with Animate Objects
	private void showDescription() {
		String spellName = trimHTML(list.getSelectedValue());
		if(isEditMode) {
			editing = encl.get(spellName);
		}
		descContent.setText(encl.get(spellName).toString());
		descContent.setCaretPosition(0);
		setContentPane(mainPanel);
	}

	/**
	 *  Trims HTML markup off of the selected list item, whether there is
	 *  markup there or not.
	 * 
	 * @param toTrim
	 * @return
	 */
	private String trimHTML(String toTrim) {
		if(toTrim == null ) {
			return "";
		}
		if( toTrim.contains("html")) {
			toTrim = toTrim.substring(24);
			toTrim = toTrim.substring(0, toTrim.indexOf("<"));
		}
		return toTrim.trim();
	}

	private class SpellListener
			implements ActionListener, WindowListener, ListSelectionListener, FocusListener, KeyListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				JButton btnsrc = (JButton) e.getSource();
				if (btnsrc.getText().equals("Filter")) {
					filter.setVisible(!filter.isVisible());
				} else if (btnsrc.getText().equals("Save")) {
					saveBtn.setEnabled(false);
					revertBtn.setEnabled(false);
				} else if(btnsrc.getText().equals("Revert")) {
					showDescription();
					revertBtn.setEnabled(false);
					saveBtn.setEnabled(false);
				}
			} else if (e.getSource() instanceof JMenuItem) {
				// Main Menu listener
				JMenuItem src = (JMenuItem) e.getSource();
				switch (src.getText()) {
				case "Open Editor Mode...":
					break; // FIXME write Editing 2.0
//					if( list.getSelectedValue() != null) {
//						editing = SpellEncyclopedia.getInstance().get(list.getSelectedValue());
//					}
//					refreshList(SpellEncyclopedia.getInstance().getEditorListModel());
//					descContent.setEditable(true);
//					editorMode.setText("Close Editor Mode...");
//					editorControls.setVisible(true);
//					isEditMode = true;
//					break;
				case "Export to TXT...":
					FileHandler.exportToPlainText("SpellEncyclopedia1.txt");
					break;
				case "Export to DND...":
					System.out.println("Serialize!");
					FileHandler.writeOut("SpellEncyclopdedia_DND_1.dnd");
					break;
				case "Export to JSON...":
					FileHandler.exportToJSON("SpellEncyclopedia1.json");
					break;
				case "Close Editor Mode...":
					refreshList();
					descContent.setEditable(false);
					editorMode.setText("Open Editor Mode...");
					editorControls.setVisible(false);
					editing = null;
					isEditMode = false;
					break;
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getSource() instanceof JTextField) {
				JTextField src = (JTextField) e.getSource();

				// Generates New Search results per key typed
				if (src.getName().equals("searchBar")) {
					// if the search bar is empty, reset list and return. 
					if(src.getText().trim().equals("") ) {
						refreshList();
						return;
					}
					
					if ((int) e.getKeyChar() == 8) { // Backspace
						refreshList(SpellEncyclopedia.getInstance().refreshDefListModel(src.getText()));
					} else {
						String term = src.getText() + e.getKeyChar();
						refreshList(SpellEncyclopedia.getInstance().refreshDefListModel(term));
					}
				}
			} 
			
			
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof JTextField) {
				JTextField src = (JTextField) e.getSource();

				if (src.getName().equals("searchBar")) {
					src.selectAll();
				}
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			FileHandler.writeOut("spells.dnd");
			dispose();
			System.exit(0);
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() instanceof JList) {
				@SuppressWarnings("unchecked")
				JList<String> src = (JList<String>) e.getSource();

				if (!src.getValueIsAdjusting()) {
					if (src.getSelectedValue() != null) {
						showDescription();
						
					}
				}
			}
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

}
