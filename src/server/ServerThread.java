package server;

import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import management.Management;

class ServerThread extends Thread {
	Socket client;
	Server server;

	ServerThread(Socket client, Server server) {
		this.client = client;
		this.server = server;
	}

	public void run() {
		// Bearbeitung einer aufgebauten Verbindung
		try {
			String msg = Connection.read(client);

			// Handle msg of client.
			JSONObject jo = Connection.stringToJSONObject(msg);
			System.out.println("[SERVER] Incoming request:	" + msg);
			String reply = processData(jo);

			// Send reply.
			Connection.send(client, reply);

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

	@SuppressWarnings("unchecked")
	private String processData(JSONObject jo) {
		JSONObject output = new JSONObject();
		JSONArray credentials = (JSONArray) jo.get("credentials");
		int mode = Integer.valueOf((String) jo.get("mode"));

		// Verify credentials if already signed in.
		if (mode != 0) {
			boolean login = server.getManagement().verifyCredentials(credentials);
			output.put("SUCCESS", login);
			if (!login)
				return output.toString();
		}

		// Deal with modes.

		switch (mode) {
		case 0: // Sign-in.
			boolean b = server.getManagement().signIn(credentials);
			output.put("SUCCESS", b);
			return output.toString();

		case 1: // Sign-out.
			server.getManagement().signOut(credentials);

		case 2: // Update client's information.
			int clientUpdateTime = Integer.valueOf((String) jo.get("latestUpdateTime"));
			if (clientUpdateTime < server.getManagement().getLatestUpdateTime()) {
				output.put("onlinePlayers", server.getManagement().getOnlinePlayers());
				output.put("playerUpdateAvailable", true);
			}
			return output.toString();
			
		case 3: // Get currently online players.
			output.put("onlinePlayers", server.getManagement().getOnlinePlayers());
			return output.toString();
			
		default: // Not a valid mode.
			return "{\"SUCCESS\":false}";
		}
	}

}
