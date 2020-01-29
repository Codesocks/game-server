package management;

import org.json.simple.JSONArray;

class Message {
    private String content;
    private User toUser;
    private User fromUser;
    private long creationTime;

    Message(String content, User toUser, long creationTime) {
        this.content = content;
        this.toUser = toUser;
        this.creationTime = creationTime;
    }

    Message(String content, User toUser, User fromUser, long creationTime) {
        this(content, toUser, creationTime);
        this.fromUser = fromUser;
    }
	
	String getContent() {
		return content;
	}

	User getToUser() {
		return toUser;
	}
	
	User getFromUser() {
		return fromUser;
	}

	long getCreationTime() {
		return creationTime;
	}

	@SuppressWarnings("unchecked")
	JSONArray getJSONObject() {
		JSONArray m = new JSONArray();
		m.add(toUser.getUsername());
		m.add(creationTime);
		m.add(content);

		return m;
	}
}
