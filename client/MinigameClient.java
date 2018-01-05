/*
 * Client for the minigame-game
 * master class that handles all the things
 */

// all the good things
import java.util.*;

public class MinigameClient {
	// connection object
	ServerConnection server;
	GameWindow wind;
	
	// constructor
	public MinigameClient() {
		// init gui
		wind = new GameWindow();

		// init ServerConnection
		server = new ServerConnection();

		// init GameHandler
	} // constructor

	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main

}
