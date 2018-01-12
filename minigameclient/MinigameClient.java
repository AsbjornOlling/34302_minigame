/*
 * Client for the minigame-game
 * master class that handles all the things
 */

package minigameclient;
import minigameclient.*;

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
	GameWindow window;

	// gamestate vars
	String currentPName;
	String currentSessionID;
	
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
