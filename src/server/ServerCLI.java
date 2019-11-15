package server;

public class ServerCLI {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// Starts a new server.
		Server s = new Server();
		Thread server = new Thread(s);
		s.start();
		
		System.out.println("Console (Server Command Line Interface):");
		System.out.println("Server started...");
	}
}
