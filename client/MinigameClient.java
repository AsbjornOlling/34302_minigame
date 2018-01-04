/*
 * Client for the minigame-game
 */

// all the good things
import java.util.*;

public class MinigameClient {
	// connection object
	ServerConnection server;
	
	// constructor
	public MinigameClient() {
		// init gui
		GameWindow wind = new GameWindow();

		// init ServerConnection
		server = new ServerConnection();

		// init GameHandler
	} // constructor

	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main

}
