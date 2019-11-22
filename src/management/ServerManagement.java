package management;

import java.util.Map;
import org.json.simple.JSONArray;

/**
 * Management of a Server. It contains the list of the currently online player
 * and all player's messages.
 * 
 * @author j-bl (Jan), Codesocks (Christian)
 * 
 */
public class ServerManagement extends Management {
	/**
	 * Attempts to add the user with the given credentials to the management. If
	 * the credentials are non-valid credentials, {@code false} is returned.
	 * 
	 * @param credentials
	 *            Credentials of the future user.
	 * @return Whether adding the user was successful.
	 */
	boolean addUser(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);
		String pwd = (String) credentials.get(1);

		if (pwd == null || username == null || users.containsKey(username)) {
			return false;
		} else {
			users.put(username, new User(username, pwd));
			users.get(username).setOnline(true);
			return true;
		}
	}

	/**
	 * Attempts to sign-in to the given user's account. If the user does not
	 * exist, attempts to add the user. {@code false} is returned if credentials
	 * are not valid for the given user or if adding the user failed.
	 * 
	 * @param credentials
	 *            Credentials of the user.
	 * @return Whether sign-in was successful.
	 */
	public boolean signIn(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);

		// Add user if not already existing.
		if (users.containsKey(username)) {
			if (!verifyCredentials(credentials))
				return false;
		} else {
			boolean b = addUser(credentials);
			if (b == false)
				return false;
		}

		users.get(username).setOnline(true);
		isUpdate();
		return true;
	}

	/**
	 * Sign-out of given user's account.
	 * 
	 * @param credentials
	 *            Credentials of the user.
	 */
	public void signOut(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);

		users.get(username).setOnline(false);
		isUpdate();
	}

	/**
	 * Verifies the user's credentials. If credentials are not valid,
	 * {@code false} is returned.
	 * 
	 * @param credentials
	 *            Credentials of the user.
	 * @return Whether credentials are valid.
	 */
	public boolean verifyCredentials(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);
		String pwd = (String) credentials.get(1);

		// Check credentials and set user to online if necessary.
		if (users.containsKey(username) && users.get(username).verifyPWD(pwd)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns all currently online players (precisely: their usernames).
	 * 
	 * @return Array containing currently online players.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getOnlinePlayers() {
		JSONArray onlinePlayers = new JSONArray();

		for (Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (user.isOnline())
				onlinePlayers.add(user.getUsername());
		}

		return onlinePlayers;
	}

	/**
	 * Returns all new messages for the client that corresponds to the given
	 * credentials since the given UNIX-timestamp.
	 * 
	 * @param credentials
	 *            Credentials of the user.
	 * @param clientUpdateTime
	 *            Latest time the client's data was updated.
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public synchronized JSONArray getNewMessages(JSONArray credentials, long clientUpdateTime) {
		// Extract username from JSON-Data.
		String username = (String) credentials.get(0);
		User user = users.get("username");

		JSONArray newMessages = new JSONArray();
		for (Message m : received) {
			if (m.getCreationTime() >= clientUpdateTime
					&& m.getToUser().getUsername().equals(username)) {
				JSONArray jMessage = new JSONArray();
				jMessage.add(m.getContent());
				jMessage.add(m.getFromUser().getUsername());
				jMessage.add(m.getCreationTime());
				newMessages.add(jMessage);
			}
		}

		return newMessages;
	}

	/**
	 * Add the given messages received by the server at the latest connection to the
	 * management's information.
	 * 
	 * @param jsonArray
	 * @param credentials
	 */
	public synchronized void addReceivedMessages(JSONArray jsonArray, JSONArray credentials) {
		if (jsonArray.size() == 0)
			return;

		// Extract username from JSON-Data.
		String fromUsername = (String) credentials.get(0);
		User fromUser = users.get(fromUsername);

		// jsonArray is JSONArray of JSONArrays.
		for (Object o : jsonArray) {
			JSONArray j = (JSONArray) o;

			String content = (String) j.get(0);
			String username = (String) j.get(1);
			Long creationTime = (Long) j.get(2);

			User user = users.get(username);
			received.add(new Message(content, user, fromUser, creationTime));
		}

		isUpdate();
	}
}
