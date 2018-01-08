// master class containing all of the client objects
// and all of the currently running sessions

import java.util.*;

public class GameState {
	MinigameServer parent;

	// list of all connected clients
	ArrayList<Object> clients;

	// constructor
	public GameState(MinigameServer parent) {
		this.parent = parent;

		// init arralists
		clients = new ArrayList<Object>();
	} // constructor

	// check all clients for new packages and print them
	// this should probably be done in GameSession instead
	// this is very shitty code and should be rplaced
	public void printAllPackets() {
		for (int i = 0; i < clients.size(); i++) {
			Client client = (Client) clients.toArray()[i];

			// if there are any packets, fetch and print it
			if (client.in.packetQueue.size() > 0) {
				String[] packet = client.in.getNextPacket();

				// print all lines of packet
				for (int j = 0; j < packet.length; j++) {
					System.out.println(packet[0]);
				}

			} //fi
		} // for
	} // printAll

} // GameState
