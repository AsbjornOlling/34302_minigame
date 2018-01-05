/*
 * This is the main class of the 
 * MiniGames server part
 */

// Importing important stuff
import java.io.*;
import java.util.*;
import java.net.*;

public class MinigameServer {
	
		public static void main(String[] args) {
		
		ClientConnection connection = new ClientConnection(6666);

		// Recives packages from client
		// as long as the server is active
		// This is permanent right now
		while (connection.ServerActive == true) {
			
			// Open socket and wait for package to arrive
			ArrayList<String> incommingPackage = connection.inPackage();

			// Gonna call the the GameSession when that is made

		}
		
		System.out.println("Connection closed");
		// Connection closed

	} // Main
} // Class
