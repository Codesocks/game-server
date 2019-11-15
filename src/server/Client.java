package server;

import java.io.IOException;
import java.net.Socket;

import management.ClientManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class Client extends Connection {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3141;
	private ClientManagement management;
	private Socket socket;

	Client(ClientManagement management) {
		this.management = management;
	}

	@SuppressWarnings("unchecked")
	private String request(JSONObject o) {
		String reply = "";

		// Add management's information.
		o.put("credentials", management.getCredentials());

		// Try sending JSONData.
		try {
			socket = new Socket(HOSTNAME, PORT);
			send(o.toString());
			reply = read();
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[CLIENT] Reply:			" + reply);
		return reply;
	}

	@SuppressWarnings("unchecked")
	int execute(String request) {
		JSONObject j = new JSONObject();

		if (request.equals("signin")) {
			j.put("mode", "0");
			System.out.println(this.request(j));
			execute("update");

		} else if (request.equals("signout")) {
			j.put("mode", "1");
			System.out.println(this.request(j));

		} else if (request.equals("update")) {
			j.put("mode", "2");
			j.put("latestUpdateTime", management.getLatestUpdateTime());
			j.put("messages", management.getLatestMessages());

			JSONObject reply = stringToJSONObject(this.request(j));
			if ((Boolean) reply.get("playerUpdateAvailable")) {
				management.setOnlinePlayers((JSONArray) reply.get("onlinePlayers"));
				System.out.println("The list of online players has been updated.");
			}
			if (((JSONArray) reply.get("messages")).size() > 0) {
				management.addReceivedMessages((JSONArray) reply.get("messages"));
				System.out.println("You have (" + ((JSONArray) reply.get("messages")).size() + ") new messages.");
			}
			System.out.println(reply);

		} else if (request.contains("sendmsg ")) {
			request = request.substring(8);
			String[] message = request.split(";");
			management.sendMessage(message[0], message[1]);
			System.out.println("The following message will be send to @" + message[1] + ": '" + message[0] + "'");
			return execute("update");

		} else if (request.contains("setusername ")) {
			request = request.substring(12);
			management.setCredentials(request, "pwddummy");
			System.out.println("Your username was set to '" + request + "'");
			return 0;

		} else {
			System.out.println("Invalid input :(");
			return -1;
		}

		return (Integer) j.get("errorCode");
	}
}
