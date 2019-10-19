package model;
public abstract class Board {
	private Player[][] board;
	
	public abstract boolean move(Move m);
	public abstract boolean isWon();
}
