/*
 * Singleton Mediator class
 */

import java.util.*;

public class Mediator {
	private static Mediator instance = new Mediator();

	private ArrayList<InternalPacketListener> listeners;

	// constructor
	private Mediator() {
	} // constructor


	// register a listener with headers to listen for
	public void addListener(Object o, String[] headers, String sID) {
		listeners.add(new InternalPacketListener(o, headers, sID));
	} // addlistener


	// return the mediator instance
	public static Mediator getInstance() {
		return instance;
	} // getInstace
} // Mediator


// Object to store in mediator arraylist
class InternalPacketListener {
	PacketListener obj;				// the listener object
	String[] headers; // types of packets to accept
	String sessionID; // session ID to accept packets for (can be ALL)

	// constr
	public InternalPacketListener(Object o, String[] headers, String sID) {
		this.obj = o;
		this.headers = headers;
		this.sessionID = sID;
	} // constructor


	// notify listener of packet
	public void sendPack(String[] packet) {
		obj.getPack(packet);
	} //sendPack
} // PacketListener
