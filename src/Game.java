public abstract class Game {
    Player player1 = new Player();
    Player player2 = new Player();
    // geht nich weil Field abstract !
    // Field field = new Field();

    public abstract void move();
    public abstract void turn();
}
