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


	// pass packets to mediator
	public void run() {
		boolean shouldRun = true;
		while (shouldRun) {
			// only works with .toArray().length, and NOT with .size()
			if (in.packetQueue.toArray().length != 0) { 
				String[] packet = in.getNextPacket();
				Mediator.getInstance().sendPacket(packet);
			} // fi
		} // loop
	} // run()


	// receive packet from mediator
	public void recvPacket(Packet pck) {
		if (pck.HEADER.equals("SESSIONCONNECT") 
				&& pck.SESSIONID.equals("NONE")) {
			// make new session
			GameSession newsession = new GameSession(parent);
		}
	} // recvPacket


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
