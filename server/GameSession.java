/* 
 * Class for gameSession objects:
 * A gamesession object contains 
 * 	any number of clients
 * 	a list of games to play
 * 	a score table
 */


import java.util.*;
import java.io.*;

public class GameSession extends PacketListener {
	MinigameServer parent;

	Random r; // randomizer

	ArrayList<Client> clients;					// all connected clients
	HashMap<String,Integer> scoreboard; // a atable of points (PName:Points))
	Integer[] gamesList;								// list of game ids to play

	String sessionID;						// id for players to join with
	int gameLength = 1; 				// number of minigames to play
	boolean inLobby = true;			// allow other players to join


	// constructor
	public GameSession(MinigameServer parent) {
		this.parent = parent;

		// make randomizer
		r = new Random();

		// init lists
		clients = new ArrayList<Client>();
		scoreboard = new HashMap<String,Integer>();

		// generate session ID
		sessionID = genSessionID();
		System.out.println("SESSION MADE: "+sessionID);

		// put into main session register
		parent.sessions.put(sessionID, this);
		// add to mediator
		String[] hdrs = {"SESSIONCONNECT"};
		Mediator.getInstance().addListener(this, hdrs, sessionID);

		// TODO generate gamesList
	} // constructor

	
	// receive packets from mediator
	public void recvPacket(Packet pck) {
		if (pck.HEADER == "SESSIONCONNECT") {
			// TODO find client somehow
			// maybe based on a pname through a bigger hashmap?
		}
	} // recvPacket


	// add client to lists
	// triggered by SESSIONCONNECT packet
	public void addClient(Client client) {
		if (inLobby) {
			clients.add(client);
			scoreboard.put(client.pName, 0);

			System.out.println("PLAYER " + client.pName 
												 + "JOINED SESSION " + sessionID);
		} else {
			System.out.println("SESSION NOT JOINABLE");
		}
	} // addClient


	// generate session ID from wordlist
	private String genSessionID() {
		int idLength = 3;
		String sessionID = "";
		// get some random words from the wordlist
		for (int i = 0; i < idLength; i++) {
			sessionID += parent.wordlist[r.nextInt(parent.wordlist.length)];
			// add a space if not the last word
			if (i + 1 < idLength) {
				sessionID += " ";
			}
		}
		return sessionID;
	} // genSessionID
} // GameSession
