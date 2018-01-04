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
	ServerConnection server;
	
	// constructor
	public MinigameClient() {
		// init gui stuff
		initGUI();

		// init ServerConnection
		server = new ServerConnection();

		// init GameHandler
	} // constructor

	// event handler
	public void actionPerformed(ActionEvent e) {
	} // event handler

	public void initGUI() {
		getContentPane().setLayout(new GridLayout(2, 1));

	} // initGUI

	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main

}
