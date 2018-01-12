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
		if (isValid(packetArray)) {
			// strip newline symbols
			for (int i = 0; i < packetArray.length; i++) {
				String line = packetArray[i];
				line = line.replace("\r\n", "");
				packetArray[i] = line;
			} // strip newline 

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
				SCORES = new HashMap<String,Integer>();

				// loop through player/score pairs
				for (int i = 1; i < packetArray.length - 1; i += 2) {
					String pname = packetArray[i].replace("PNAME: ","");
					int pscore = Integer.parseInt(
								  		  packetArray[i+1].replace("PSCORE: ","")
											 );
					SCORES.put(pname, pscore);
				}
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
		if ( packet[packet.length - 1].equals("END\r\n") ) {
			validEnd = true;
		} else {
			validEnd = false;
		}

		if (validHeader && validEnd) {
			return true;
		} else {
			System.out.println("WARNING: Invalid packet parsed.");
			return false;
		}
	} // isValid
} // Packet
