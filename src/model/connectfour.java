package model;

import management.User;

import java.io.IOException;
import java.util.Scanner;

public class connectfour {

	public static void main(String[] args) throws IOException {
		User p1 = new User("User 1");
		User p2 = new User("User 2");
		p2.setComputer(true);
		CFGame mygame = new CFGame(p1, p2, 8, 4, true);

		while (true) {
			@SuppressWarnings("resource")
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
