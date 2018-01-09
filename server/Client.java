// important imports
import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class Client implements Runnable {
	// not really direct parent but whatevs DUDE
	MinigameServer parent;

	// connection objects
	Socket clientSocket;
	ClientIn in;
	ClientOut out;

	// client info
	String pName;

	// constructor
	public Client (MinigameServer parent, Socket clientSocket) {
		this.parent = parent;
		this.clientSocket = clientSocket;

		// make incoming data handler
		in = new ClientIn(clientSocket);	
		Thread inThread = new Thread(in);
		inThread.start();

		// make outgoint data handler
		out = new ClientOut(clientSocket);
		Thread outThread = new Thread(out);
		outThread.start(); //*/
	} //Client


	// wait for first packet to arrive
	// then hand over client to a session
	public void run() {
		// run until fist packet received
		boolean shouldRun = true;
		while (shouldRun) {

			// if the first packet is received
			// this only works with .toArray().length and not with just .size()
			if (in.packetQueue.toArray().length != 0) { 
				System.out.println("FIRST PACKET FROM A CLIENT");
				String[] packet = in.getNextPacket();

				// connect to session
				sessionConnect(packet);
				shouldRun = false;
			} // fi
		} // loop
	} // run()


	// join a session based on session id
	// assume package is valid
	public void sessionConnect(String[] packet) {
		System.out.println("ATTEMPTING SESSION CONNECT");

		// get player name
		pName = packet[1].replace("PNAME: ", "");

		// get session ID
		String sessionID = packet[2].replace("SESSION ID: ", "");
		
		if (sessionID != "NONE") {
			// join an existing session
			GameSession session = parent.sessions.get(sessionID);
			session.sessionConnect(this);
		} else {
			// create new session
			GameSession session = new GameSession(parent);
		}
	} // joinSession


	// terminate connection to client
	public void closeSocket() {
		try { clientSocket.close(); } catch (IOException ioEx) {
			System.out.println("ERROR: Could not close connection to client.");
		}
	} // CloseSocket
} // Class


// concurrent input reader for Client
class ClientIn implements Runnable {
	private BufferedReader inReader;
	private ArrayList<String> dataQueue;
	protected ArrayList<String[]> packetQueue;

	// constructor
	public ClientIn(Socket clientSocket) {
		// init arraylists
		dataQueue = new ArrayList<String>();
		packetQueue = new ArrayList<String[]>();

		// make reader on clientSocket
		try { 
			inReader = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
		} catch (IOException ioEx) {
			System.out.println("ERROR: Could not make input reader.");
		}
	} // constr


	// thread loop
	public void run() {
		while (true) {
			// read any incoming lines
			try { dataQueue.add(inReader.readLine()); } catch (IOException e) { 
				System.out.println("ERROR: Could not read input.");
			}
			// try to split dataQueue into packets
			parseDataQueue();
		} // thread loop
	} // run()


	// read data queue and split it into discrete packets if possible
	public void parseDataQueue() {
		// if end of message found
		if (dataQueue.toArray()[dataQueue.size() - 1].equals("END")) {
			// add packet to packetQueue
			packetQueue.add(dataQueue.toArray(new String[dataQueue.size()]));
			// clear dataQueue
			dataQueue.clear();

			System.out.println("PACKET RECEIVED");
			System.out.println("NO OF UNPULLED PACKETS: " + packetQueue.size());
		}	
	} // parseQueue


	// extract first packet from packetQueue
	public String[] getNextPacket() {
		String[] packet = (String[]) packetQueue.toArray()[0];
		packetQueue.remove(packet);
		System.out.println("PULLING PACKET");
		return packet;
	} // getNextPacket
} // ClientIn


// concurrent output writer for Client
class ClientOut implements Runnable {
// TODO actually write this class

	// constructor
	public ClientOut(Socket clientSocket) {
		// make printWriter object on clientSocket
	} // constructor


	// thread loop
	public void run() {
		// write any queued packets
	} // thread loop
} // ClientOut
