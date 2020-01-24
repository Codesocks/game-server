package management;

import org.json.simple.JSONArray;

public class GameInvitation extends CommunicationObject {
    private long game;
    private long width;
    private long height;

    static final long GAME_CHOMP = 0;
    static final long GAME_CONNECTFOUR = 1;

    // Client.
    GameInvitation(long game, User fromUser, int width, int height, long creationTime) {
        this.game = game;
        this.fromUser = fromUser;
        this.width = width;
        this.height = height;
        this.creationTime = creationTime;
    }

    // Server.
    GameInvitation(long game, User toUser, User fromUser, long creationTime) {
        this.game = game;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.creationTime = creationTime;
    }

    public long getGame() {
        return game;
    }

    @SuppressWarnings("unchecked")
    JSONArray getJSONObject() {
        JSONArray o = new JSONArray();
        o.add(toUser.getUsername());
        o.add(creationTime);
        o.add(game);

        return o;
    }
}
