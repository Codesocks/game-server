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
	
	public void addReceivedMessages(JSONArray jsonArray) {
		if(jsonArray.size() == 0) return;
		
		// jsonArray is JSONArray of JSONArrays.
		for (Object o : jsonArray) {
			JSONArray j = (JSONArray) o;

			String content = (String) j.get(0);
			String username = (String) j.get(1);
			Long creationTime = (Long) j.get(2);

			User user = users.get(username);
			received.add(new Message(content, user, creationTime));
		}

		isUpdate();
	}
}
