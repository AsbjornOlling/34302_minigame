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

		out = new ServerOut(connection);
		Thread outThread = new Thread(out);
		outThread.start();

		in = new ServerIn(connection);
		Thread inThread = new Thread(in);
		inThread.start();

	} // constructor


	// TODO FUCK ME OVER
	// init connection objects
	public void connect() {
		// socket
		try { // connect to server
			connection = new Socket(HOST, PORT);
		} catch (Exception ex) {
			System.out.println("ERROR: COULD NOT CONNECT TO SERVER");
		} 
		
		try { // input reader and output writer
			in = new BufferedReader(
					 new InputStreamReader(connection.getInputStream()));
		} catch (Exception ex) {
			// tell the user that he dun goofd
		}
	} // connect


	// just send one string immediately
	// mainly for testing
	public void sendString(String string) {
		out.print(string+"\r\n");
		out.flush();
	} // sendString


	// send gameComplete message
	public void gameComplete(int score) {
		// TODO construct packet here
		out.print("GAMECOMPLETE\r\n");
		out.print("PNAME: " + parent.game.pName + "\r\n");
		out.print("GSCORE: " + score + "\r\n");
		out.print("END" + "\r\n");
		out.flush();
	} // gameComplete
} // class


class ServerOut implements Runnable {
	private PrintWriter writer;	
	ArrayList<String[]> packetQueue;

	// cosntructor
	public ServerOut(Socket s) {
		// printwriter to write to server	
		writer = new PrintWriter(s.getOutputStream());
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
