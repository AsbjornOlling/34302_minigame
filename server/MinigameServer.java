/*
 * This is the main class of the 
 * Minigames Server
 *
 * Mainly for instantiating the different main objects
 *
 */

import java.io.*;
import java.util.*;

public class MinigameServer {
	public final int PORT = 6666;

	// wordlist for session IDs
	String[] wordlist;

	// main objects
	Listener newConnects;
	HashMap<String,GameSession> sessions;
	HashMap<String,Client> allClients;

	// constructor
	public MinigameServer() {
		// init wordlist array
		try { loadWordList(); } catch (Exception e) {
			System.out.println("ERROR: Could not read wordlist");
		}

		// init hashmaps
		sessions = new HashMap<String,GameSession>();
		allClients = new HashMap<String,Client>();

		// listener object
		newConnects = new Listener(this);
		Thread cThread = new Thread(newConnects);
		cThread.start();
	} // constructor


	// load list of words into String[] wordlist
	public void loadWordList() throws IOException {
		FileReader fr = new FileReader("wordlist.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> lines = new ArrayList<String>();
		String line = null;

		// read file, add to arraylist
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		// convert arraylist to array and put into object field
		wordlist = lines.toArray(new String[lines.size()]);
	} // loadWordList


	public static void main(String[] args) {
		MinigameServer serverInstance = new MinigameServer();
	} // Main
} // Class
