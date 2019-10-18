import java.util.Stack;

public interface Recordable {
    Stack<Move> stack = new Stack<>();

    // abstract als modifier redundant aber was solls
    abstract void newMove(int row, int column, Player player);

    // einfach nur pop oder gezielt löschen?
    abstract void deleteMove();
}
