package model;

import org.json.simple.JSONObject;

public abstract class Game {
	protected Protocol protocol = new Protocol();
	protected Board board;
	protected Player[] player = new Player[2];

	/**
	 * Creates a new game with the two given players as players.
	 * 
	 * @param player1 First player of the game.
	 * @param player2 Second player of the game.
	 */
	protected Game(Player player1, Player player2) {
		player[0] = player1;
		player[1] = player2;
	}

	public void move(Move m) {
		boolean validMove = board.move(m);

		if (validMove) {
			protocol.push(m);
		}
	}

	/**
	 * Conducts the given move on the board if the move is a valid move. Otherwise
	 * no move is made.
	 * 
	 * @param m JSONObject containing the instructions for the move.
	 */
	public void move(JSONObject m) {
		move(new Move(m));
	}

	/**
	 * Returns the player at the position (x, y) of the board. {@code null} is
	 * returned if no player has made a move at that position.
	 * 
	 * @param x X-Coordinate of the given move.
	 * @param y Y-Coordinate of the given move.
	 * @return Player who has made a move at the given position.
	 */
	public Player getPlayerAt(int x, int y) {
		return board.getPlayerAt(x, y);
	}

	/**
	 * Returns the first player of this game.
	 * 
	 * @return First player of the game.
	 */
	public Player getPlayer1() {
		return player[0];
	}

	/**
	 * Returns the second player of this game.
	 * 
	 * @return Second player of the game.
	 */
	public Player getPlayer2() {
		return player[1];
	}

	@Override
	public String toString() {
		return board.toString();
	}
}
