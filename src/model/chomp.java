package model;

import java.io.IOException;
import java.util.Scanner;

public class chomp {

	public static void main(String[] args) throws IOException {
		Player p1 = new Player();
		Player p2 = new Player();
		p2.setComputer(true);
		ChGame mygame = new ChGame(p1, p2, 8, 4);
		Player currentPlayer = p1;

		while (!mygame.isWon()) {
			if (!currentPlayer.isComputer()) {
				Scanner bf = new Scanner(System.in);
				System.out.println("x:...");
				int x = bf.nextInt();
				System.out.println("y:...");
				int y = bf.nextInt();

				mygame.move(currentPlayer, x, y);
			}
			else
				mygame.moveComputerPlayer(currentPlayer);

			System.out.println(mygame.toString());

			if (mygame.isWon()) {
				if (mygame.getWinner().equals(mygame.getPlayer1()))
					System.out.println("Winner is Player 1");
				else
					System.out.println("Winner is Player 2");
				break;
			}

			if (currentPlayer.equals(p1))
				currentPlayer = p2;
			else
				currentPlayer = p1;
		}
	}

}
