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

	// packet templates
	private final String[] SESSIONJOINED = {
		"SESSIONJOINED\r\n",
		"GAMES: ",
		"SESSIONID: ",
		"END\r\n"
	};
	private final String[] SCOREUPDATE = {
		"SCOREUPDATE\r\n",
		"PNAME: ",		// repeat these two lines for number of players
		"PSCORE: ",		// repeat these two lines for number of players
		"END\r\n"
	};
	private final String[] GAMESTART = {
		"GAMESTART\r\n",
		"END\r\n"
	};

	Client host;												// creator of the session
	ArrayList<Client> clients;					// all connected clients
	HashMap<String,Integer> scoreboard; // table of points (PName:Points)
	String gamesList;										// list of game ids to play

	String sessionID;						// id for players to join
	int gameLength = 8; 				// number of minigames to play
	boolean inLobby = true;			// allow other players to join


	// constructor
	public GameSession(MinigameServer parent, Client host) {
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
		String[] hdrs = {"SESSIONCONNECT", 
										 "GAMECOMPLETE",
										 "GAMESTART",
										 "QUIT"};
		Mediator.getInstance().addListener(this, hdrs, sessionID);

		// generate gamesList
		gamesList = genGamesList();

		// add host
		this.host = host;
		addClient(host, host.pName);
	} // constructor

	
	// receive packets from mediator
	public void recvPacket(Packet pck) {
		// SESSIONCONNECT
		if (pck.HEADER.equals("SESSIONCONNECT")
				&& pck.SESSIONID.equals(this.sessionID)) {
			addClient(pck.SOURCE, pck.PNAME);
		} // SESSIONCONNECT

		// GAMECOMPLETE
		else if (pck.HEADER.equals("GAMECOMPLETE")
						 && pck.SESSIONID.equals(this.sessionID)) {
			System.out.println("INFO: " + pck.PNAME + " received " + pck.GSCORE + "points.");

			// log points
			int newPoints = scoreboard.get(pck.PNAME);
			newPoints += pck.GSCORE;
			scoreboard.put(pck.PNAME, newPoints);
										 
			// udpate client scoreboards
			broadcastScoreUpdate();
		} // GAMECOMPLETE

		// GAMESTART
		else if (pck.HEADER.equals("GAMESTART")) {
			if (pck.SOURCE == host) {
				broadcastGameStart();
			}
		} // GAMESTART

		// QUIT
		else if (pck.HEADER.equals("QUIT")) {
			System.out.println("INFO: Removing a client.");

			// remove client from session
			clients.remove(pck.SOURCE);
			// remove client from server
			parent.allClients.remove(pck.SOURCE);

			// stop client threads, close socket
			pck.SOURCE.kill();
		}
	} // recvPacket

	public void broadcastGameStart() {
		System.out.println("INFO: Broadcasting GAMESTART.");
		
		// send simple GAMESTART packet
		for (Client c : clients) {
			c.out.queuePacket(GAMESTART);
		}
	}

	// broadcast scoreboard to all players
	private void broadcastScoreUpdate() {
		System.out.println("INFO: Broadcasting scoreboard.");

		// start building new packet
		ArrayList<String> packetList = new ArrayList<String>();
		packetList.add(SCOREUPDATE[0]);

		// build meat of the packet
		for (Client c : clients) {
			packetList.add(SCOREUPDATE[1] + c.pName + "\r\n");
			packetList.add(SCOREUPDATE[2] + scoreboard.get(c.pName) + "\r\n");
		} // loop
		// end packet
		packetList.add(SCOREUPDATE[3]);

		// finalize packet and queue for sending
		String[] packet = packetList.toArray(new String[packetList.size()]);
		for (Client c : clients) {
			c.out.queuePacket(packet);
		} // loop
	} // broadcastScoreboard


	// confirm that the client joined
	// and send player the gamesList
	private void sendSessionJoined(Client c) {
		// make copy of protocol text
		String[] packet = SESSIONJOINED.clone();
		
		// add session properties
		packet[1] += gamesList + "\r\n";
		packet[2] += sessionID + "\r\n";

		// queue packet for sending
		c.out.queuePacket(packet);
	} // sendSessionJoined


	// add client to lists
	// triggered by SESSIONCONNECT packet
	public void addClient(Client client, String name) {
		if (inLobby) {
			System.out.println("INFO: " + name + " has joined session " + this.sessionID);

			// add to list of clients
			clients.add(client);
			// init scoreboard
			scoreboard.put(name, 0);

			// send confirmation to player
			sendSessionJoined(client);

			// update playerlist (and give new client first playerlist)
			broadcastScoreUpdate();
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
		} // loop
		return sessionID;
	} // genSessionID
	

	// generate list of games to play in this session
	private String genGamesList() {
		String gamesList = "";
		int noOfGames = 3;
		int tempNo;
		for (int i = 0; i < noOfGames; i++) {
			tempNo = r.nextInt(gameLength);

			// get a new number, if it was a repeat
			while (gamesList.contains(tempNo+"")) {
				tempNo = r.nextInt(gameLength);
			}

			// add no to string
			gamesList += tempNo+"";

			// add space if not the alst game
			if ( i + 1 < noOfGames) {
				gamesList += " ";
			}
		} // loop
		return gamesList;
	} //genGamesList
} // GameSession
