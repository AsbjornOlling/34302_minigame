
// important imports
import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class Client {

	Socket clientSocket;
	ClientIn in;
	ClientOut out;


	// constructor
	public Client (Socket clientSocket) {
		this.clientSocket = clientSocket;

		// make incoming data handler
		in = new ClientIn(clientSocket);	
		Thread inThread = new Thread(in);
		inThread.start();

		// make outgoint data handler
		out = new ClientOut(clientSocket);
		Thread outThread = new Thread(out);
		outThread.start();

	} //Client


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


// concurrent input reader for Client
class ClientIn implements Runnable {
	BufferedReader inReader;
	ArrayList<String> dataQueue;
	ArrayList<String[]> packetQueue;// Maaske bedre med bare ArrayList af StringArrays


	// constructor
	public ClientIn(Socket clientSocket) {
		// init arraylists
		dataQueue = new ArrayList<String>();
		packetQueue = new ArrayList<String[]>();

		// make reader on clientSocket
		try { 
			inReader = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not read input.");
		}
	} // constructo


	// thread loop
	public void run() {
		// read any incoming lines
		try { 
		dataQueue.add(inReader.readLine());
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not read input.");
		}

		// try to split queue into packets
		parseDataQueue();
	} // thread loop


	// read data queue and split it into discrete packets if possible
	public void parseDataQueue() {

		// if end of message found
		if (dataQueue.toArray()[dataQueue.size() - 1] == "END") {

			packetQueue.add((String[]) dataQueue.toArray()); // Add dataQueue to packetQueue
			dataQueue.clear(); // Remove all elements in dataQueue
		}		
	} // parseQueue


	// get first packet from list of packets
	public String[] getNextPacket() {
		return (String[]) packetQueue.toArray()[0];
	} // getNextPacket
} // ClientIn


// concurrent output writer for Client
class ClientOut implements Runnable {


	// constructor
	public ClientOut(Socket clientSocket) {
		// make printWriter object on clientSocket
	} // constructor


	// thread loop
	public void run() {
		// write any queued packets
	} // thread loop
} // ClientOut
