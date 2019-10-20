package model;

public class ChGame extends Game {
	public ChGame(Player player1, Player player2, int width, int height) {
		super(player1, player2);
		super.board = new ChBoard(this, width, height);
	}
}
