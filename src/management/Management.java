package management;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;

abstract class Management {
	HashMap<String, User> users = new HashMap<String, User>();
	ArrayList<Message> received = new ArrayList<Message>();
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
}
