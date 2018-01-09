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


	// return the mediator instance
	public static Mediator getInstance() {
		return instance;
	} // getInstace


	// register a listener with headers to listen for
	public void addListener(PacketListener l, String[] hdrs, String sID) {
		listeners.add(new InternalPacketListener(l, hdrs, sID));
	} // addlistener


} // Mediator


// Object to store in mediator arraylist
class InternalPacketListener {
	PacketListener listener;	// the listener object
	String[] headers; 	// types of packets to accept
	String sessionID; 	// session ID to accept packets for (can be ALL)

	// constr
	public InternalPacketListener(PacketListener listener, 
																String[] headers, 
																String sessionID) {
		this.listener = listener;
		this.headers = headers;
		this.sessionID = sessionID;
	} // constructor


	// notify listener of packet
	public void sendPack(String[] packet) {
		listener.recvPacket(packet);
	} //sendPack
} // PacketListener
