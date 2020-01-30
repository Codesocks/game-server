package management;

import org.json.simple.JSONArray;

/**
 * Invitation for a game of chomp or connect four.
 */
public class GameInvitation extends CommunicationObject {
	private long game;
	private long width;
	private long height;

	/**
	 * Creates a new game invitation for a ClientManagement.
	 * 
	 * @param game         Type of game.
	 * @param fromUser     User who sent the invitation.
	 * @param width        Width of the game.
	 * @param height       Height of the game.
	 * @param creationTime Time the invitation was sent.
	 */
	GameInvitation(long game, User fromUser, int width, int height, long creationTime) {
		this.game = game;
		this.fromUser = fromUser;
		this.width = width;
		this.height = height;
		this.creationTime = creationTime;
	}

	/**
	 * Creates a new game invitation for a ServerManagement.
	 * 
	 * @param game         Type of game.
	 * @param toUser       User to whom the invitation is sent.
	 * @param fromUser     User who sent the invitation.
	 * @param creationTime Time the invitation was sent.
	 */
	GameInvitation(long game, User toUser, User fromUser, long creationTime) {
		this.game = game;
		this.toUser = toUser;
		this.fromUser = fromUser;
		this.creationTime = creationTime;
	}

	/**
	 * Returns the type of the game this invitation is for.
	 * 
	 * @return Type of the game.
	 */
	public long getGame() {
		return game;
	}

	/**
	 * Returns the width of the game this invitation is for.
	 * 
	 * @return Width.
	 */
	public long getWidth() {
		return width;
	}

	/**
	 * Returns the height of the game this invitation is for.
	 * 
	 * @return Height.
	 */
	public long getHeight() {
		return height;
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
