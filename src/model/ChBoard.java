package model;

public class ChBoard extends Board {
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
		if (m.getX() < 0 || m.getY() < 0 || m.getX() >= width || m.getY() >= height) {
			throw new IllegalArgumentException("The field you try to choose does not exist!");
		} else if (board[m.getX()][m.getY()] != null) {
			return false;
		}

		// Validate move.
		// (x,y) is lower right corner of the move.
		int x, y;
		for (x = m.getX(); x < width; x++)
			if (board[x][m.getY()] != null)
				break;
		for (y = m.getY(); y < height; y++)
			if (board[m.getX()][y] != null)
				break;
		System.out.println(x + " " + y);
		for (int i = m.getX(); i < x; i++)
			for (int j = m.getY(); j < y; j++)
				if(board[i][j] != null) return false;

		// Move is valid, save it.
		for (int i = m.getX(); i < x; i++)
			for (int j = m.getY(); j < y; j++)
				if (board[i][j] == null)
					board[i][j] = m.getPlayer();

		// Check whether game is won with this move.
		isWon = moveIsWinningMove(m.getX(), m.getY(), m.getPlayer());
		if (isWon && m.getPlayer().equals(game.getPlayer1()))
			winner = game.getPlayer2();
		if (isWon && m.getPlayer().equals(game.getPlayer2()))
			winner = game.getPlayer1();

		// Output that move is valid.
		return true;
	}

	private boolean moveIsWinningMove(int x, int y, Player p) {
		return x == 0 && y == 0;
	}

	@Override
	void undoLastMove(Move m) {
		winner = null;
		isWon = false;

		// Calculation of lower right border is NOT possible this way!
		// (x,y) is lower right corner of the move.
		int x, y;
		for (x = m.getX(); x < width; x++)
			if (board[x][m.getY()] != m.getPlayer())
				break;
		for (y = m.getY(); y < height; y++)
			if (board[m.getX()][y] != m.getPlayer())
				break;

		// Remove last move.
		for (int i = m.getX(); i < x; i++)
			for (int j = m.getY(); j < y; j++)
				board[i][j] = null;
	}
}
