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
	public Object[][] SCOREBOARD;

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
				SCOREBOARD = parseScoreUpdate(packetArray);
			} else {
				// nullify unused vars
				SCOREBOARD = null;
			}
		} // isValid
		else { // if invalid packet
			HEADER = null;
			SESSIONID = null;
			GAMES = null;
		}
	} // constructor


	// pair scores and player names
	// and sort them by score
	public Object[][] parseScoreUpdate(String[] packet) {
		int noOfEntries = packet.length - 2;

		Object[][] scoreboard = new Object[2][noOfEntries];
		scoreboard[0] = new String[noOfEntries]; // pnames
		scoreboard[1] = new Integer[noOfEntries]; // scores

		// loop through player/score pairs
		for (int i = 1; i < packet.length - 1; i += 2) {
			// parse name and score
			String pname = packet[i].replace("PNAME: ","");
			Integer pscore = Integer.parseInt(
										packet[i+1].replace("PSCORE: ","")
									 );

			// put into scoreboard
			int entryNo = i - 1;
			scoreboard[0][entryNo] = pname;
			scoreboard[1][entryNo] = pscore;
		} // loop

		// sort the scoreboard
		Object[][] sortedboard = new Object[2][noOfEntries];
		for (int i = 0; i < noOfEntries; i++) {
			// find the highest value from the remaining entries
			int highestNo = -1;
			int highestIndex = -1;
			for (int j = 0; j < noOfEntries; j++) {
				if ((int) scoreboard[1][j] > highestNo) {
					highestNo = (int) scoreboard[1][j];
					highestIndex = j;
				}
			}
			// add the high value to the sorted board
			sortedboard[0][i] = scoreboard[0][highestIndex];
			sortedboard[1][i] = scoreboard[1][highestIndex];

			// clear that value from the old scoreboard
			scoreboard[0][highestIndex] = null;
			scoreboard[1][highestIndex] = null;
		} // sorting loop

		return sortedboard;
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
