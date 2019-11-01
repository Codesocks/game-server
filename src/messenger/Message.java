package messenger;

import model.Player;

public class Message {
	private Player fromPlayer;
	private String message;
	
	public Message(Player fromPlayer, String message) {
		this.fromPlayer = fromPlayer;
		this.message = message;
	}

	public Player getFromPlayer() {
		return fromPlayer;
	}

	public String getMessage() {
		return message;
	}
}
