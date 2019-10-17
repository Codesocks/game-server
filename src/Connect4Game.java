public class Connect4Game {
    private int rows;
    private int cols;
    private int[][] game;

    public Connect4Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.game = new int[rows][cols];
    }

    public void setField(int row, int col, int player) {
        game[row][col] = player;
    }

    public int getField(int row, int col) {
        return game[row][col];
    }

    public void move(int col, int player) {
        if (getField(0, col) != 0) {
            System.out.println("There's no available space in this column.");
            return;
        }
        for (int i = rows - 1; i >= 0; i--) {
            if (getField(i, col) == 0) {
                setField(i, col, player);
                return;
            }
        }
    }

    public void printGame() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++)
                System.out.print(game[i][j] + " ");
            System.out.println();
        }
    }

    public int checkWon(int currentRow, int currentCol, int player) {
        int connected = 0;
        // vertical
        for(int i = currentRow; i < rows; i++) {
            if(game[i][currentCol] == player)
                connected++;
            else {
                connected = 0;
                break;
            }
            if(connected >= 4)
                return player;

        }
        return 0;
    }
}
