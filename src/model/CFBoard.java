package model;

public class CFBoard extends Board {
	private boolean isWon;

	/**
	 * Creates a new connect four board with the given width and height. The board
	 * must have at least 8 fields (otherwise it cannot be won).
	 * 
	 * @param width  Width (in fields) of the board.
	 * @param height Height (in fields) of the board.
	 */
	CFBoard(Game game, int width, int height) {
		super(game, width, height);

		// If board can never be won or has negative size.
		if ((width * height < 8) || width < 1 || height < 1) {
			throw new IllegalArgumentException("A connect four board cannot have this size!");
		}
	}

	@Override
	boolean move(Move m) {
		// Check whether field can exist.
		if (m.getX() < 0 || m.getX() >= width) {
			throw new IllegalArgumentException("The field you try to choose does not exist!");
		}

		// Calculate y-Coordinate.
		if (board[m.getX()][0] != null)
			return false;
		int y = height - 1;
		while (board[m.getX()][y] != null) {
			y--;
		}

		// Move is valid, save it.
		super.board[m.getX()][y] = m.getPlayer();

		// Check whether game is won with this move.
		isWon = moveIsWinningMove(m.getX(), y, m.getPlayer());

		// Output that move is valid.
		return true;
	}

	private boolean moveIsWinningMove(int x, int y, Player p) {
		// Check horizontal.
		int count = 0;
		for (int i = x - 3; (i >= 0 && i <= x + 3 && i < width); i++) {
			if (board[i][y] != null && board[i][y].equals(p))
				count++;
			else
				count = 0;

			if (count == 4)
				return true;
		}

		// Check vertical.
		count = 0;
		for (int i = y; (i <= y + 3 && i < height); i++) {
			if (board[x][i] != null && board[x][i].equals(p))
				count++;
			else
				count = 0;

			if (count == 4)
				return true;
		}

		// Check diagonal (top-left to bottom-right).
		int i = 0;
		for (; x - 3 + i < 0 || y - 3 + i < 0;) i++;
		for (; x - 3 + i < width && y - 3 + i < height; i++) {
			if (board[x - 3 + i][y - 3 + i] != null && board[x - 3 + i][y - 3 + i].equals(p))
				count++;
			else
				count = 0;

			if (count == 4)
				return true;
		}

		// Check diagonal (top-right to bottom-left).
		i = 0;
		for (; x + 3 - i >= width || y - 3 + i < 0;) i++;
		for (; x + 3 - i >= 0 && y - 3 + i < height; i++) {
			if (board[x + 3 - i][y - 3 + i] != null && board[x + 3 - i][y - 3 + i].equals(p))
				count++;
			else
				count = 0;

			if (count == 4)
				return true;
		}
		
		return false;
	}

	@Override
	boolean isWon() {
		return isWon;
	}

}
