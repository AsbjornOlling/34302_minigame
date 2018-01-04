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
		// TODO write code
		
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

		// one text field
		JTextArea textarea = new JTextArea();
		cstr.fill = GridBagConstraints.VERTICAL;
		cstr.gridx = 1;
		cstr.gridy = 0;
		getContentPane().add(textarea);

		// title, size, and closeoperation
		this.setTitle("ITS JUST MINIGAMES OKAY");	
		this.setSize(GUIWIDTH, GUIHEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} // initGUI

	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main

}
