package model;

import management.User;

class Move {
    private int x;
    private int y;
	private User player;

    Move(User player, int x, int y) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	User getPlayer() {
		return player;
	}
}
