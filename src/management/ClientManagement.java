package management;

import model.CFGame;
import model.ChGame;
import model.Game;
import org.json.simple.JSONArray;
import view.ClientUIController;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.toIntExact;

/**
 * Management of a Client. It contains the list of the currently online players,
 * the player's messages and his credentials.
 *
 * @author j-bl (Jan), Codesocks (Christian)
 */
public class ClientManagement extends Management {
	private final User thisUser = new User("_thisUser");
	private final User computerUser = new User("_computerUser");
	private ArrayList<Message> sendMessages = new ArrayList<Message>();
	private Game game = null;
	private String username;
	private String pwd;
	private ClientUIController uiController;

	public ClientManagement(ClientUIController uiController) {
		this.uiController = uiController;
	}

	// Only for LoginUI, ClientClI and debug-purposes.
	public ClientManagement() {

	}

	/**
	 * Sets all given players to online and all others to off-line.
	 * 
	 * @param jsonArray Contains new online players.
	 */
	public void setOnlinePlayers(JSONArray jsonArray) {
		// Set all players to off-line.
		for (Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			user.setOnline(false);
		}

		// Set new online players.
		for (Object o : jsonArray) {
			String username = (String) o;
			if (username == null || username.equals("") || username.equals(this.username))
				continue;

			if (!users.containsKey(username))
				users.put(username, new User(username, ""));
			users.get(username).setOnline(true);
		}

		// Updated Management.
		isUpdate();
	}

	/**
	 * Add the given message to the messages that shall be send. The message is send
	 * to the user with the given username. If that user is not currently online, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param content  Content of the message.
	 * @param username Username of the target.
	 * @throws IllegalArgumentException Thrown if the message is invalid or the
	 *                                  target not online.
	 */
	public void sendMessage(String content, String username) throws IllegalArgumentException {
		if (content == null || content.equals("") || content.length() > 140 || !users.containsKey(username)
				|| !(users.get(username).isOnline()))
			throw new IllegalArgumentException(
					"The message you attempted to send was not a valid message. It was either too short / long or the receiving party is not online.");

		sendMessages.add(new Message(content, users.get(username), System.currentTimeMillis()));
	}

	/**
	 * Returns a JSONArray containing all messages that were added to the list of
	 * messages to be send since the last update of this management's information.
	 * 
	 * @return JSONArray of messages to be send.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getLatestMessages() {
		JSONArray jsonArray = new JSONArray();

		for (Message m : sendMessages) {
			if (m.getCreationTime() >= this.getLatestUpdateTime()) {
				JSONArray jMessage = new JSONArray();
				jMessage.add(m.getContent());
				jMessage.add(m.getToUser().getUsername());
				jMessage.add(m.getCreationTime());
				jsonArray.add(jMessage);
			}
		}

		return jsonArray;
	}

	/**
	 * Add the given messages received by the client at the latest update to the
	 * management's information.
	 * 
	 * @param jsonArray Received messages.
	 */
	public void addReceivedMessages(JSONArray jsonArray) {
		if (jsonArray.size() == 0)
			return;

		// jsonArray is JSONArray of JSONArrays.
		for (Object o : jsonArray) {
			JSONArray j = (JSONArray) o;

			String content = (String) j.get(0);
			String username = (String) j.get(1);
			Long creationTime = (Long) j.get(2);
			User fromUser = users.get(username);

			// Check whether message is internal message and deal with invitations and so
			// on.
			if (content.length() >= 2 && content.substring(0, 2).equals("$$")) {
				if (content.substring(2, 4).equals("00")) { // game invitation received.
					addReceivedInvitation(fromUser, Integer.parseInt(content.split("-")[1]),
							Integer.parseInt(content.split("-")[2]),
							Character.getNumericValue(content.toCharArray()[4]));
					System.out.println("Received a game invitation from @" + fromUser.getUsername() + " for "
							+ (Character.getNumericValue(content.toCharArray()[4]) == Game.GAME_CHOMP ? "Chomp"
									: "Connect Four")
							+ " on a " + content.split("-")[1] + "x" + content.split("-")[2] + " board");

				} else if (content.substring(2, 4).equals("01")) { // game invitation accept.
					System.out.println("@" + fromUser.getUsername() + " accepted your game invitation for a game of "
							+ ((int) content.toCharArray()[4] == Game.GAME_CHOMP ? "Chomp" : "Connect Four") + " on a "
							+ content.split("-")[1] + "x" + content.split("-")[2] + " board");
					setGame(Character.getNumericValue(content.toCharArray()[4]),
							Integer.parseInt(content.split("-")[1]), Integer.parseInt(content.split("-")[2]),
							fromUser.getUsername(), false);
					uiController.openMainGame();

				} else if (content.substring(2, 4).equals("10")) { // game move.
					System.out.println("Your opponent attempted a move. It is now your turn.");
					if (game instanceof ChGame) {
						((ChGame) game).move(fromUser, Integer.parseInt(content.split("-")[1]),
								Integer.parseInt(content.split("-")[2]));
					} else {
						((CFGame) game).move(fromUser, Integer.parseInt(content.split("-")[1]));
					}
					uiController.updateGameView();

				} else if (content.substring(2, 4).equals("11")) {
					System.out.println("Your opponent terminated the game.");
					if (game != null) {
						uiController.opponentSurrendered();
					}
					// Left to implement.
				}
			} else {
				receivedMessages.add(new Message(content, fromUser, creationTime));
			}
		}

		isUpdate();
	}

	private void addReceivedInvitation(User fromUser, int width, int height, long game) {
		// Delete earlier invitations from the same player.
		GameInvitation delete = null;
		for (GameInvitation g : receivedInvitations) {
			if (g.getFromUser().getUsername().equals(fromUser.getUsername())) {
				delete = g;
			}
		}
		if (delete != null)
			receivedInvitations.remove(delete);

		receivedInvitations.add(new GameInvitation(game, fromUser, width, height, System.currentTimeMillis()));
	}

	/**
	 * Returns whether the player with the given name is currently online.
	 * 
	 * @param username Username of the player.
	 * @return Whether the given player is online.
	 */
	public boolean isOnline(String username) {
		if (!users.containsKey(username))
			return false;
		else
			return users.get(username).isOnline();
	}

	/**
	 * Updates the credentials of this client.
	 * 
	 * @param username Username.
	 * @param pwd      Password.
	 */
	public void setCredentials(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}

	/**
	 * Returns the credentials of this client as a JSONArray. Credentials consist of
	 * a username and a password.
	 * 
	 * @return Credentials.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getCredentials() {
		JSONArray credentials = new JSONArray();
		credentials.add(username);
		credentials.add(pwd);

		return credentials;
	}

	/**
	 * Returns all messages exchanged with the user with the given username.
	 * String[0] is the username of the user that send the message and String[1] is
	 * the content of that message.
	 * 
	 * @param username Username of the user the conversation was held with.
	 * @return Content of the chat.
	 */
	public String[][] getMessages(String username) {
		// Messages to User.
		ArrayList<Message> sendToUser = new ArrayList<Message>();
		for (Message m : sendMessages) {
			if (m.getToUser().getUsername().equals(username)) {
				sendToUser.add(m);
			}
		}

		// Messages from user.
		ArrayList<Message> receivedFromUser = new ArrayList<Message>();
		for (Message m : receivedMessages) {
			if (m.getToUser().getUsername().equals(username)) {
				receivedFromUser.add(m);
			}
		}

		// Total messages.
		String[][] messages = new String[receivedFromUser.size() + sendToUser.size()][2];
		int i = 0;
		while (!(sendToUser.isEmpty() && receivedFromUser.isEmpty())) {
			if (sendToUser.isEmpty()) {
				messages[i][0] = username;
				messages[i][1] = receivedFromUser.get(0).getContent();
				receivedFromUser.remove(0);

			} else if (receivedFromUser.isEmpty()) {
				messages[i][0] = this.username;
				messages[i][1] = sendToUser.get(0).getContent();
				sendToUser.remove(0);

			} else {
				// Check which message is earlier.
				if (sendToUser.get(0).getCreationTime() < receivedFromUser.get(0).getCreationTime()) {
					messages[i][0] = this.username;
					messages[i][1] = sendToUser.get(0).getContent();
					sendToUser.remove(0);
				} else {
					messages[i][0] = username;
					messages[i][1] = receivedFromUser.get(0).getContent();
					receivedFromUser.remove(0);
				}
			}

			i++;
		}

		return messages;
	}

	/**
	 * Marks a given invitation for a game as "dealt with".
	 * 
	 * @param invitation Invitation to close.
	 */
	public void closeInvitation(GameInvitation invitation) {
		receivedInvitations.remove(invitation);
	}

	/**
	 * Sets the UIController of this management to the given controller.
	 * 
	 * @param uiController UIController.
	 */
	public void setUiController(ClientUIController uiController) {
		this.uiController = uiController;
	}

	/**
	 * Sets the current game to a game with the given parameters.
	 * 
	 * @param gametype     Determines which game is played.
	 * @param width        Width of the board.
	 * @param height       Height of the board.
	 * @param opponentUser Username of player to play against.
	 * @param firstMove    If {@code true} this player is the one to move first.
	 */
	public void setGame(long gametype, long width, long height, String opponentUser, boolean firstMove) {
		if (gametype == Game.GAME_CHOMP) {
			game = new ChGame(thisUser, users.get(opponentUser), toIntExact(width), toIntExact(height), firstMove);
		} else {
			game = new CFGame(thisUser, users.get(opponentUser), toIntExact(width), toIntExact(height), firstMove);
		}
	}

	/**
	 * Sets the current game to a game with the given parameters against computer
	 *
	 * @param gametype  Determines which game is played.
	 * @param width     Width of the board.
	 * @param height    Height of the board.
	 * @param firstMove If {@code true} this player is the one to move first.
	 */
	public void setGame(long gametype, long width, long height, boolean firstMove) {
		computerUser.setComputer(true);
		if (gametype == Game.GAME_CHOMP) {
			game = new ChGame(thisUser, computerUser, toIntExact(width), toIntExact(height), firstMove);
		} else {
			game = new CFGame(thisUser, computerUser, toIntExact(width), toIntExact(height), firstMove);
		}
	}

	/**
	 * Returns the currently active game.
	 * 
	 * @return Game.
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Is to be called when the game is finished. Then the game of this management
	 * is closed as well.
	 */
	public void closeGame() {
		game = null;
	}

	@Override
	public ArrayList<String> getUsersOnline() {
		ArrayList<String> onlinePlayers = new ArrayList<String>();

		for (Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (user.isOnline() && !user.getUsername().contentEquals(username))
				onlinePlayers.add(user.getUsername());
		}

		return onlinePlayers;
	}

	/**
	 * Removes the latest game acceptation that was sent. If none was sent,
	 * nothing happens. This is to be called, when the invitation to accept is invalid.
	 * Otherwise client would constantly fail at sending it.
	 */
	public void removeLatestInvitation() {
		if(sendMessages != null && sendMessages.get(sendMessages.size() - 1).getContent().length() >= 2 && sendMessages.get(sendMessages.size() - 1).getContent().substring(0, 4).equals("$$01")) {
			sendMessages.remove(sendMessages.get(sendMessages.size() - 1));
		}
	}
}
