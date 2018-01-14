/* 
 * Connection to gameserver
 */

// std library imports
import java.util.*;

// networking
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class ServerConnection {
	MinigameClient parent;

	// packet templates
	private final String[] GAMECOMPLETE = {
		"GAMECOMPLETE\r\n",
		"PNAME: ",
		"SESSIONID: ",
		"GSCORE: ",
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
			System.out.println("ERROR: Could not connect to server.");
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
	public void sendGameComplete(String pn, String sID, int score) {
		// make copy of packet template
		String[] packet = GAMECOMPLETE.clone();

		// add pName
		packet[1] += pn + "\r\n";
		// add sessionID
		packet[2] += sID + "\r\n";
		// add gamescore
		packet[3] += score + "\r\n";

		// put packet into send queue
		out.queuePacket(packet);
	} // GAMECOMPLETE


	// SESSIONCONNECT message
	public void sendSessionConnect(String pn, String sID) {
		// make copy of packet template
		String[] packet = SESSIONCONNECT.clone();

		// add pName
		packet[1] += pn + "\r\n";
		// add sessionID
		packet[2] += sID + "\r\n";

		// queue packet for sending
		out.queuePacket(packet);
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
			// send packets
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

			// read incoming lines
			try { dataQueue.add(reader.readLine()); } catch (Exception e) { 
				System.out.println("ERROR: Could not read input.");
			}

			// try to split dataQueue into packets
			parseDataQueue();

			// send to mediator if full packet waiting
			if (packetQueue.toArray().length != 0) {
				String[] packet = getNextPacket();
				Packet pck = new Packet(packet);
				Mediator.getInstance().sendPacket(pck);
			}
		} // thread loop
	} // run()


	// read data queue and split it into discrete packets if possible
	public void parseDataQueue() {
		// if end of message found
		if (dataQueue.toArray()[dataQueue.size() - 1].equals("END")) {
			System.out.println("INFO: Packet received from server.");

			// add packet to packetQueue
			packetQueue.add(dataQueue.toArray(new String[dataQueue.size()]));

			// clear dataQueue
			dataQueue.clear();
		}	
	} // parseQueue


	// extract first packet from packetQueue
	public String[] getNextPacket() {
		String[] packet = (String[]) packetQueue.toArray()[0];
		packetQueue.remove(packet);
		return packet;
	} // getNextPacket
} // ClientIn
