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
		if (m.getX() < 1 || m.getY() < 1 || m.getX() >= super.width || m.getY() >= super.height) {
			throw new IllegalArgumentException("The field you try to choose does not exist!");
		} else if (super.board[m.getX()][m.getY()] != null) {
			return false;
		}

		// Move is valid, save it.
		for (int i = m.getX(); i < super.width; i++) {
			for (int j = m.getY(); j < super.height; j++) {
				super.board[i][j] = m.getPlayer();
			}
		}
		return false;
	}

	@Override
	boolean isWon() {
		// TODO Auto-generated method stub
		return false;
	}

}
