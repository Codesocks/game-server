package management;

import java.util.Map;

import org.json.simple.JSONArray;

/**
 * Management of a Client. It contains the list of the currently online players,
 * the player's messages and his credentials.
 * 
 * @author j-bl (Jan), Codesocks (Christian)
 * 
 */
public class ClientManagement extends Management {
	private String username;
	private String pwd;

	/**
	 * Sets all given players to online and all others to off-line.
	 * 
	 * @param jsonArray
	 *            Contains new online players.
	 */
	public void setOnlinePlayers(JSONArray jsonArray) {
		// Set all players to off-line.
		for (Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			user.setOnline(false);
		}

		// Set new online players.
		for (Object o : jsonArray) {
			String username = (String) o;
			if (username == "" || username == null || username == this.username)
				break;

			if (!users.containsKey(username))
				users.put(username, new User(username, ""));
			users.get(username).setOnline(true);
		}

		// Updated Management.
		isUpdate();
	}
	
	/**
	 * Returns whether the player with the given name is currently online.
	 * 
	 * @param username Username of the player.
	 * @return Whether the given player is online.
	 */
	public boolean isOnline(String username) {
		if(!users.containsKey(username)) return false;
		else return users.get(username).isOnline();
	}

	/**
	 * Updates the credentials of this client.
	 * 
	 * @param username
	 *            Username.
	 * @param pwd
	 *            Password.
	 */
	public void setCredentials(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}

	/**
	 * Returns the credentials of this client as a JSONArray. Credentials
	 * consist of a username and a password.
	 * 
	 * @return Credentials.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getCredentials() {
		JSONArray credentials = new JSONArray();
		credentials.add(username);
		credentials.add(pwd);

		return credentials;
	}
}
