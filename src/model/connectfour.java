package model;

import java.io.IOException;
import java.util.Scanner;

public class connectfour {

	public static void main(String[] args) throws IOException {
		Player p1 = new Player();
		Player p2 = new Player();
		CFGame mygame = new CFGame(p1, p2, 8, 4);
		boolean player = false;

		while (true) {
			Scanner bf = new Scanner(System.in);
			System.out.println("x:...");
			int x = bf.nextInt();

			if (x >= 0) {
				if (player == false)
					mygame.move(p1, x);
				else
					mygame.move(p2, x);
				player = !player;
			} else {
				mygame.undoLastMove();
			}
			player = !player;
			
			System.out.println(mygame.toString());
		}
	}

}
