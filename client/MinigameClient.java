/*
 * Client for the minigame-game
 */

// all the good things
import java.util.*;

// supercomfy gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MinigameClient extends JFrame implements ActionListener {
	// connection object
	ServerConnection server;
	// gui params
	public final int GUIWIDTH = 500;
	public final int GUIHEIGHT = 500;
	
	// constructor
	public MinigameClient() {
		// init gui stuff
		initGUI();

		// init ServerConnection
		server = new ServerConnection();

		// init GameHandler
		// TODO write class
		
		// show finished window
		this.setVisible(true);
	} // constructor

	// event handler
	public void actionPerformed(ActionEvent e) {
	} // event handler

	public void initGUI() {
		// layout
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cstr = new GridBagConstraints();

		// right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

		// label
		JLabel statusHeader = new JLabel("Session ID: [PLACEHOLDER]");
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.anchor = GridBagConstraints.CENTER;
		cstr.gridx = 0;
		cstr.gridy = 0;
		rightPanel.add(statusHeader, cstr);
		// one text field
		JTextArea textarea = new JTextArea(20, 20);
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.gridx = 0;
		cstr.gridy = 1;
		rightPanel.add(textarea, cstr);
		// one button
		JButton button = new JButton("BUTTON");
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
	} // initGUI

	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main

}
