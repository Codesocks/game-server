package server;

import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import management.Management;

public class ServerThread extends Thread {
	Socket client;
	Management management;

	ServerThread(Socket client) {
		this.client = client;
	}

	public void run() {
		// Bearbeitung einer aufgebauten Verbindung
		try {
			String msg = Connection.read(client);
			
			// Handle msg of client.
			/*JSONObject jo = Connection.stringToJSONObject(msg);
			if(jo != null) {
				if(management.verifyUser(jo.get("username").toString(), jo.get("pwd").toString())) {
					System.out.println("Valid user.");
				}*/
			
			
			
			// Send reply.
			Connection.send(client, msg);

			
			// Fehler bei Ein- und Ausgabe
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (client != null)
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
