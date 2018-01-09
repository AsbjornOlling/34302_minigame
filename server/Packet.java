// a packet object for easier access to the different values
// only for received packets

public class Packet {
	public final String HEADER;
	public final String PNAME;
	public final String SESSIONID;
	public final Client SOURCE;

	// for GAMECOMPLETE packets
	public final int GSCORE;

	// constructor
	public Packet(String[] packetArray, Client source) {
		// source client
		SOURCE = source;

		// parse packetArray
		HEADER = packetArray[0];
		PNAME = packetArray[1].replace("PNAME: ", "");
		SESSIONID = packetArray[2].replace("SESSIONID: ", "");

		if (HEADER.equals("GAMECOMPLETE")) {
			String score = packetArray[3].replace("GSCORE: ", "");
			GSCORE = Integer.parseInt(score);
		} else {
			GSCORE = 0;
		}
	} // constructor
} // Packet
