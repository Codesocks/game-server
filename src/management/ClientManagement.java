package management;

import java.util.ArrayList;
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
	private ArrayList<Message> send = new ArrayList<Message>();

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

	public void sendMessage(String content, String username)
			throws IllegalArgumentException {
		if (content == null || content.equals("") || content.length() > 140
				|| !users.containsKey(username) || !(users.get(username).isOnline()))
			throw new IllegalArgumentException(
					"The message you attempted to send was not a valid message. It was either too short / long or the receiving party is not online.");

		send.add(new Message(content, users.get(username), System
				.currentTimeMillis()));
	}

	@SuppressWarnings("unchecked")
	public JSONArray getLatestMessages() {
		JSONArray jsonArray = new JSONArray();
		
		for(Message m: send) {
			System.out.println(m.getCreationTime() + " ?>= " + this.getLatestUpdateTime());
			
			if(m.getCreationTime() >= this.getLatestUpdateTime()) {
				JSONArray jMessage = new JSONArray();
				jMessage.add(m.getContent());
				jMessage.add(m.getUser().getUsername());
				jMessage.add(m.getCreationTime());
				jsonArray.add(jMessage);
			}
		}
		
		return jsonArray;
	}
	
	/**
	 * Returns whether the player with the given name is currently online.
	 * 
	 * @param username
	 *            Username of the player.
	 * @return Whether the given player is online.
	 */
	public boolean isOnline(String username) {
		if (!users.containsKey(username))
			return false;
		else
			return users.get(username).isOnline();
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
