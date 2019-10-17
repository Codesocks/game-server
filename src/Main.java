public class Main {

    public static void main(String[] args) {
        int rows = 6;
        int cols = 7;
        boolean player1Turn = true;

        Connect4Game myGame = new Connect4Game(rows, cols);
        myGame.move(3,2);
        myGame.move(3,1);
        myGame.move(3,1);
        myGame.move(1,2);
        myGame.move(3, 1);
        System.out.println(myGame.checkWon(2,3,1));
        myGame.printGame();
    }
}
