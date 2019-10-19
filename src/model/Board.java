package model;
public abstract class Board {
	protected Player[][] board;
	
	public abstract boolean move(Move m);
	public abstract boolean isWon();
}
