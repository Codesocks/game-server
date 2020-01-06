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

    long getCreationTime() {
        return creationTime;
    }

    abstract JSONArray getJSONObject();
}
