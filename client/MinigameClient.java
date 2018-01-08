/*
 * Client for the minigame-game
 * master class that handles all the things
 */

// all the good things
import java.util.*;

public class MinigameClient {
	// some GUI parameters	
	public final int GUIWIDTH = 1280;
	public final int GUIHEIGHT = 720;
	public final int GAMEWIDTH = 800;

	// main essential objects
	GameHandler game;
	ServerConnection server;
	GameWindow wind;
	

	// constructor
	public MinigameClient() {
		// game handler object
		game = new GameHandler(this);

		// init gui
		wind = new GameWindow(this);

		// init ServerConnection
		server = new ServerConnection(this);
	} // constructor


	// main method
	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main
} // MinigameClient
