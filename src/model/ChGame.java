package model;

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
}
