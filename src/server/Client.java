package server;

import java.io.IOException;
import java.net.Socket;

import management.ClientManagement;

import model.Game;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Client of the game. Can connect to the server and execute commands. You need
 * to remember to sign-in before you can execute other functions.
 */
public class Client extends Connection {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 3141;
	private ClientManagement management;

	public ClientManagement getManagement() {
		return management;
	}

	/**
	 * Creates a new Client with the credentials of the given management. All
	 * replies are contained in the management.
	 * 
	 * @param management Management containing credentials.
	 */
	public Client(ClientManagement management) {
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
			System.out.println("Send:  " + o.toString());

			reply = read();
			System.out.println("Reply: " + reply);

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

		return reply;
	}

	/**
	 * Executes the given command and returns the error-code returned.
	 * @param request Command to execute.
	 * @return Error code.
	 */
	@SuppressWarnings("unchecked")
	public long execute(String request) {
		JSONObject j = new JSONObject();
		JSONObject reply;

		if (request.equals("signin")) {
			j.put("mode", "0");
			reply = stringToJSONObject(request(j));

		} else if (request.equals("signout")) {
			j.put("mode", "1");
			reply = stringToJSONObject(request(j));

		} else if (request.equals("update")) {
			j.put("mode", "2");
			j.put("latestUpdateTime", management.getLatestUpdateTime());
			j.put("messages", management.getLatestMessages());
			// j.put("invitations", management.getLatestInvitations());

			reply = stringToJSONObject(request(j));
			if ((Boolean) reply.get("playerUpdateAvailable")) {
				management.setOnlinePlayers((JSONArray) reply.get("onlinePlayers"));
				System.out.println("[CLIENT] [INFO] The list of online players has been updated.");
			}
			if (((JSONArray) reply.get("messages")).size() > 0) {
				management.addReceivedMessages((JSONArray) reply.get("messages"));
				System.out.println("[CLIENT] [INFO]	You have (" + ((JSONArray) reply.get("messages")).size()
						+ ") new messages.");

				for (Object o : ((JSONArray) reply.get("messages"))) {
					JSONArray jsonArray = (JSONArray) o;
					if(!((String) jsonArray.get(0)).substring(0,2).equals("$$")) System.out.println("@" + ((String) jsonArray.get(1)) + ": " + ((String) jsonArray.get(0)));
				}
			}
			/*if (((JSONArray) reply.get("invitations")).size() > 0) {
				management.addReceivedInvitations((JSONArray) reply.get("invitations"));
				System.out.println("[CLIENT] [INFO]	You have (" + ((JSONArray) reply.get("invitations")).size()
						+ ") new invitations.");

				for (Object o : ((JSONArray) reply.get("invitations"))) {
					JSONArray jsonArray = (JSONArray) o;
					System.out.println("@" + ((String) jsonArray.get(1)) + ": Game number " + ((Long) jsonArray.get(0)));
				}
			}*/

		} else if (request.contains("sendmsg ")) {
			request = request.substring(8);
			String[] message = request.split(";");
			management.sendMessage(message[0], message[1]);
			// Check whether internal control sequence present.
			if(request.toCharArray()[0] == '$' && request.toCharArray()[1] == '$') {
				// Stay silent, internal message.
			} else {
				System.out.println(
						"[CLIENT] [INFO] The following message will be send to @" + message[1] + ": '" + message[0] + "'");
			}
			return execute("update");

		/*} else if(request.contains("sendinvite ")) {
			request = request.substring(11);
			String[] invitation = request.split(";");
			management.invitePlayer(Long.valueOf(invitation[0]), invitation[1]);
			System.out.println(
					"[CLIENT] [INFO] An invitation for game number " + invitation[0] + " will be send to @" + invitation[1] + ".");
			return execute("update");*/

		} else if(request.contains("invite ")) {
			request = request.substring(7);
			String[] invitation = request.split(";");
			System.out.println(
					"[CLIENT] [INFO] An invitation for game number " + invitation[0] + " will be send to @" + invitation[1] + ".");
			return execute("sendmsg $$00" + request);

		} else if(request.contains("accept ")) {
			request = request.substring(7);
			String[] invitation = request.split(";");
			System.out.println(
					"[CLIENT] [INFO] Attempt to accept invitation for game number " + invitation[0] + " by @" + invitation[1] + ".");
			return execute("sendmsg $$01" + request);

		} else if (request.contains("setusername ")) {
			request = request.substring(12);
			management.setCredentials(request, "pwddummy");
			System.out.println("[CLIENT] [INFO]	Your username was set to '" + request + "'");
			return 0;

		} else {
			System.out.println("Invalid input :(");
			return -1;
		}

		return (Long) reply.get("errorCode");
	}
}
