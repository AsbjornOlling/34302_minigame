/* 
 * Class for gameSession objects:
 * A gamesession object contains 
 * 	any number of clients
 * 	a list of games to play
 * 	a score table
 */


import java.util.*;
import java.io.*;

public class GameSession {
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

		// put session into main session register
		parent.sessions.put(sessionID, this);

		// TODO make gamesList
	} // constructor


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


	// try to connect to session
	public void sessionConnect(Client client) {
		if (inLobby) {
			clients.add(client);
			scoreboard.put(client.pName, 0);
		} else {
			// game in progress
			// tell user to fuck off
		}
	} // sessionConnect

} // GameSession
