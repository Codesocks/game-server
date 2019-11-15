package server;

import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
			System.out.println("[RECEIVED] Incoming request:	    " + msg);
			String reply = processData(jo);

			// Send reply.
			Connection.send(client, reply);
			System.out.println("[SEND]     Reply to request:	    " + reply);

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
	/**
	 * Error-Codes:
	 * 	   -1:	Something unexpected went wrong
	 * 		0:	Success
	 * 		1:	Credentials failed
	 * 		2:	
	 * @param jo
	 * @return
	 */
	private String processData(JSONObject jo) {
		JSONObject output = new JSONObject();
		output.put("errorCode", 0);

		// Extract from JSONObject.
		JSONArray credentials = (JSONArray) jo.get("credentials");
		int mode = Integer.valueOf((String) jo.get("mode"));

		// Verify credentials if not attempting to sign-in.
		if (mode != 0) {
			boolean login = server.getManagement().verifyCredentials(
					credentials);
			if (!login) {
				output.put("errorCode", 1);
				return output.toString();
			}
		}

		// Deal with modes.

		switch (mode) {
		case 0: // Sign-in.
			boolean b = server.getManagement().signIn(credentials);
			if (!b)
				output.put("errorCode", 1);
			break;

		case 1: // Sign-out.
			server.getManagement().signOut(credentials);
			break;

		case 2: // Update client's information.
			long clientUpdateTime = (Long) jo
					.get("latestUpdateTime");
			JSONArray messages = (JSONArray) jo.get("messages");
			server.getManagement().addReceivedMessages(messages);
			
			output.put("messages", server.getManagement().getNewMessages(credentials, clientUpdateTime));
			output.put("playerUpdateAvailable", false);
			if (clientUpdateTime < server.getManagement().getLatestUpdateTime()) {
				output.put("onlinePlayers", server.getManagement()
						.getOnlinePlayers());
				output.put("playerUpdateAvailable", true);
			}
			break;

		case 3: // Get currently online players.
			output.put("onlinePlayers", server.getManagement()
					.getOnlinePlayers());
			break;

		default: // Not a valid mode.
			output.put("SUCCESS", -1);
			break;
		}
		
		return output.toString();
	}

}
