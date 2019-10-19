package model;

import org.json.simple.JSONObject;

public class Move {
    private int x;
    private int y;
	private Player player;
    
	// Constructor.
    public Move(Player player, int x, int y) {
        this.x = x;
        this.x = y;
        this.player = player;
    }
    
    public Move(JSONObject o) {
    	
    }

    // Getter and Setter.
    public JSONObject getJSONObject() {
    	return null;
    }
    
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Player getPlayer() {
		return player;
	}
}
