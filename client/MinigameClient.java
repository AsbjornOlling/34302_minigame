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
		// init ServerConnection
		server = new ServerConnection();
		// init GameHandler
	} // constructor

	// event handler
	public void actionPerformed(ActionEvent e) {
	} // event handler

	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main

}
