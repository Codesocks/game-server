package model;

import org.json.simple.JSONObject;

public abstract class Game {
    protected Protocol protocol = new Protocol();
    protected Board board;
    protected Player[] player;
    
    protected Game(Player player1, Player player2) {
    	player = new Player[2];
    	player[0] = player1;
    	player[1] = player2;
    }
    
    public void move(Move m) {
    	boolean validMove = board.move(m);
		
		if(validMove) {
			protocol.push(m);
		}
    }
    
    public void move(JSONObject m) {
    	move(new Move(m));
    }
    
    public Player getPlayer1() {
    	return player[0];
    }
    
    public Player getPlayer2() {
    	return player[1];
    }
    
    @Override
    public String toString() {
		return board.toString();
    }
}
