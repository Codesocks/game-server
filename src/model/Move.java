package model;

import org.json.simple.JSONObject;

class Move {
    private int x;
    private int y;
	private Player player;
    
	// Constructor.
    Move(Player player, int x, int y) {
        this.x = x;
        this.y = y;
        this.player = player;
    }
    
    Move(JSONObject o) {
    	
    }

    // Getter and Setter.
    JSONObject getJSONObject() {
    	return null;
    }
    
    int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	Player getPlayer() {
		return player;
	}
}
