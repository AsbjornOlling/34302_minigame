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

	String pName;
	String sessionID;

	// constructor
	public MinigameClient() {
		game = new GameHandler(this);
		window = new GameWindow(this);
		server = new ServerConnection(this);

		String[] hdrs = {"SESSIONJOINED"};
		Mediator.getInstance().addListener(this, hdrs);
	} // constructor

	// packet listener
	public void recvPacket(Packet pck) {
		if (pck.HEADER.equals("SESSIONJOINED")) {
		}
	} // recvPacket

	// main method
	public static void main(String[] args) {
		MinigameClient client = new MinigameClient();
	} // main
} // MinigameClient
