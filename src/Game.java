public abstract class Game {
    Player player1 = new Player();
    Player player2 = new Player();
    Field field = new Field();

    public abstract void move();
    public abstract void turn();
}
