package model;

public class CFGame extends Game {	
	/**
	 * Creates a new game of connect four with the two given players as players.
	 * 
	 * @param player1 First player of the game.
	 * @param player2 Second player of the game.
	 * @param width Width of the board.
	 * @param height Height of the board.
	 */
	public CFGame(Player player1, Player player2, int width, int height) {
		super(player1, player2);
		super.board = new CFBoard(this, width, height);
	}

	public void move(Player player, int x) {
		Move m = new Move(player, x, -1);
		this.move(m);
	}
}