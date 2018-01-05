/*
 * This is the main class of the 
 * MiniGames server part
 */

// Importing important imports
import java.io.*;
import java.util.*;
import java.net.*;

public class MinigameServer {
	
		public static void main(String[] args) {
		
			// Calling the cunstructor in ClientConnection
			ClientConnection connection = new ClientConnection(6666);

			// Recives packages from client
			// as long as the server is active
			// This is permanent right now
			while (connection.serverActive == true) {
			
				// Open socket and wait for package to arrive
				ArrayList<String> incommingPackage = connection.inPackage();

				// Gonna call the the GameSession when that is made
				System.out.println("I should contact GameSession now");
			}
			
			connection.closeSocket();
				
			System.out.println("Connection closed");
			// Connection closed

	} // Main
} // Class
