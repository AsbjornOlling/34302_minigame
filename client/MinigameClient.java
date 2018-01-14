/*
 * Client for the minigame-game
 * master class that handles all the things
 */

// std lib imports
import java.util.*;

public class MinigameClient extends PacketListener {
	// some GUI parameters	
	public final int GUIWIDTH = 1280;
	public final int GUIHEIGHT = 720;
	public final int GAMEWIDTH = 800;

	// main essential objects
	GameHandler game;
	ServerConnection server;
	GameWindow window;

	// game state objects
	// (are accessed from window and serverconnection
	String pName;
	String sessionID;
	Object[][] scoreboard = new Object[2][1];

	// constructor
	public MinigameClient() {
		// main objects
		game = new GameHandler(this);
		window = new GameWindow(this);
		server = new ServerConnection(this);

		// mediator
		String[] hdrs = {"SESSIONJOINED"};
		Mediator.getInstance().addListener(this, hdrs);
	} // constructor

	// update gamestate from packet info
	public void recvPacket(Packet pck) {
		if (pck.HEADER.equals("SESSIONJOINED")) {
			System.out.println("INFO: Received id " + pck.SESSIONID);
			sessionID = pck.SESSIONID;
		} 
		
		else if (pck.HEADER.equals("SCOREUPDATE")) {
			System.out.println("INFO: Got new scoreboard.");
			scoreboard = pck.SCOREBOARD;
		}
	} // recvPacket


	// SHA-BANG
	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	}
} // MinigameClient
