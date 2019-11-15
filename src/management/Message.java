package management;

import org.json.simple.JSONArray;

class Message {
	String content;
	User user;
	long creationTime;

	Message(String content, User user, long creationTime) {
		this.content = content;
		this.user = user;
		this.creationTime = creationTime;
	}

	String getContent() {
		return content;
	}

	User getUser() {
		return user;
	}

	long getCreationTime() {
		return creationTime;
	}

	@SuppressWarnings("unchecked")
	JSONArray getJSONObject() {
		JSONArray m = new JSONArray();
		m.add(user.getUsername());
		m.add(creationTime);
		m.add(content);

		return m;
	}
}
