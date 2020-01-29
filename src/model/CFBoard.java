package model;

import management.User;

class CFBoard extends Board {
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
        if (m.getX() < 0 || m.getX() >= getWidth()) {
            throw new IllegalArgumentException("The field you try to choose does not exist!");
        }

        // Calculate y-Coordinate.
        if (getBoard()[m.getX()][0] != null)
            return false;
        int y = getHeight() - 1;
        while (getBoard()[m.getX()][y] != null) {
            y--;
        }

        // Move is valid, save it.
        getBoard()[m.getX()][y] = m.getPlayer();

        // Check whether game is won with this move.
        setWon(moveIsWinningMove(m.getX(), y, m.getPlayer()));
        if (isWon()) setWinner(m.getPlayer());

        // Output that move is valid.
        return true;
    }

	private boolean moveIsWinningMove(int x, int y, User p) {
        int connected = 1;
        // vertical
        for (int j = y + 1; j < getHeight() && getBoard()[x][j].equals(p); j++) {
            connected++;
            if (connected == 4)
                return true;
        }

        connected = 1;
        // horizontal
        for (int i = x - 1; i >= 0 && getBoard()[i][y] != null && getBoard()[i][y].equals(p); i--) {
            connected++;
            if (connected == 4)
                return true;
        }
        for (int i = x + 1; i < getWidth() && getBoard()[i][y] != null && getBoard()[i][y].equals(p); i++) {
            connected++;
            if (connected == 4)
                return true;
        }

        connected = 1;
        // diagonal (top-left to bottom-right)
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0 && getBoard()[i][j] != null && getBoard()[i][j].equals(p); i--, j--) {
            connected++;
            if (connected == 4)
                return true;
        }
        for (int i = x + 1, j = y + 1; i < getWidth() && j < getHeight() && getBoard()[i][j] != null && getBoard()[i][j].equals(p); i++, j++) {
            connected++;
            if (connected == 4)
                return true;
        }

        connected = 1;
        // diagonal (top-right to bottom-left)
        for (int i = x + 1, j = y - 1; i < getWidth() && j >= 0 && getBoard()[i][j] != null && getBoard()[i][j].equals(p); i++, j--) {
            connected++;
            if (connected == 4)
                return true;
        }
        for (int i = x - 1, j = y + 1; i >= 0 && j < getHeight() && getBoard()[i][j] != null && getBoard()[i][j].equals(p); i--, j++) {
            connected++;
            if (connected == 4)
                return true;
        }

		return false;
	}
}
