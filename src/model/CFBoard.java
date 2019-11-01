package model;

public class CFBoard extends Board {
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
		board[m.getX()][y] = m.getPlayer();

		// Check whether game is won with this move.
		isWon = moveIsWinningMove(m.getX(), y, m.getPlayer());
		if(isWon) winner = m.getPlayer();

		// Output that move is valid.
		return true;
	}

	private boolean moveIsWinningMove(int x, int y, Player p) {
		int connected = 1;
		// vertical
		for (int j = y + 1; j < height && board[x][j].equals(p); j++) {
			connected++;
			if (connected == 4)
				return true;
		}

		connected = 1;
		// horizontal
		for (int i = x - 1; i >= 0 && board[i][y] != null && board[i][y].equals(p); i--) {
			connected++;
			if (connected == 4)
				return true;
		}
		for (int i = x + 1; i < width && board[i][y] != null && board[i][y].equals(p); i++) {
			connected++;
			if (connected == 4)
				return true;
		}

		connected = 1;
		// diagonal (top-left to bottom-right)
		for (int i = x - 1, j = y - 1; i >= 0 && j >= 0 && board[i][j] != null && board[i][j].equals(p) ; i--, j--) {
			connected++;
			if (connected == 4)
				return true;
		}
		for (int i = x + 1, j = y + 1; i < width && j < height && board[i][j] != null && board[i][j].equals(p); i++, j++) {
			connected++;
			if (connected == 4)
				return true;
		}

        connected = 1;
        // diagonal (top-right to bottom-left)
        for (int i = x + 1, j = y - 1; i < width && j >= 0 && board[i][j] != null && board[i][j].equals(p) ; i++, j--) {
            connected++;
            if (connected == 4)
                return true;
        }
        for (int i = x - 1, j = y + 1; i >= 0 && j < height && board[i][j] != null && board[i][j].equals(p); i--, j++) {
            connected++;
            if (connected == 4)
                return true;
        }

		return false;
	}
}
