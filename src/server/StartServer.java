package server;

public class StartServer {
	public static void main(String[] args) {
		// Starte den Server.
		Server s = new Server();
		Thread server = new Thread(s);
		s.start();
		System.out.println("Server started...");
	}
}
