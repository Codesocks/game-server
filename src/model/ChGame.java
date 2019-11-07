package model;

import java.util.Random;

/**
 * Game of Connect Four. It consists of two players, one of them can be a
 * computer player. The board of the game can be adjusted. The rules are
 * implemented like you know them from your general knowledge.
 * 
 * @author j-bl (Jan), Codesocks (Christian)
 *
 */
public class ChGame extends Game {
	/**
	 * Creates a new game of chomp with the two given players as players.
	 * 
	 * @param player1 First player of the game.
	 * @param player2 Second player of the game.
	 * @param width   Width of the board.
	 * @param height  Height of the board.
	 */
	public ChGame(Player player1, Player player2, int width, int height) {
		super(player1, player2);
		board = new ChBoard(this, width, height);
	}

	/**
	 * Attempts the given move for the given player.
	 *
	 * @param player Player who makes the given move.
	 * @param x      X-Coordinate of the given move.
	 * @param y      Y-Coordinate of the given move.
	 */
	public void move(Player player, int x, int y) {
		Move m = new Move(player, x, y);
		this.move(m);
	}

	@Override
	void moveComputerPlayer(Player player) {
		int x;
		int y;
		if (board.getPlayerAt(0, 1) != null && board.getPlayerAt(1, 0) != null) {
			x = 0;
			y = 0;
		} else {
			Random rand = new Random();

			do {
				x = rand.nextInt(board.width);
				y = rand.nextInt(board.height);
			} while (board.getPlayerAt(x, y) != null && !(x == 0 && y == 0));
		}

		move(player, x, y);
	}
}
