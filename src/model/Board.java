package model;

import management.User;

abstract class Board {
	User[][] board;
	Game game;
	int width;
	int height;
	boolean isWon;
	User winner;


	Board(Game game, int width, int height) {
		if (width < 1 || height < 1)
			throw new IllegalArgumentException("A board cannot have negative fields!");

		this.game = game;
		board = new User[width][height];
		this.width = width;
		this.height = height;
	}

	/**
	 * Move is checked, if m is valid the move is made
	 * check if game is won with this move
	 *
	 * @param m
	 * @return true if move is valid, else false
	 */
	abstract boolean move(Move m);

	User getWinner() {
		return winner;
	}
	
	boolean isWon() {
		return isWon;
	}

	/**
	 * Returns the player at the position (x, y) of the board. {@code null} is
	 * returned if no player has made a move at that position.
	 * 
	 * @param x X-Coordinate of the given move.
	 * @param y Y-Coordinate of the given move.
	 * @return Player who has made a move at the given position.
	 */
	User getPlayerAt(int x, int y) {
		return board[x][y];
	}

	/**
	 * Returns a String-representation of the board.
	 *
	 * @return String-representation of the board.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		// Ablaufen des Spielfeldes.
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[j][i] == null) {
					s.append("-");
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
