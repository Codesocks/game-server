package management;

import org.json.simple.JSONArray;

abstract class CommunicationObject {
	User toUser;
	User fromUser;
	long creationTime;

	User getToUser() {
		return toUser;
	}

	User getFromUser() {
		return fromUser;
	}

	/**
	 * Returns the name of the user who sent this object.
	 * 
	 * @return Username of the user.
	 */
	public String getFromUsername() {
		return fromUser.getUsername();
	}

	/**
	 * Returns the time this object was created.
	 * 
	 * @return Time.
	 */
	public long getCreationTime() {
		return creationTime;
	}

	abstract JSONArray getJSONObject();
}
