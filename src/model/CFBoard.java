package model;

public class CFBoard extends Board {

	/**
	 * Creates a new connect four board with the given width and height. The board
	 * must have at least 8 fields (otherwise it cannot be won).
	 * 
	 * @param width Width (in fields) of the board.
	 * @param height Height (in fields) of the board.
	 */
	public CFBoard(int width, int height) {
		// If board can never be won or has negative size.
		if ((width * height < 8) || width < 1 || height < 1) {
			throw new IllegalArgumentException("A connect four board cannot have this size!");
		}
		super.board = new Player[width][height];
	}

	@Override
	public boolean move(Move m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWon() {
		// TODO Auto-generated method stub
		return false;
	}

}
