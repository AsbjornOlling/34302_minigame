import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class ClientConnection {
	boolean serverActive = true; // For the MinigamesServer main file

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

	// Wait for the next incomming package
	// and put it into an arrayList
	public Arraylist<String> inPackage() {
		BufferedReader input = null;
		ArrayList<String> package = new ArrayList<String>();
		
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

	// send a bytearray back
	public void sendResponse(Response response) {
		// open output stream
		BufferedOutputStream output = null;
		try { 
			output = new BufferedOutputStream(clientSocket.getOutputStream());
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not open outputstream to client.");
		}

		// write some shit to client
		// EDIT SHIT L8R
		try { 
			output.write( response.SHIT);
			if ( DEBUG ) System.out.print(response.SHIT);

			output.flush();
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not write data to outputstream.");
		}

		// close the client connection
		try {
			clientSocket.close();
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not close connection to client.");
		} //*/

	} // sendResponse


} // Class
