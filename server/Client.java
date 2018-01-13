// important imports
import java.io.*;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;

public class Client extends PacketListener implements Runnable {
	// not really direct parent but whatevs DUDE
	MinigameServer parent;

	// connection objects
	Socket clientSocket;
	ClientIn in;
	ClientOut out;
	String pName; // player info

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

		// add to mediator
		String[] hdrs = {"SESSIONCONNECT"};
		Mediator.getInstance().addListener(this, hdrs, "ALL");
	} //Client


	// construct Packets and send to mediator
	public void run() {
		boolean shouldRun = true;
		while (shouldRun) {
			// only works with .toArray().length, and NOT with .size()
			if (in.packetQueue.toArray().length != 0) { 
				// construct Package
				String[] packet = in.getNextPacket();
				Packet pck = new Packet(packet, this);
				// send to mediator
				Mediator.getInstance().sendPacket(pck);
			} // fi
		} // loop
	} // run()


	// receive packet from mediator
	public void recvPacket(Packet pck) {
		if (pck.HEADER.equals("SESSIONCONNECT") 
				&& pck.SOURCE == this) {
			// set new name
			this.pName = pck.PNAME;

			// make new session
			if (pck.SESSIONID.equals("NONE")) {
				GameSession newsession = new GameSession(parent, this);
			}
		} // SESSIONCONNECT
	} // recvPacket

	// TODO move this to other TODO
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

			// test connection

		} // thread loop
	} // run()


	// read data queue and split it into discrete packets if possible
	public void parseDataQueue() {
		// if end of message found
		if (dataQueue.toArray()[dataQueue.size() - 1].equals("END")) {
			// move the received packet to packetQueue
			packetQueue.add(dataQueue.toArray(new String[dataQueue.size()]));
			dataQueue.clear();

			System.out.println("PACKET RECEIVED");
		}	
	} // parseQueue


	// extract first packet from packetQueue
	public String[] getNextPacket() {
		String[] packet = (String[]) packetQueue.toArray()[0];
		packetQueue.remove(packet);
		return packet;
	} // getNextPacket
} // ClientIn

// Read TODO for changes here
// concurrent output writer for Client
class ClientOut implements Runnable {
	ArrayList<String[]> packetQueue;

	private OutputStream outStream;
	private PrintWriter writer;

	// constructor
	public ClientOut(Socket clientSocket) {
		// make printWriter object on clientSocket
		try {
			outStream = clientSocket.getOutputStream();
			writer = new PrintWriter(outStream); 
		} catch (Exception e) {
			System.out.println("ERROR: Could not open output printWriter.");
		}

		// init list
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
} // ClientOut
