/*
 * GUI for the game client
 */

// supercomfy gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

public class GameWindow extends JFrame implements ActionListener {
	// gui params
	public final int GUIWIDTH = 1280;
	public final int GUIHEIGHT = 720;
	public final int GAMEWIDTH = 1000;
	public final int PANELWIDTH = GUIWIDTH - GAMEWIDTH;

	// constructor
	public GameWindow() {
		// content pane layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cstr = new GridBagConstraints();

		// make game handler
		GameHandler gameScreen = new GameHandler(GAMEWIDTH, GUIHEIGHT);

		// add game to content pane
		cstr.gridx = 0;
		cstr.gridy = 0;
		cstr.weightx = 1;
		cstr.weighty = 0;
		cstr.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(gameScreen, cstr);

		// make right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

		// add panel to content pane 
		cstr.gridx = 1;
		cstr.gridy = 0;
		cstr.weightx = 1;
		cstr.weighty = 1;
		cstr.fill = GridBagConstraints.VERTICAL;
		getContentPane().add(rightPanel, cstr);

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

		// title, size, and closeoperation
		setTitle("ITS JUST MINIGAMES OKAY");	
		setSize(GUIWIDTH, GUIHEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// show finished window
		setVisible(true);
	} // constructor


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

}
