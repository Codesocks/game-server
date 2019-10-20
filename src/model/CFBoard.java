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
	public boolean move(Move m) {
		// Check whether move is valid (field is empty and exists)
		if (m.getX() < 0 || m.getY() < 0 || m.getX() >= super.width || m.getY() >= super.height) {
			throw new IllegalArgumentException("The field you try to choose does not exist!");
		} else if (super.board[m.getX()][m.getY()] != null) {
			return false;
		}
		
		// Move is valid, save it.
		super.board[m.getX()][m.getY()] = m.getPlayer();
		return true;
	}

	@Override
	boolean isWon() {
		// TODO Auto-generated method stub
		return false;
	}

}
