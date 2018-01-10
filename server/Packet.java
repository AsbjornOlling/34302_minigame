// a packet object for easier access to the different values
// only for received packets

import java.util.*;

public class Packet {
	private final Set<String> VALIDHEADERS = new HashSet<String>(
		Arrays.asList(
			"SESSIONCONNECT",
			"GAMECOMPLETE"
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
			// parse packetArray
			HEADER = packetArray[0];
			PNAME = packetArray[1].replace("PNAME: ", ""
													 ).replace("\r\n","");
			SESSIONID = packetArray[2].replace("SESSIONID: ", ""
															 ).replace("\r\n","");

			// parse gamescore
			if (HEADER.equals("GAMECOMPLETE")) {
				String score = packetArray[3].replace("GSCORE: ", ""
		  															).replace("\r\n","");
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
			GSCORE = 0;
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
