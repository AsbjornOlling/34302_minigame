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
		int noOfEntries = (packet.length - 2)/2;
		System.out.println("ENTRIES:"+noOfEntries);

		// make blank board if no scores got
		if (noOfEntries < 1) {
			Object[][] blankBoard = new Object[2][1];
			blankBoard[0][0] = new Integer(-1);
			blankBoard[0][1] = new Integer(-2);
		}

		// make scoreboard
		Object[][] scoreboard = new Object[2][noOfEntries];
		scoreboard[0] = new String[noOfEntries]; // pnames
		scoreboard[1] = new Integer[noOfEntries]; // scores

		// loop through player/score line pairs in packet
		int entryNo = 0;
		for (int i = 0; i < noOfEntries; i++) {
			// b/c there are two lines per entry, one header on top
			int lineNo = (i*2) + 1;

			// parse the two lines
			String pname = packet[lineNo].replace("PNAME: ","");
			Integer pscore = Integer.parseInt(packet[lineNo + 1]
												.replace("PSCORE: ",""));

			// put them into scoreboard
			scoreboard[0][i] = pname;
			scoreboard[1][i] = pscore;

			// count and loop
			entryNo++;
		} // loop

		// sort the scoreboard ("selection sort" i think?)
		Object[][] sortedboard = new Object[2][noOfEntries];
		for (int i = 0; i < noOfEntries; i++) {
			// find the highest value from the remaining entries
			int highestNo = -1;
			int highestIndex = -1;
			for (int j = 0; j < noOfEntries; j++) {
				System.out.println("J"+j);
				if ((int) scoreboard[1][j] > highestNo) {
					System.out.println("SORT "+scoreboard[1][j]+" "+highestNo);
					highestNo = (int) scoreboard[1][j];
					highestIndex = j;
				} else {
					System.out.println("NOT HIGHEST: "+ (int) scoreboard[1][j]);
				}

			} // high found

			// add the high value to the sorted board
			System.out.println("SORTING");
			System.out.println("NAME:"+scoreboard[0][highestIndex]);
			sortedboard[0][i] = scoreboard[0][highestIndex]; // name
			System.out.println("SCORE:"+scoreboard[1][highestIndex]);
			sortedboard[1][i] = scoreboard[1][highestIndex]; // score

			// clear that value from the old scoreboard
			scoreboard[0][highestIndex] = "";
			scoreboard[1][highestIndex] = -1;
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
