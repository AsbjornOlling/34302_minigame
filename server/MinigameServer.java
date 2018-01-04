public class MinigameServer {
	public static void main(String[] args) {
		
		ClientConnection connection = new ClientConnection(6666);

		while (connection.ServerActive == true) {
			// Do something while the connection is alive
		}
		
	}
}
