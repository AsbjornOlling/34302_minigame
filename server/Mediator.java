/*
 * Singleton Mediator class
 */

import java.util.*;

public class Mediator {
	private static Mediator instance = new Mediator();

	private ArrayList<InternalPacketListener> listeners;

	// private constructor to avoid instantiation
	private Mediator() {
		// init arraylist
		listeners = new ArrayList<InternalPacketListener>();
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
	public void sendPacket(Packet pck) {
		// send to clients first, before sending to GameSession instances
		// to ensure that client has PNAMe attribute set when GameSession reads packet
		sendToClients(pck);
		sendToOthers(pck);
	} // sendPack


	// send to all clients
	public void sendToClients(Packet pck) {
		// make clone of listners list (avoid concurrent modification)
		ArrayList<InternalPacketListener> listenersClone;
		listenersClone = new ArrayList<>(listeners);

		for ( InternalPacketListener intList : listenersClone ) {
			if (intList.listener instanceof Client && intList.wantsPacket(pck) ) {
				intList.sendPacket(pck);
			}
		} // listners loop
	} // sendToClients


	// send to non-client listers (i.e. GameSessions)
	public void sendToOthers(Packet pck) {
		// make clone of listners list (avoid concurrent modification)
		ArrayList<InternalPacketListener> listenersClone;
		listenersClone = new ArrayList<>(listeners);

		for ( InternalPacketListener intList : listenersClone ) {
			if ( !(intList.listener instanceof Client) && intList.wantsPacket(pck) ) {
				intList.sendPacket(pck);
			}
		} // listners loop
	} // sendToClients
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


	// check recipient conditions
	public boolean wantsPacket(Packet pck) {
		// reset czech vars
		boolean validHeader = false;
		boolean validID = false;

		// check if header in listeners list of accepted headers
		for (String header : this.acceptHeaders) {
			if (pck.HEADER.equals(header)) {
				validHeader = true;
			}
		} // header-czech

		// check if session ID allowed
		if (this.acceptsID.equals("ALL") || 
				this.acceptsID.equals(pck.SESSIONID)) {
			validID = true;
		} // sID-czech

		// send package if valid
		if (validHeader && validID) {
			return true;
		} else {
			return false;
		}
	} // listenerWantsPacket
} // PacketListener
