/*
 * Listens for new clients to connect
 */

// Importing important imports
import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class Listener implements Runnable {
	MinigameServer parent;

	// Socket fields
	Socket clientSocket;
	ServerSocket serverSocket;

	// Constructor that makes the server listen
	public Listener(MinigameServer parent) {
	this.parent = parent;
	int port = parent.PORT;

		// make server socket
		try { 
			serverSocket = new ServerSocket(port);
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not establish socket on port "+port);
		}
	} // constructor


	// thread loop
	public void run() {
		boolean shouldRun = true;
		while (shouldRun) {
			// wait for new connection
			try { 
				clientSocket = serverSocket.accept();
			} catch (IOException ioEx) {
				System.out.println("ERROR: Client could not connect to server.");
			}

			// make new client connection
			Client aClient = new Client (parent, clientSocket);
			Thread cThread = new Thread(aClient);
			cThread.start();
			System.out.println("NEW CLIENT CONNECTION");
		}
	} // run
} // Class Listener
