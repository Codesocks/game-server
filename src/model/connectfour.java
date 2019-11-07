package model;

import java.io.IOException;
import java.util.Scanner;

public class connectfour {

	public static void main(String[] args) throws IOException {
		Player p1 = new Player();
		Player p2 = new Player();
		p2.setComputer(true);
		CFGame mygame = new CFGame(p1, p2, 8, 4);

		while (true) {
			Scanner bf = new Scanner(System.in);
			System.out.println("x:...");
			int x = bf.nextInt();

			mygame.move(mygame.getCurrentPlayer(), x);

			System.out.println(mygame.toString());

			if (mygame.isWon()) {
				if (mygame.getWinner().equals(mygame.getPlayer1()))
					System.out.println("Winner is Player 1");
				else
					System.out.println("Winner is Player 2");
				break;
			}
		}
	}

}
