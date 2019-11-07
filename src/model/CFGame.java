package model;

import java.util.Random;

/**
 * Game of Chomp. It consists of two players, one of them can be a computer
 * player. The board of the game can be adjusted. The rules are implemented
 * according to https://de.wikipedia.org/wiki/Chomp.
 * 
 * @author j-bl (Jan), Codesocks (Christian)
 *
 */
public class CFGame extends Game {
	/**
	 * Creates a new game of connect four with the two given players as players.
	 * 
	 * @param player1 First player of the game.
	 * @param player2 Second player of the game.
	 * @param width   Width of the board.
	 * @param height  Height of the board.
	 */
	public CFGame(Player player1, Player player2, int width, int height) {
		super(player1, player2);
		super.board = new CFBoard(this, width, height);
	}

	/**
	 * Attempts the given move for the given player.
	 *
	 * @param player Player who makes the given move.
	 * @param x      X-Coordinate of the given move.
	 */
	public void move(Player player, int x) {
		Move m = new Move(player, x, -1);
		this.move(m);
	}

	@Override
	void moveComputerPlayer(Player player) {
		Random rand = new Random();
		int x;
		do {
			x = rand.nextInt(board.width);
		} while (board.getPlayerAt(x, 0) != null);

		move(player, x);
	}
}
