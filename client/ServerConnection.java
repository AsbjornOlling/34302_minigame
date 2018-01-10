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
	MinigameClient parent;

	private final String[] GAMECOMPLETE = {
		"GAMECOMPLETE\r\n",
		"PNAME: ",
		"SESSIONID: ",
		"END\r\n"
	};

	private final String[] SESSIONCONNECT = {
		"SESSIONCONNECT\r\n",
		"PNAME: ",
		"SESSIONID: ",
		"END\r\n"
	};

	// server address
	private final String HOST = "localhost";
	private final int PORT = 6666;

	// connection objects
	private Socket connection;
	private ServerIn in;
	private ServerOut out;

	// constructor
	public ServerConnection(MinigameClient parent) {
		this.parent = parent;

		// open connection to server
		try { connection = new Socket(HOST, PORT); } catch (Exception ex) {
			System.out.println("ERROR: COULD NOT CONNECT TO SERVER");
		}

		// output object
		out = new ServerOut(connection);
		Thread outThread = new Thread(out);
		outThread.start();

		// input object
		in = new ServerIn(connection);
		Thread inThread = new Thread(in);
		inThread.start();
	} // constructor


	// send GAMECOMPLETE message
	public void sendGameComplete(int score) {
		// make copy of packet template
		String[] packet = GAMECOMPLETE.clone();

		// add pName
		// add sessionID
		// add gamescore
	} // GAMECOMPLETE


	// SESSIONCONNECT message
	public void sendSessionConnect() {
		// make copy of packet template
		String[] packet = GAMECOMPLETE.clone();

		// add pName
		// add sessionID
	} // SESSIONCONNECT

} // class


// object to send info to server
class ServerOut implements Runnable {
	private PrintWriter writer;	
	ArrayList<String[]> packetQueue;

	// cosntructor
	public ServerOut(Socket s) {
		// printwriter to write to server	
		try { writer = new PrintWriter(s.getOutputStream()); } 
		catch (Exception e) {
			System.out.println("ERROR: Could not open output writer.");
		}
		// queue of packets to send
		packetQueue = new ArrayList<String[]>();
	} // constructor


	// thread loop
	public void run() {
		boolean shouldRun = true;
		while (shouldRun) {
			// only works with .toArray().length, and NOT with .size()
			if (packetQueue.toArray().length != 0) { 
				sendNextPacket();
			}
		} // thread loop
	} // run()


	// extract a packet and send to client
	private void sendNextPacket() {
		String[] packet = packetQueue.get(0);
		packetQueue.remove(packet);
		sendPacket(packet);
	} // sendNextPacket()


	// send a packet to the client
	private void sendPacket(String[] packet) {
		System.out.println("SENDING PACKET TO CLIENT");

		// write line-by-line
		for (int i = 0; i < packet.length; i++) {
			String line = packet[i];
			writer.print(line);
		}
		writer.flush();
	} // sendPacket


	// add a packet to the queue
	public void queuePacket(String[] packet) {
		packetQueue.add(packet);
	} // queuePacket
} // serverout


// concurrent input reader for Client
class ServerIn implements Runnable {
	private BufferedReader reader;
	private ArrayList<String> dataQueue;
	protected ArrayList<String[]> packetQueue;

	// constructor
	public ServerIn(Socket s) {
		// init arraylists
		dataQueue = new ArrayList<String>();
		packetQueue = new ArrayList<String[]>();

		// make reader on clientSocket
		try { 
			reader = new BufferedReader(
							 new InputStreamReader(s.getInputStream()));
		} catch (Exception e) {
			System.out.println("ERROR: Could not make input reader.");
		}
	} // constr


	// thread loop
	public void run() {
		boolean shouldRun = true;
		while (shouldRun) {

			// read any incoming lines
			try { dataQueue.add(reader.readLine()); } catch (Exception e) { 
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

			System.out.println("COMPLETE PACKET RECEIVED");
		}	
	} // parseQueue


	// extract first packet from packetQueue
	public String[] getNextPacket() {
		String[] packet = (String[]) packetQueue.toArray()[0];
		packetQueue.remove(packet);
		return packet;
	} // getNextPacket
} // ClientIn
