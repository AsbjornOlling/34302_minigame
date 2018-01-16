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
	private final String[] GAMESTART = {
		"GAMESTART\r\n",
		"PNAME: ",
		"SESSIONID: ",
		"END\r\n"
	};
	private final String[] QUIT = {
		"QUIT\r\n",
		"PNAME: ",
		"SESSIONID: ",
		"END\r\n"
	};

	// server address
	private final String HOST = "localhost";
	private final int PORT = 6666;

	// connection objects
	protected Socket s;
	private ServerIn in;
	private ServerOut out;
	public boolean shouldRun;

	// constructor
	public ServerConnection(MinigameClient parent) {
		this.parent = parent;

		// open connection to server
		try { s = new Socket(HOST, PORT); } catch (Exception ex) {
			System.out.println("ERROR: Could not connect to server.");
		}

		// start sum shit
		shouldRun = true;

		// output object
		out = new ServerOut(this);
		Thread outThread = new Thread(out);
		outThread.start();

		// input object
		in = new ServerIn(this);
		Thread inThread = new Thread(in);
		inThread.start();
	} // constructor


	// send GAMECOMPLETE message
	public void sendGameComplete(int score) {
		// make copy of packet template
		String[] packet = GAMECOMPLETE.clone();

		packet[1] += parent.pName + "\r\n"; // pName
		packet[2] += parent.sessionID + "\r\n"; // sID
		packet[3] += score + "\r\n"; // GSCORE

		// send packet
		out.queuePacket(packet);
	} // GAMECOMPLETE


	// SESSIONCONNECT message
	public void sendSessionConnect(String pn, String sID) {
		// make copy of packet template
		String[] packet = SESSIONCONNECT.clone();

		packet[1] += pn + "\r\n"; //pName
		packet[2] += sID + "\r\n"; //sessionID

		// queue packet for sending
		out.queuePacket(packet);
	} // SESSIONCONNECT


	// GAMESTART message from host
	public void sendGameStart() {
		// make copy of packet template
		String[] packet = GAMESTART.clone();

		packet[1] += parent.pName + "\r\n"; //pName
		packet[2] += parent.sessionID + "\r\n"; // sessionID

		// queue packet for sending
		out.queuePacket(packet);
	} //GAMESTART

	// QUIT message
	public void sendQuit() {
		// make copy of packet template
		String[] packet = QUIT.clone();

		packet[1] += parent.pName + "\r\n"; //pName
		packet[2] += parent.sessionID + "\r\n"; // sessionID

		// queue packet for sending
		out.queuePacket(packet);
	} //sendQuit

	// close socket
	public void close() {
		System.out.println("INFO: Terminating ServerConnection");
		// close all running threads
		this.shouldRun = false;
		
		// send QUIT message to server
		sendQuit();

		// close socket
		try { s.close(); } catch (Exception e) {
			System.out.println("ERROR: Trouble closing socket.");
		}
	} // close()
} // serverconnection


// object to send info to server
class ServerOut implements Runnable {
	private ServerConnection parent;
	private PrintWriter writer;	
	ArrayList<String[]> packetQueue;

	// cosntructor
	public ServerOut(ServerConnection parent) {
		// parent objects
		this.parent = parent;
		Socket s = parent.s;

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
		while (parent.shouldRun) {
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
	private ServerConnection parent;
	private BufferedReader reader;
	private ArrayList<String> dataQueue;
	protected ArrayList<String[]> packetQueue;

	// constructor
	public ServerIn(ServerConnection parent) {
		this.parent = parent;
		Socket s = parent.s;

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
		while (parent.shouldRun) {
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
