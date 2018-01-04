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
	private final String HOST = "localhost";
	private final int PORT = "6666";

	private Socket connection;
	private BufferedReader in;
	private PrintWriter out;

	// constructor
	public ServerConnection() {
		connect();
	} // constructor

	public void update() {
		// send score
		// read scores of other players
	}

	public void connect() {
		// make connection objects
		connection = new Socket(HOST, PORT);
		in = new BufferedReader(
				 new InputStreamReader(connection.getInputStream()));
		out = new PrintWriter(connection.getOutputStream());
	}

	// join a game session
	public void joinSession(String sessionid) {
	}

	// make a new game session
	public void createSession() {
	}

} // class
