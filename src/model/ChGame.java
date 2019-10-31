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
	
	public void move(Player player, int x, int y) {
		Move m = new Move(player, x, y);
		this.move(m);
	}

	// the move made by the computer
	public void move(Player player) {
		int x = 0;
		int y = 0;

		for (int i = 0; i < super.board.width; i++) {
			for (int j = 0; j < super.board.height; j++) {
				if (super.board.getPlayerAt(i,j) == null) {
					x = i;
					y = j;
				}
			}
		}

		move(player, x, y);
	}
}
