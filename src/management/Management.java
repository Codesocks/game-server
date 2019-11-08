package management;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;

abstract class Management {
	HashMap<String, User> users = new HashMap<String, User>();
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
