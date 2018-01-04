/*
 * GUI for the game client
 */

// supercomfy gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements ActionListener {
	// gui params
	public final int GUIWIDTH = 700;
	public final int GUIHEIGHT = 700;

	// constructor
	public GameWindow() {
		// content pane layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cstr = new GridBagConstraints();

		// right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

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
		rightPanel.add(textarea, cstr);
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
		this.setTitle("ITS JUST MINIGAMES OKAY");	
		this.setSize(GUIWIDTH, GUIHEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// show finished window
		this.setVisible(true);
	} // constructor

	// event handler
	public void actionPerformed(ActionEvent e) {
	} // event handler

}
