package model;

import java.util.Random;

public class ChGame extends Game {
	/**
	 * Creates a new game of chomp with the two given players as players.
	 * 
	 * @param player1 First player of the game.
	 * @param player2 Second player of the game.
	 * @param width Width of the board.
	 * @param height Height of the board.
	 */
	public ChGame(Player player1, Player player2, int width, int height) {
		super(player1, player2);
		super.board = new ChBoard(this, width, height);
	}

	/**
	 * Generates new Move object with the given params
	 *
	 * @param player Player who makes the given move
	 * @param x X-Coordinate of the given move
	 * @param y Y-Coordinate of the given move
	 */
	public void move(Player player, int x, int y) {
		Move m = new Move(player, x, y);
		this.move(m);
	}

	/**
	 * the move made by the computer (random)
	 * at larger boards very inefficient
	 *
	 * @param player Computer player
	 */
	public void move(Player player) {
		int x;
		int y;
		if (super.board.getPlayerAt(0, 1) != null && super.board.getPlayerAt(1, 0) != null) {
			x = 0;
			y = 0;
		}

		else {
			Random rand = new Random();

			do {
				x = rand.nextInt(super.board.width);
				y = rand.nextInt(super.board.height);
			} while (super.board.getPlayerAt(x, y) != null && !(x == 0 && y == 0));
		}

		move(player, x, y);
	}
}
