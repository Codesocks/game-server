package model;

public class ChGame extends Game {
	public ChGame(int width, int height) {
		super.board = new ChBoard(width, height);
	}
}
