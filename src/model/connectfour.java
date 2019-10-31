package model;

import java.io.IOException;
import java.util.Scanner;

public class connectfour {

    public static void main(String[] args) throws IOException {
        Player p1 = new Player();
        Player p2 = new Player();
        p2.setComputer(true);
        CFGame mygame = new CFGame(p1, p2, 8, 4);
        Player currentPlayer = p1;

        while (true) {
            if (!currentPlayer.isComputer()) {
                Scanner bf = new Scanner(System.in);
                System.out.println("x:...");
                int x = bf.nextInt();

                if (x >= 0)
                    mygame.move(currentPlayer, x);
                else
                    mygame.undoLastMove();
            }
            else
                mygame.move(currentPlayer);

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

            System.out.println(mygame.toString());
        }
    }

}
