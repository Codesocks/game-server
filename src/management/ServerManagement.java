package management;

import model.CFGame;
import model.ChGame;
import model.Game;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * Management of a Server. It contains the list of the currently online player
 * and all player's messages.
 *
 * @author j-bl (Jan), Codesocks (Christian)
 */
public class ServerManagement extends Management {
	private volatile ArrayList<Game> games = new ArrayList<Game>();
	private volatile ArrayList<String> logs = new ArrayList<String>();

	/**
	 * Attempts to add the user with the given credentials to the management. If the
	 * credentials are non-valid credentials, {@code false} is returned.
	 * 
	 * @param credentials Credentials of the future user.
	 * @return Whether adding the user was successful.
	 */
	boolean addUser(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);
		String pwd = (String) credentials.get(1);

		if (pwd == null || username == null || users.containsKey(username)) {
			return false;
		} else {
			users.put(username, new User(username, pwd));
			users.get(username).setOnline(true);
			return true;
		}
	}

	/**
	 * Attempts to sign-in to the given user's account. If the user does not exist,
	 * attempts to add the user. {@code false} is returned if credentials are not
	 * valid for the given user or if adding the user failed.
	 * 
	 * @param credentials Credentials of the user.
	 * @return Whether sign-in was successful.
	 */
	public boolean signIn(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);

		// Add user if not already existing.
		if (users.containsKey(username)) {
			if (!verifyCredentials(credentials))
				return false;
		} else {
			boolean b = addUser(credentials);
			if (!b)
				return false;
		}

		users.get(username).setOnline(true);
		isUpdate();
		return true;
	}

	/**
	 * Sign-out of given user's account.
	 * 
	 * @param credentials Credentials of the user.
	 */
	public void signOut(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);
		users.get(username).setOnline(false);

		// Delete all current invitations for this player and by this player.
		ArrayList<GameInvitation> deletedInvitations = new ArrayList<GameInvitation>();
		for (GameInvitation g : receivedInvitations) {
			if (g.getToUser().getUsername().equals(username) || g.getFromUser().getUsername().equals(username)) {
				deletedInvitations.add(g);
			}
		}
		for (GameInvitation g : deletedInvitations) {
			receivedInvitations.remove(g);
		}

		// Delete the game the player is currently in and send a message that he
		// surrendered..
		Game surrenderedGame = null;
		for (Game g : games) {
			if (g.getPlayer1().getUsername().equals(username) || g.getPlayer2().getUsername().equals(username)) {
				surrenderedGame = g;
			}
		}
		if (surrenderedGame != null) {
			if (surrenderedGame.getPlayer1().getUsername().equals(username)) {
				receivedMessages.add(new Message("$$11", surrenderedGame.getPlayer2(), users.get(username),
						System.currentTimeMillis()));
			} else {
				receivedMessages.add(new Message("$$11", surrenderedGame.getPlayer1(), users.get(username),
						System.currentTimeMillis()));
			}
			games.remove(surrenderedGame);
		}

		isUpdate();
	}

	/**
	 * Verifies the user's credentials. If credentials are not valid, {@code false}
	 * is returned.
	 * 
	 * @param credentials Credentials of the user.
	 * @return Whether credentials are valid.
	 */
	public boolean verifyCredentials(JSONArray credentials) {
		// Extract username and password from JSON-Data.
		String username = (String) credentials.get(0);
		String pwd = (String) credentials.get(1);

		// Check credentials and set user to online if necessary.
		return users.containsKey(username) && users.get(username).verifyPWD(pwd);
	}

	/**
	 * Returns all currently online players (precisely: their usernames).
	 * 
	 * @return Array containing currently online players.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getOnlinePlayers() {
		JSONArray onlinePlayers = new JSONArray();

		for (Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (user.isOnline())
				onlinePlayers.add(user.getUsername());
		}

		return onlinePlayers;
	}

	/**
	 * Returns all new messages for the client that corresponds to the given
	 * credentials since the given UNIX-timestamp.
	 * 
	 * @param credentials      Credentials of the user.
	 * @param clientUpdateTime Latest time the client's data was updated.
	 * @return JSONArray of new messages for the specified user.
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public synchronized JSONArray getNewMessages(JSONArray credentials, long clientUpdateTime) {
		// Extract username from JSON-Data.
		String username = (String) credentials.get(0);
		User user = users.get("username");

		JSONArray newMessages = new JSONArray();
		for (Message m : receivedMessages) {
			if (m.getCreationTime() >= clientUpdateTime && m.getToUser().getUsername().equals(username)) {
				JSONArray jMessage = new JSONArray();
				jMessage.add(m.getContent());
				jMessage.add(m.getFromUser().getUsername());
				jMessage.add(m.getCreationTime());
				newMessages.add(jMessage);
			}
		}

		return newMessages;
	}

	/**
	 * Add the given messages received by the server at the latest connection to the
	 * management's information.
	 * 
	 * @param jsonArray   JSONArray to parse.
	 * @param credentials Credentials of the user.
	 */
	public synchronized void addReceivedMessages(JSONArray jsonArray, JSONArray credentials) {
		if (jsonArray.size() == 0)
			return;

		// Extract username from JSON-Data.
		String fromUsername = (String) credentials.get(0);
		User fromUser = users.get(fromUsername);

		// jsonArray is JSONArray of JSONArrays.
		for (Object o : jsonArray) {
			JSONArray j = (JSONArray) o;

			String content = (String) j.get(0);
			String username = (String) j.get(1);
			Long creationTime = (Long) j.get(2);

			User toUser = users.get(username);

			// Check whether message is internal message and deal with invitations and so
			// on.
			if (content.length() >= 2 && content.substring(0, 2).equals("$$")) {
				switch (content.substring(2, 4)) {
				case "00": // game invitation.
					// Is player already in game?
					for (Game g : games) {
						if (g.getPlayer1().getUsername().equals(fromUser.getUsername())
								|| g.getPlayer2().getUsername().equals(fromUser.getUsername())) {
							throw new IllegalArgumentException(
									"The player trying to invite is already in a game. He cannot join a second game now!");
						}
						if (g.getPlayer1().getUsername().equals(toUser.getUsername())
								|| g.getPlayer2().getUsername().equals(toUser.getUsername())) {
							throw new IllegalArgumentException(
									"The player you are trying to invite is already in a game. He cannot join a second game now!");
						}
					}

					// Valid invite -> add it.
					addReceivedInvitation(fromUser, toUser, Integer.valueOf(content.toCharArray()[4]));
					log("Received a game invitation from @" + fromUser.getUsername() + " for @" + toUser.getUsername()
							+ " to play "
							+ (Integer.valueOf(content.toCharArray()[4]) == Game.GAME_CHOMP ? "Chomp"
									: "Connect Four"));

					break;
				case "01": // game invitation accept.
					// Does invite exist?
					GameInvitation invite = null;
					for (GameInvitation g : receivedInvitations) {
						if (g.getToUser().getUsername().equals(fromUser.getUsername())
								&& g.getFromUser().getUsername().equals(toUser.getUsername())
								&& (System.currentTimeMillis() - 60000) <= g.getCreationTime()) {
							invite = g;
						}
					}
					if (invite == null)
						throw new IllegalArgumentException(
								"There is no such invitation. Please remember that it might have already expired! Invites are only valid for 60 seconds after being send.");

					// Is player already in game?
					for (Game g : games) {
						if (g.getPlayer1().getUsername().equals(fromUser.getUsername())
								|| g.getPlayer2().getUsername().equals(fromUser.getUsername())) {
							throw new IllegalArgumentException(
									"The player trying to accept an invitation is already in a game. He cannot join a second game now!");
						}
					}

					// Deal with accepted invitation.
					receivedInvitations.remove(invite);
					if (Integer.valueOf(content.toCharArray()[4]) == Game.GAME_CHOMP) {
						games.add(new ChGame(fromUser, toUser, Integer.valueOf(content.split("-")[1]),
								Integer.valueOf(content.split("-")[2]), true));
					} else {
						games.add(new CFGame(fromUser, toUser, Integer.valueOf(content.split("-")[1]),
								Integer.valueOf(content.split("-")[2]), true));
					}
					log("@" + fromUser.getUsername() + " accepted the challenge by @" + toUser.getUsername()
							+ " to play a game of "
							+ (Integer.valueOf(content.toCharArray()[4]) == Game.GAME_CHOMP ? "Chomp"
									: "Connect Four"));

					break;
				case "10": // Game played.
					// Just forward the message - no check of validity at this point. Should be done
					// at client's site.

					break;
				case "11": // surrender.
					Game surrenderedGame = null;
					for (Game g : games) {
						if (g.getPlayer1().getUsername().equals(fromUser.getUsername())
								|| g.getPlayer2().getUsername().equals(fromUser.getUsername())) {
							surrenderedGame = g;
						}
					}
					if (surrenderedGame != null) {
						log("Game of @" + surrenderedGame.getPlayer1().getUsername() + " and @" + surrenderedGame.getPlayer2().getUsername()
								+ " was terminated.");
						games.remove(surrenderedGame);
					}
					break;
				}

			} else {
				log("Received a message from @" + fromUser.getUsername() + " to @" + toUser.getUsername() + ": \'" + content + "\'");
			}
			receivedMessages.add(new Message(content, toUser, fromUser, creationTime));
		}

		isUpdate();
	}

	/**
	 * Adds current game invitation to receivedInvitations. If the same invitation
	 * already exists, the existing one will be deleted
	 *
	 * @param fromUser User who sent the invitation.
	 * @param toUser   User who receives the invitation.
	 * @param game     Game that was invited to.
	 */
	private synchronized void addReceivedInvitation(User fromUser, User toUser, int game) {
		// Delete earlier invitations to the same player.
		GameInvitation delete = null;
		for (GameInvitation g : receivedInvitations) {
			if (g.getToUser().getUsername().equals(toUser.getUsername())
					&& g.getFromUser().getUsername().equals(fromUser.getUsername())) {
				delete = g;
			}
		}
		if (delete != null)
			receivedInvitations.remove(delete);

		receivedInvitations.add(new GameInvitation(game, toUser, fromUser, System.currentTimeMillis()));
	}

	@Override
	public ArrayList<String> getUsersOnline() {
		ArrayList<String> onlinePlayers = new ArrayList<String>();

		for (Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (user.isOnline())
				onlinePlayers.add(user.getUsername());
		}

		return onlinePlayers;
	}

	/**
	 * Returns a list of all currently running games displayed as Strings.
	 * 
	 * @return List of currently running games.
	 */
	public ArrayList<String> getCurrentGames() {
		ArrayList<String> currentGames = new ArrayList<String>();

		for (Game game : games) {
			currentGames.add("@" + game.getPlayer1().getUsername() + ", @" + game.getPlayer2().getUsername() + ": "
					+ (game instanceof ChGame ? "Chomp" : "Connect Four"));
		}

		return currentGames;
	}

	/**
	 * Adds the given information to the management's logs.
	 * 
	 * @param content Information to log.
	 */
	public void log(String stringToLog) {
		logs.add(stringToLog);
	}

	/**
	 * Returns all logs contained in this management.
	 * 
	 * @return Logs.
	 */
	public ArrayList<String> getLogs() {
		return logs;
	}
}
