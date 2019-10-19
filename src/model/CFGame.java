package model;

public class CFGame extends Game {	
	public CFGame(int width, int height) {
		super.board = new CFBoard(width, height);
	}
}
