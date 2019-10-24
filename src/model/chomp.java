package model;

import java.io.IOException;
import java.util.Scanner;

public class chomp {

	public static void main(String[] args) throws IOException {
		Player p1 = new Player();
		Player p2 = new Player();
		ChGame mygame = new ChGame(p1, p2, 8, 4);
		boolean player = false;

		while (!mygame.isWon()) {
			Scanner bf = new Scanner(System.in);
			System.out.println("x:...");
			int x = bf.nextInt();
			System.out.println("y:...");
			int y = bf.nextInt();

			if (x >= 0) {
				if (player == false)
					mygame.move(p1, x, y);
				else
					mygame.move(p2, x, y);
			} else {
				mygame.undoLastMove();
			}
			player = !player;

			System.out.println(mygame.toString());
		}
		System.out.println("Spiel wurde gewonnen!");
	}

}
