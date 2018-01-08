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

	ArrayList<Object> clients;					// all connected clients
	HashMap<String,Integer> scoreboard; // scoreboard...
	Integer[] gamesList;								// list of game ids to play
	String sessionID;

	int gameLength = 1; 				// number of minigames to play
	boolean lobbyMode = true;		// allow other players to join


	// constructor
	public GameSession(MinigameServer parent) {
		this.parent = parent;

		// make randomizer
		r = new Random();

		// init lists
		clients = new ArrayList<Object>();
		scoreboard = new HashMap<String,Integer>();

		// generate session ID
		sessionID = genSessionID();
		System.out.println("SESSION MADE: "+sessionID);
	} // constructor


	// generate session ID from wordlist
	public String genSessionID() {
		int idLength = 3;
		String sessionID = "";
		// get some random words from the wordlist
		for (int i = 0; i < idLength; i++) {
			sessionID += parent.wordlist[r.nextInt(parent.wordlist.length)];
			// add space if not the last word
			if (i + 1 < idLength) {
				sessionID += " ";
			}
		}
		return sessionID;
	} // genSessionID

} // GameSession
