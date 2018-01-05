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

	// constructor
	public GameWindow() {
		// content pane layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cstr = new GridBagConstraints();

		// right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		rightPanel.setSize(50, GUIWIDTH);

		// label
		JLabel statusHeader = new JLabel("Session ID: [PLACEHOLDER]",
																		 JLabel.CENTER);
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.anchor = GridBagConstraints.CENTER;
		cstr.gridx = 0;
		cstr.gridy = 0;
		rightPanel.add(statusHeader, cstr);

		// one text field TODO replace with JTable
		JTextArea textarea = new JTextArea(20, 20);
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.gridx = 0;
		cstr.gridy = 1;
		rightPanel.add(makeScoreboard(), cstr);

		// one button
		JButton button = new JButton("KILL");
		cstr.fill = GridBagConstraints.HORIZONTAL;
		cstr.gridx = 0;
		cstr.gridy = 2;
		rightPanel.add(button, cstr); 

		// add panel to content pane
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.anchor = GridBagConstraints.LINE_END;
		cstr.weightx = 0.5;
		cstr.gridx = 0;
		cstr.gridy = 0;
		getContentPane().add(rightPanel, cstr);

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
			public int getRowCount() { return 30; }
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
