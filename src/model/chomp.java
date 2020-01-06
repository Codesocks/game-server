package model;

import management.User;

import java.io.IOException;
import java.util.Scanner;

public class chomp {

	public static void main(String[] args) throws IOException {
		User p1 = new User("User 1");
		User p2 = new User("User 2");
		p2.setComputer(true);
		ChGame mygame = new ChGame(p1, p2, 8, 4);
		User currentPlayer = p1;

		while (!mygame.isWon()) {
			currentPlayer = mygame.getCurrentPlayer();
			@SuppressWarnings("resource")
			Scanner bf = new Scanner(System.in);
			System.out.println("x:...");
			int x = bf.nextInt();
			System.out.println("y:...");
			int y = bf.nextInt();

			mygame.move(currentPlayer, x, y);

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
