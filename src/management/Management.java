package management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class Management {
	HashMap<String, User> users = new HashMap<String, User>();
	ArrayList<Message> receivedMessages = new ArrayList<Message>();
	ArrayList<GameInvitation> receivedInvitations = new ArrayList<GameInvitation>();

	private long latestUpdateTime = 0;

	/**
	 * Returns the last time this Management was updated. The time is given in
	 * milliseconds as a UNIX-time-stamp.
	 * 
	 * @return Time of latest update.
	 */
	public long getLatestUpdateTime() {
		return latestUpdateTime;
	}

	void isUpdate() {
		latestUpdateTime = System.currentTimeMillis();
	}

	/**
	 * Returns a List of all players currently online.
	 * 
	 * @return List of online players.
	 */
	public abstract ArrayList<String> getUsersOnline();
}
