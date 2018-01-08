/* 
 * Connection to gameserver
 */

// small useful things
import java.util.*;

// networking
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class ServerConnection {
	MinigameClient parent;

	// server address
	private final String HOST = "localhost";
	private final int PORT = 6666;

	// connection objects
	private Socket connection;
	private BufferedReader in;
	private PrintWriter out;


	// constructor
	public ServerConnection(MinigameClient parent) {
		this.parent = parent;

		// make connection objects
		connect();
	} // constructor


	// init connection objects
	public void connect() {
		try { // connect to server
			connection = new Socket(HOST, PORT);
		} catch (Exception ex) {
			// tell the user that he dun goofd
		} 
		
		try { // input reader and output writer
			in = new BufferedReader(
					 new InputStreamReader(connection.getInputStream()));
			out = new PrintWriter(connection.getOutputStream());
		} catch (Exception ex) {
			// tell the user that he dun goofd
		}
	} // connect


	// just send one string immediately
	// mainly for testing
	public void sendString(String string) {
		out.print(string+"\r\n");
		out.flush();
	} // sendString


	// send gameComplete message
	public void gameComplete(int score) {
		out.print("GAMECOMPLETE\r\n");
		out.print("PNAME: " + parent.game.pName + "\r\n");
		out.print("GSCORE: " + score + "\r\n");
		out.print("END" + "\r\n");
		out.flush();
	} // gameComplete


	// send and receive data
	public void update() {
		// send score
		// read scores of other players
	} // update


	// join a game session
	public void joinSession(String sessionid) {
		// send player name
	} // joinSession


	// make a new game session
	public void createSession() {
	} // createSession
} // class
