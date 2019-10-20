package model;

abstract class Board {
	protected Player[][] board;
	protected Game game;
	protected int width;
	protected int height;

	protected Board(Game game, int width, int height) {
		if (width < 1 || height < 1)
			throw new IllegalArgumentException("A board cannot have negative fields!");

		this.game = game;
		board = new Player[width][height];
		this.width = width;
		this.height = height;
	}

	abstract boolean move(Move m);

	abstract boolean isWon();

	/**
	 * Returns the player at the position (x, y) of the board. {@code null} is
	 * returned if no player has made a move at that position.
	 * 
	 * @param x X-Coordinate of the given move.
	 * @param y Y-Coordinate of the given move.
	 * @return Player who has made a move at the given position.
	 */
	protected Player getPlayerAt(int x, int y) {
		return board[x][y];
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		// Ablaufen des Spielfeldes
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[j][i] == null) {
					s.append("\u2610");
				} else if (board[j][i].equals(game.getPlayer1())) {
					s.append("0");
				} else {
					s.append("X");
				}
			}

			s.append("\n");
		}
		return s.toString();
	}
}
