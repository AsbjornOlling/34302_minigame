/*
 * This is the main class of the 
 * MiniGames server part
 */

public class MinigameServer {
	// constant params
	public final int PORT = 6666;

	// main objects
	Listener newConnects;
	GameState state;

	// constructor
	public MinigameServer() {
		// make listener object
		newConnects = new Listener(this);
		// start thread
		Thread cThread = new Thread(newConnects);
		cThread.start();

		// make gamestate object
		state = new GameState(this);
	} // constructor

	// temp testing loop
	public void deleteMe() {
		while (true) {
			state.printAllPackets();
		}
	}
	
	// main
	public static void main(String[] args) {
		MinigameServer serverInstance = new MinigameServer();
		serverInstance.deleteMe();
	} // Main
} // Class
