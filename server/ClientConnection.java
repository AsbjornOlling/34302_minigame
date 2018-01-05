/*
 * Handels connection from the game client
 * Reads the incomming packages from the game client
 * 
 */

import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class ClientConnection {
	boolean serverActive = true; // For the MinigamesServer main file
	boolean DEBUG = true; // Debug parameter

	// Socket fields
	ServerSocket serverSocket;
	Socket clientSocket;

	// Constructor that makes the server listen
	public ClientConnection(int port) {
		try { 
			serverSocket = new ServerSocket(port);
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not establish socket on port "+port);
		}
	} // Constructor

	// Wait for the next incomming package
	// and put it into an arrayList
	public ArrayList<String> inPackage() {
		BufferedReader input = null;
		ArrayList<String> request = new ArrayList<String>();
		
		// wait for client to connect
		try { 
			clientSocket = serverSocket.accept();
		} catch (IOException ioEx) {
			System.out.println("ERROR: Client could not connect to server.");
		}

		// read the request
		try {
			String line;
			input = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			while ( !( (line = input.readLine()) == null ) ) {
				if ( line.isEmpty() ) break;
				if (DEBUG) System.out.println(line);
				request.add(line);
			}
			if (DEBUG) System.out.print("\n");
		} catch (IOException ioEx ) {
			System.out.println("ERROR: Could not read line from InputStream");
		}

		return request;
	} //inPackage

	// Close the server socket
	public void closeSocket() {

		// close the client connection
		try {
			clientSocket.close();
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not close connection to client.");
		} //*/

	} // CloseSocket


} // Class
