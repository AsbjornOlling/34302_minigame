/*
 * Client for the minigame-game
 * master class that handles all the things
 */

package minigame.client;
import minigame.client.*;

// std lib imports
import java.util.*;

public class MinigameClient {
	// some GUI parameters	
	public final int GUIWIDTH = 1280;
	public final int GUIHEIGHT = 720;
	public final int GAMEWIDTH = 800;

	// main essential objects
	GameHandler game;
	ServerConnection server;
	GameWindow window;

	String pName;
	String sessionID;

	// constructor
	public MinigameClient() {
		game = new GameHandler(this);
		window = new GameWindow(this);
		server = new ServerConnection(this);
	} // constructor

	// main method
	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main
} // MinigameClient
