package server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import management.ClientManagement;
import org.json.simple.JSONObject;

public class Client {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3141;
	private ClientManagement management;
	private Socket socket;
	
	
	Client(ClientManagement management) {
		this.management = management;
	}
	
	public String request(JSONObject o) {
		String reply = "";

		try {
			this.connect();
			this.send(o.toString());
			reply = this.read();
			this.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[CLIENT] Reply:			" + reply);
		return reply;
	}
	
	private void connect() throws UnknownHostException, IOException {
		socket = new Socket(HOSTNAME, PORT);
	}

	private void disconnect() throws IOException {
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void send(String msg) throws IOException {
		Connection.send(socket, msg);
	}

	private String read() throws IOException {
		return Connection.read(socket);
	}
}
