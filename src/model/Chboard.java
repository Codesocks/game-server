package model;

public class ChBoard extends Board {

	/**
	 * Creates a new chomp board with the given width and height. The board
	 * must have at least 2 fields (otherwise it cannot be played).
	 * 
	 * @param width Width (in fields) of the board.
	 * @param height Height (in fields) of the board.
	 */
	public ChBoard(int width, int height) {
		// If board can never be won or has negative size.
		if ((width * height < 2) || width < 1 || height < 1) {
			throw new IllegalArgumentException("A chomp board cannot have this size!");
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
