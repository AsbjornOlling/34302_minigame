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


	// construct a new packet and send to all valid listners
	public void sendPacket(String[] packet) {
		// construct packet
		Packet pck = new Packet(packet);
		
		// loop through all listeners
		for (InternalPacketListener listener : listeners) {
			// reset czech vars
			boolean validHeader = false;
			boolean validID = false;

			// check if header in listeners list of accepted headers
			for (String header : listener.acceptHeaders) {
				if (pck.HEADER.equals(header)) {
					validHeader = true;
				}
			} // header-czech

			// check if session ID allowed
			if (listener.acceptsID.equals("ALL") || 
					listener.acceptsID.equals(pck.SESSIONID)) {
				validID = true;
			} // sID-czech

			// send package if valid
			if (validHeader && validID) {
				listener.sendPacket(pck);
			}
		} // listeners loop
	} // sendPack
} // Mediator


// Object to store in mediator arraylist
class InternalPacketListener {
	PacketListener listener;	// the listener object
	String[] acceptHeaders;	 	// types of packets to accept
	String acceptsID; 	// session ID to accept packets for (can be ALL)

	// constr
	public InternalPacketListener(PacketListener listener, 
																String[] headers, 
																String sessionID) {
		this.listener = listener;
		this.acceptHeaders = headers;
		this.acceptsID = sessionID;
	} // constructor


	// notify listener of packet
	public void sendPacket(Packet pck) {
		listener.recvPacket(pck);
	} //sendPack
} // PacketListener
