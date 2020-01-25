package model;

import management.User;
import org.json.simple.JSONObject;

public abstract class Game {
	Protocol protocol = new Protocol();
	Board board;
	User[] player = new User[2];
	User currentPlayer;

	public static final long GAME_CHOMP = 0;
	public static final long GAME_CONNECTFOUR = 1;

	/**
	 * Creates a new game with the two given players as players.
	 * 
	 * @param player1   First player of the game.
	 * @param player2   Second player of the game.
	 * @param firstMove If {@code true} the first player is the first one to move.
	 */
	Game(User player1, User player2, boolean firstMove) {
		player[0] = player1;
		player[1] = player2;
		if (firstMove)
			currentPlayer = player1;
		else
			currentPlayer = player2;
	}

	boolean move(Move m) {
		if (!board.isWon()) {
			boolean validMove = board.move(m);

			// Protocol last move.
			System.out.println("Winning: " + board.isWon());
			if (validMove) {
				protocol.push(m);

				// Update who's turn it is.
				if (m.getPlayer().equals(getPlayer1()))
					currentPlayer = getPlayer2();
				else
					currentPlayer = getPlayer1();

				// Move computer player.
				if (currentPlayer.isComputer())
					moveComputerPlayer(currentPlayer);

				return true;
			}
		}
		
		return false;
	}

	/**
	 * Makes a random move for the given player. This player should be a computer
	 * player. Those moves are very inefficient on larger boards.
	 *
	 * @param player Computer player.
	 */
	abstract void moveComputerPlayer(User player);

	/**
	 * Returns whether the game is won.
	 * 
	 * @return Whether game is won.
	 */
	public boolean isWon() {
		return board.isWon();
	}

	/**
	 * Conducts the given move on the board if the move is a valid move. Otherwise
	 * no move is made.
	 * 
	 * @param m JSONObject containing the instructions for the move.
	 */
	void move(JSONObject m) {
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
	public User getPlayerAt(int x, int y) {
		return board.getPlayerAt(x, y);
	}

	/**
	 * Returns the first player of this game.
	 * 
	 * @return First player of the game.
	 */
	public User getPlayer1() {
		return player[0];
	}

	/**
	 * Returns the second player of this game.
	 * 
	 * @return Second player of the game.
	 */
	public User getPlayer2() {
		return player[1];
	}

	/**
	 * Returns the winner of the game. If {@code null} no one has won the game yet.
	 * 
	 * @return Winner of the game.
	 */
	public User getWinner() {
		return board.getWinner();
	}

	/**
	 * Returns player who is supposed to move the next Move.
	 * 
	 * @return Player who shall move the next Move.
	 */
	public User getCurrentPlayer() {
		return currentPlayer;
	}

	public int getWidth() {
		return board.getWidth();
	}

	public int getHeight() {
		return board.getHeight();
	}

	@Override
	public String toString() {
		return board.toString();
	}
}
