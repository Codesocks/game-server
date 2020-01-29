package model;

class ChBoard extends Board {
	/**
	 * Creates a new chomp board with the given width and height. The board must
	 * have at least 2 fields (otherwise it cannot be played).
	 * 
	 * @param width  Width (in fields) of the board.
	 * @param height Height (in fields) of the board.
	 */
	ChBoard(Game game, int width, int height) {
		super(game, width, height);

		// If board can never be won or has negative size.
		if ((width * height < 2) || width < 1 || height < 1) {
			throw new IllegalArgumentException("A chomp board cannot have this size!");
		}
	}

	@Override
	boolean move(Move m) {
        // Check whether move is valid (field is empty and exists)
        if (m.getX() < 0 || m.getY() < 0 || m.getX() >= getWidth() || m.getY() >= getHeight()) {
            throw new IllegalArgumentException("The field you try to choose does not exist!");
        } else if (getBoard()[m.getX()][m.getY()] != null) {
            return false;
        }

        // Move is valid, save it.
        for (int i = m.getX(); i < super.getWidth(); i++) {
            for (int j = m.getY(); j < super.getHeight(); j++) {
                if (getBoard()[i][j] == null)
                    super.getBoard()[i][j] = m.getPlayer();
            }
        }

        // Check whether game is won with this move.
        setWon(moveIsWinningMove(m.getX(), m.getY()));
        if (isWon() && m.getPlayer().equals(getGame().getPlayer1()))
            setWinner(getGame().getPlayer2());
        if (isWon() && m.getPlayer().equals(getGame().getPlayer2()))
            setWinner(getGame().getPlayer1());

        // Output that move is valid.
        return true;
    }

    /**
     * Checks if the game is won with the given move
     *
     * @param x X coordinate of given move.
     * @param y Y coordinate of given move.
     * @return true if given move is winning move
     */
    private boolean moveIsWinningMove(int x, int y) {
        return x == 0 && y == 0;
    }
}
