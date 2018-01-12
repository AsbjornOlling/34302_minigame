/*
 * Singleton Mediator class
 */

import java.util.*;

public class Mediator {
	private ArrayList<InternalPacketListener> listeners;
	private static Mediator instance = new Mediator();

	private Mediator() {
		listeners = new ArrayList<InternalPacketListener>();
	} // constructor

	public static Mediator getInstance() {
		return instance;
	} // getInstace


	// register a listener with headers to listen for
	public void addListener(PacketListener l, String[] hdrs) {
		listeners.add(new InternalPacketListener(l, hdrs));
	} // addlistener


	// construct a new packet and send to all valid listners
	public void sendPacket(Packet pck) {
		// clone list to avoid concurrent modification
		ArrayList<InternalPacketListener> listenersClone;
		listenersClone = new ArrayList<>(listeners);

		for (InternalPacketListener listener : listenersClone) {
			boolean validHeader = false;

			// send if the listener accepts the packet type
			for (String header : listener.acceptHeaders) {
				if (pck.HEADER.equals(header)) {
					listener.sendPacket(pck);
				}
			} // header-check
		} // listeners loop
	} // sendPacket()
} // Mediator


// Object to store in mediator arraylist
class InternalPacketListener {
	PacketListener listener;	// the listener object
	String[] acceptHeaders;	 	// types of packets to accept

	// constr
	public InternalPacketListener(PacketListener l, String[] hdrs) {
		this.listener = l;
		this.acceptHeaders = hdrs;
	} // constructor


	// notify listener of packet
	public void sendPacket(Packet pck) {
		listener.recvPacket(pck);
	} //sendPack
} // PacketListener
