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
	public final int GAMEWIDTH = 80;

	// constructor
	public GameWindow() {
		// content pane layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cstr = new GridBagConstraints();

		// add main game content
		GameHandler gameScreen = new GameHandler(GAMEWIDTH, GUIHEIGHT);
		cstr.fill = GridBagConstraints.HORIZONTAL;
		cstr.weightx = 0.5;
		cstr.gridx = 0;
		cstr.gridy = 0;
		getContentPane().add(gameScreen, cstr);

		// make right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		rightPanel.setSize(GUIWIDTH - GAMEWIDTH, GUIHEIGHT);
		// add it to content pane
		cstr.fill = GridBagConstraints.HORIZONTAL;
		cstr.anchor = GridBagConstraints.LINE_END;
		cstr.weightx = 0.5;
		cstr.gridx = 1;
		cstr.gridy = 0;
		getContentPane().add(rightPanel, cstr);

		// label at top of the right panel
		JLabel statusHeader = new JLabel("Session ID: [PLACEHOLDER]",
																		 JLabel.CENTER);
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.anchor = GridBagConstraints.CENTER;
		cstr.weightx = 0.5;
		cstr.gridx = 0;
		cstr.gridy = 0;
		rightPanel.add(statusHeader, cstr);

		// add scoreboard in the middle
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.anchor = GridBagConstraints.PAGE_START;
		cstr.gridx = 0;
		cstr.gridy = 1;
		cstr.weightx = 0.5;
		cstr.weighty = 0;
		rightPanel.add(makeScoreboard(), cstr);

		// and one button in the bottom
		JButton button = new JButton("Exit");
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.gridx = 0;
		cstr.gridy = 2;
		rightPanel.add(button, cstr); 

		// add panel to content pane

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
			public int getRowCount() { return 20; }
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
