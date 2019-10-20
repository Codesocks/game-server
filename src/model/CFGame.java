package model;

public class CFGame extends Game {	
	public CFGame(Player player1, Player player2, int width, int height) {
		super(player1, player2);
		super.board = new CFBoard(this, width, height);
	}
}
