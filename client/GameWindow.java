/*
 * GUI for the game client
 */

// supercomfy gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

public class GameWindow extends JFrame implements ActionListener {
	// parent object
	MinigameClient parent;

	// size parameters
	public final int GUIWIDTH;
	public final int GUIHEIGHT;
	public final int GAMEWIDTH;
	public final int PANELWIDTH;

	// constructor
	public GameWindow(MinigameClient parent) {
		this.parent = parent;
		this.GUIWIDTH = parent.GUIWIDTH;
		this.GUIHEIGHT = parent.GUIHEIGHT;
		this.GAMEWIDTH = parent.GAMEWIDTH;
		this.PANELWIDTH = GUIWIDTH - GAMEWIDTH;

		// content pane layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cstr = new GridBagConstraints();

		// add game to content pane
		cstr.gridx = 0;
		cstr.gridy = 0;
		cstr.weightx = 1;
		cstr.weighty = 1;
		cstr.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(parent.game, cstr);

		// make right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		// add panel to content pane 
		cstr.gridx = 1;
		cstr.gridy = 0;
		cstr.weightx = 0;
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
