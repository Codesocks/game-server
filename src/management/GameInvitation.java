package management;

import org.json.simple.JSONArray;

class GameInvitation extends CommunicationObject {
    private long game;

    static final long GAME_CHOMP = 0;
    static final long GAME_CONNECTFOUR = 1;

    GameInvitation(long game, User toUser, long creationTime) {
        this.game = game;
        this.toUser = toUser;
        this.creationTime = creationTime;
    }

    GameInvitation(long game, User toUser, User fromUser, long creationTime) {
        this.game = game;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.creationTime = creationTime;
    }

    long getGame() {
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
