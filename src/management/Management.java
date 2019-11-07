package management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.rules.Verifier;

public class Management {
	private HashMap<String, User> users = new HashMap<String, User>();
	private long latestUpdateTime = 0;

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

	public void signOut(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);

		users.get(username).setOnline(false);
		isUpdate();
	}

	public long getLatestUpdateTime() {
		return latestUpdateTime;
	}

	private void isUpdate() {
		latestUpdateTime = System.currentTimeMillis();
	}
}
