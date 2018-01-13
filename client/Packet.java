// a packet object for easier access to the different values
// only for received packets

import java.util.*;

public class Packet {
	private final Set<String> VALIDHEADERS = new HashSet<String>(
		Arrays.asList(
			"SESSIONJOINED",
			"SCOREUPDATE"
		));

	public final String HEADER;
	public final String SESSIONID;
	public final String GAMES;
	public HashMap<String,Integer> SCORES;

	// constructor
	public Packet(String[] packetArray) {
		// strip newline symbols
		for (int i = 0; i < packetArray.length; i++) {
			String line = packetArray[i];
			line = line.replace("\r\n", "");
			packetArray[i] = line;
		} // strip newline 

		if (isValid(packetArray)) {

			HEADER = packetArray[0];

			// parse SESSIONJOINED
			if (HEADER.equals("SESSIONJOINED")) {
				GAMES = packetArray[1].replace("GAMES: ","");
				SESSIONID = packetArray[2].replace("SESSIONID: ","");
			} else {
				// nullify not-used fields
				SESSIONID = null;
				GAMES = null;
			}

			// parse SCOREUPDATE
			if (HEADER.equals("SCOREUPDATE")) {
				parseScoreUpdate(packetArray);
			} else {
				// nullify unused vars
				SCORES = null;
			}
		} // isValid
		else { // if invalid packet
			HEADER = null;
			SESSIONID = null;
			GAMES = null;
			SCORES = null;
		}
	} // constructor


	// pair scores and player names
	// and sort them by score
	public void parseScoreUpdate(String[] packet) {
		SCORES = new HashMap<String,Integer>();

		// loop through player/score pairs
		for (int i = 1; i < packet.length - 1; i += 2) {
			String pname = packet[i].replace("PNAME: ","");
			int pscore = Integer.parseInt(
										packet[i+1].replace("PSCORE: ","")
									 );
			SCORES.put(pname, pscore);
		} // loop
	} // parseScoreUpdate

	// check packet validity
	public boolean isValid(String[] packet) {
		boolean validHeader;
		if (VALIDHEADERS.contains(packet[0])) {
			validHeader = true;
		} else {
			validHeader = false;
			System.out.println("INVALID HEADER READ: " + packet[0]);
		}

		boolean validEnd;
		if ( packet[packet.length - 1].equals("END") ) {
			validEnd = true;
		} else {
			validEnd = false;
			System.out.println("INVALID END READ: " + packet[packet.length-1]);
		}

		if (validHeader && validEnd) {
			return true;
		} else {
			System.out.println("WARNING: Invalid packet parsed.");
			return false;
		}
	} // isValid
} // Packet
