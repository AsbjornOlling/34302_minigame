/*
 * This file is for non-game UI elements
 */

package client;

// supercomfy gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

/*
 * The main game window.
 *
 * This class encapsulates all visible elements.
 */
public class GameWindow extends JFrame implements ActionListener {
	// parent object
	MinigameClient parent;

	// layout objects
	GridBagConstraints cstr;
	JPanel mainPanel;

	// size parameters
	public final int GUIWIDTH;
	public final int GUIHEIGHT;
	public final int GAMEWIDTH;
	public final int PANELWIDTH;

	// make main elements of the client window
	public GameWindow(MinigameClient parent) {
		this.parent = parent;
		this.GUIWIDTH = parent.GUIWIDTH;
		this.GUIHEIGHT = parent.GUIHEIGHT;
		this.GAMEWIDTH = parent.GAMEWIDTH;
		this.PANELWIDTH = GUIWIDTH - GAMEWIDTH;

		// init layout stuff
		getContentPane().setLayout(new GridBagLayout());
		cstr = new GridBagConstraints();

		// main content panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		cstr.gridx = 0;
		cstr.gridy = 0;
		cstr.weightx = 1;
		cstr.weighty = 1;
		cstr.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(mainPanel, cstr);

		// loadGameHandler();
		loadIdleScreen();

		// make and add right panel
		JPanel rightPanel = makeRightPanel();
		cstr.gridx = 1;
		cstr.gridy = 0;
		cstr.weightx = 0;
		cstr.weighty = 1;
		cstr.fill = GridBagConstraints.VERTICAL;
		getContentPane().add(rightPanel, cstr);

		// title, size, and closeoperation
		setTitle("ITS JUST MINIGAMES OKAY");	
		setSize(GUIWIDTH, GUIHEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// show finished window
		setVisible(true);
	} // constructor


	// load GameHandler instance
	public void loadGameHandler() {
		mainPanel.removeAll();
		mainPanel.add(parent.game);
	} // loadGameHandler


	// create and show idle screen
	public void loadIdleScreen() {
		mainPanel.removeAll();
		mainPanel.add(new IdleScreen(this));
	} // loadIdleScreen


	// right UI panel containing scoreboard, session ID, etc
	public JPanel makeRightPanel() {
		// make panel and set layout
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

		// label at top of the right panel
		JLabel statusHeader = new JLabel("Session ID: [PLACEHOLDER]",
																		 JLabel.CENTER);
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.weightx = 0;
		cstr.weighty = 0;
		cstr.gridx = 0;
		cstr.gridy = 0;
		rightPanel.add(statusHeader, cstr);

		// add scoreboard in the middle
		cstr.fill = GridBagConstraints.HORIZONTAL;
		cstr.gridx = 0;
		cstr.gridy = 1;
		cstr.weightx = 0;
		cstr.weighty = 1;
		JTable scoreboard = makeScoreboard();
		scoreboard.setPreferredSize(new Dimension(PANELWIDTH, GUIHEIGHT));
		rightPanel.add(scoreboard, cstr);

		// and one button in the bottom
		JButton button = new JButton("Exit");
		cstr.fill = GridBagConstraints.HORIZONTAL;
		cstr.gridx = 0;
		cstr.gridy = 2;
		cstr.weightx = 1;
		cstr.weighty = 0;
		rightPanel.add(button, cstr); 

		return rightPanel;
	} // make right panel

	// make empty scoreboard (called from makeRightPanel)
	public JTable makeScoreboard() {
		TableModel tablemodel = new AbstractTableModel() {
			public int getColumnCount() { return 2; }
			public int getRowCount() { return 10; }
			public Object getValueAt(int row, int col) { 
				return new Integer(row*col); 
			}
		};
		JTable table = new JTable(tablemodel);
		return table;
	} // makeScoreboard()


	// event handler
	public void actionPerformed(ActionEvent e) {
	} // event handler
} // GameWindow


/*
 * Panel that shows when the player is not in session.
 *
 * Contains two buttons:
 * 	newSession creates a new session on the server.
 * 	joinSession joins a session with a specific name
 */
class IdleScreen extends JPanel implements ActionListener {
	GameWindow parent;
	MinigameClient app;
	private final int WIDTH;
	private final int HEIGHT;

	private JButton newSession;
	private JButton joinSession;

	// constructor
	public IdleScreen(GameWindow parent) {
		this.parent = parent;
		this.app = parent.parent;
		this.WIDTH = parent.GAMEWIDTH;
		this.HEIGHT = parent.GUIHEIGHT;

		setLayout(new GridLayout(2, 1));

		// new session button
		newSession = new JButton("Create new session.");
		newSession.addActionListener(this);
		add(newSession);

		// join session button
		joinSession = new JButton("Join an existing session.");
		joinSession.addActionListener(this);
		add(joinSession);
	} // constructor


	// event handler
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();

		// if it's either of the buttons
		if (source == newSession || source == joinSession) {
			// get player name
			app.currentPName = JOptionPane.showInputDialog("Your name for this sesson:");

			String sID = null;
			if (source == joinSession) {
				sID = JOptionPane.showInputDialog("Session ID to join:");
			} else if (source == newSession) {
				sID = "NONE";
			}
			// send SESSIONCONNECT packet to server.
			app.server.sendSessionConnect(app.currentPName, sID);
		} // if button
	} // event handler


	// tell the layout manager about size
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	} // getPreferredSize
} // IdleScreen
