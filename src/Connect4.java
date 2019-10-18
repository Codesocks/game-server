public class Connect4 extends Game implements Recordable{

    @Override
    public void move() {

    }

    @Override
    public void turn() {

    }

    @Override
    public void newMove(int row, int column, Player player) {
        Move currentMove = new Move(row, column, player);
        stack.push(currentMove);
    }

    @Override
    public void deleteMove() {
        stack.pop();
    }
}
