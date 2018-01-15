// a packet object for easier access to the different values
// only for received packets

import java.util.*;

public class Packet {
	private final Set<String> VALIDHEADERS = new HashSet<String>(
		Arrays.asList(
			"SESSIONCONNECT",
			"GAMECOMPLETE",
			"GAMESTART"
		));

	public final String HEADER;
	public final String PNAME;
	public final String SESSIONID;
	public final Client SOURCE;
	public final int GSCORE; // for GAMECOMPLETE packets

	// constructor
	public Packet(String[] packetArray, Client source) {
		// source client
		SOURCE = source;
		if (isValid(packetArray)) {
			// strip \r\n from packet
			for (int i = 0; i < packetArray.length; i++) {
				String line = packetArray[i];
				line = line.replace("\r\n", "");
				packetArray[i] = line;
			} 

			// parse packetArray
			// this works for all packet types
			HEADER = packetArray[0];
	 		PNAME = packetArray[1].replace("PNAME: ", "");
			SESSIONID = packetArray[2].replace("SESSIONID: ", "");

			// parse gamescore
			if (HEADER.equals("GAMECOMPLETE")) {
				String score = packetArray[3].replace("GSCORE: ", "");
				GSCORE = Integer.parseInt(score);

				System.out.println("READ GAMESCORE: " + score
													 + " FROM: " + PNAME);
			} else { 
				GSCORE = 0;
			}
		} else {
			HEADER = "INVALID";
			PNAME = "INVALID";
			SESSIONID = "INVALID";
			GSCORE = -1;
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

		boolean validPName;
		if (packet[1].substring(0, 7).equals("PNAME: ")) {
			validPName = true;
		} else {
			validPName = false;
			System.out.println("INVALID PNAME READ: " 
												 + packet[1].substring(0, 6)+ "X");
		}

		boolean validSessionID;
		if (packet[2].substring(0, 11).equals("SESSIONID: ")) {
			validSessionID = true;
		} else {
			validSessionID = false;
			System.out.println("INVALID SESSIONID READ: " + packet[2]);
		}

		if (validHeader && validPName && validSessionID) {
			return true;
		} else {
			return false;
		}
	} // isValid
} // Packet
