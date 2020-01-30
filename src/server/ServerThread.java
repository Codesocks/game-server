package server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;

class ServerThread extends Connection implements Runnable {
	Server server;

	/**
	 * Creates a new ServerThread that responds to the given connection.
	 * 
	 * @param client Client of this ServerThread.
	 * @param server Server that the ServerThread reports to.
	 */
	ServerThread(Socket client, Server server) {
		socket = client;
		this.server = server;
	}

	/**
	 * Handles the given connection.
	 */
	public void run() {
		// Handles a given connection.
		try {
			String msg = read();

			// Handle msg of client.
			JSONObject jo = stringToJSONObject(msg);
			String reply = processData(jo);

			// Send reply.
			send(reply);

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
	 * Error-Codes: -1: Something unexpected went wrong 0: Success 1: Credentials
	 * failed 2: There is no such invitation to accept or one of the players is
	 * already in a game.
	 *
	 * @param jo Received JSONObject that shall be analyzed.
	 * @return Error code of parsing and handling the JSONObject.
	 */
	private String processData(JSONObject jo) {
		JSONObject output = new JSONObject();
		output.put("errorCode", 0);

		// Extract from JSONObject.
		JSONArray credentials = (JSONArray) jo.get("credentials");
		int mode = Integer.parseInt((String) jo.get("mode"));

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
			try {
				server.getManagement().addReceivedMessages(messages, credentials);
			} catch (IllegalArgumentException e) {
				output.put("errorCode", 2);
				e.printStackTrace();
			}

			// Compute reply and send it.
			output.put("messages", server.getManagement().getNewMessages(credentials, clientUpdateTime));
			// output.put("invitations",
			// server.getManagement().getNewInvitations(credentials, clientUpdateTime));
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
