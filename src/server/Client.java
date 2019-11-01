package server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3141;
	private Socket socket;

	/*
	 * public static void main(String[] args) { Socket server = null; try { server =
	 * new Socket("localhost", 3141); DataInputStream in = new
	 * DataInputStream(server.getInputStream()); DataOutputStream out = new
	 * DataOutputStream(server.getOutputStream()); out.writeInt(4); // sende 1.
	 * Operanden out.writeInt(10000); // sende 2. Operanden int result =
	 * in.readInt();// lese das Ergebnis System.out.println("Client: " + result); }
	 * catch (UnknownHostException e) {
	 * 
	 * } catch (IOException e) { // Verbindungsfehler e.printStackTrace();
	 * 
	 * } finally { // Fehler bei Ein-und Ausgabe if (server != null) try {
	 * server.close(); } catch (IOException e) { } } }
	 */

	public void connect() throws UnknownHostException, IOException {
		socket = new Socket(HOSTNAME, PORT);
	}

	public void disconnect() throws IOException {
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void send(String msg) throws IOException {
		Connection.send(socket, msg);
	}

	public String read() throws IOException {
		return Connection.read(socket);
	}
}
