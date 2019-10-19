package model;

import org.json.simple.JSONObject;

public abstract class Game {
    protected Protocol protocol;
    protected Board board;
    
    public void move(Move m) {
    	boolean validMove = board.move(m);
		
		if(validMove) {
			protocol.push(m);
		}
    }
    
    public void move(JSONObject m) {
    	move(new Move(m));
    }
}
