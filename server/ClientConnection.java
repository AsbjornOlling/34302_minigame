import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class ClientConnection {
	// Socket fields
	ServerSocket serverSocket;
	Socket clientSocket;

	// Constructor
	public ClientConnection(int port) {
		try { 
			serverSocket = new ServerSocket(port);
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not establish the connection");
		}
	} // Constructor
} // Class
