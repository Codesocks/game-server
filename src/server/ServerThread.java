package server;

import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class ServerThread extends Connection implements Runnable {
	Server server;

	ServerThread(Socket client, Server server) {
		socket = client;
		this.server = server;
	}

	public void run() {
		// Bearbeitung einer aufgebauten Verbindung
		try {
			String msg = read();

			// Handle msg of client.
			JSONObject jo = stringToJSONObject(msg);
			// server.addLog("[RECEIVED] Incoming request: " + msg);
			System.out.println("[RECEIVED] Incoming request:	    " + msg);
			String reply = processData(jo);

			// Send reply.
			send(reply);
			// server.addLog("[SEND] Reply to request: " + reply);
			System.out.println("[SEND]     Reply to request:	    " + reply);

			// Fehler bei Ein- und Ausgabe
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Error-Codes:
	 * 	-1: Something unexpected went wrong
	 * 	0: Success
	 * 	1: Credentials failed 2:
	 * 
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
			boolean login = server.getManagement().verifyCredentials(credentials);
			if (!login) {
				server.addLog("Failed credentials by user with credentials: " + credentials.toString());
				output.put("errorCode", 1);
				return output.toString();
			}
		}

		// Deal with modes.

		switch (mode) {
		case 0: // Sign-in.
			boolean b = server.getManagement().signIn(credentials);
			if (!b) {
				output.put("errorCode", 1);
				server.addLog("Failed login by user with credentials: " + credentials.toString());
			} else {
				server.addLog("Successful login by @" + credentials.get(0));
			}
			break;

		case 1: // Sign-out.
			server.getManagement().signOut(credentials);
			server.addLog("User @" + credentials.get(0) + " signed out");
			break;

		case 2: // Update client's information.
			// Extract given information and add included messages, invitations.
			long clientUpdateTime = (Long) jo.get("latestUpdateTime");
			JSONArray messages = (JSONArray) jo.get("messages");
			// JSONArray invitations = (JSONArray) jo.get("invitations");
			server.getManagement().addReceivedMessages(messages, credentials);
			// server.getManagement().addReceivedInvitations(invitations, credentials);

			// Log received Messages and Invitations.
			if (!messages.toString().equals("[]")) {
				server.addLog("Processing new messages send by @" + credentials.get(0) + ":");
				for (Object message : messages)
					server.addLog("...to @" + (String) ((JSONArray) message).get(1) + ": "
							+ (String) ((JSONArray) message).get(0));
			}
			/*if (!invitations.toString().equals("[]")) {
				server.addLog("Processing new invitations send by @" + credentials.get(0) + ":");
				for (Object invitation: invitations)
					server.addLog("...to @" + (String) ((JSONArray) invitation).get(1));
			}*/

			// Compute reply and send it.
			output.put("messages", server.getManagement().getNewMessages(credentials, clientUpdateTime));
			// output.put("invitations", server.getManagement().getNewInvitations(credentials, clientUpdateTime));
			output.put("playerUpdateAvailable", false);
			if (clientUpdateTime < server.getManagement().getLatestUpdateTime()) {
				output.put("onlinePlayers", server.getManagement().getOnlinePlayers());
				output.put("playerUpdateAvailable", true);
				server.addLog("Transmit updated online players to @" + credentials.get(0));
			}
			break;

		case 3: // Get currently online players.
			output.put("onlinePlayers", server.getManagement().getOnlinePlayers());
			break;

		default: // Not a valid mode.
			output.put("errorCode", -1);
			break;
		}

		return output.toString();
	}

}
