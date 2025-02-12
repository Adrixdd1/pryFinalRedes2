package game.utilities.Online;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class SnakeGameInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private SoftSnakePlayer snake1, snake2, snake3, snake4;
    private Point food;
    private boolean gameOver;

    public SnakeGameInfo(Point food, SoftSnakePlayer s2, SoftSnakePlayer s1, SoftSnakePlayer s3, SoftSnakePlayer s4, boolean gameOver) {
        this.food = food;
        this.snake1 = new SoftSnakePlayer(s1.getBody(), s1.getColor(), s1.isActive());
        this.snake2 = new SoftSnakePlayer(s2.getBody(), s2.getColor(), s2.isActive());
        this.snake3 = new SoftSnakePlayer(s3.getBody(), s3.getColor(), s3.isActive());
        this.snake4 = new SoftSnakePlayer(s4.getBody(), s4.getColor(), s4.isActive());
        this.gameOver = gameOver;
    }

    public SnakeGameInfo() {
    }

    public SoftSnakePlayer getSnake1() { return snake1; }
    public SoftSnakePlayer getSnake2() { return snake2; }
    public SoftSnakePlayer getSnake3() { return snake3; }
    public SoftSnakePlayer getSnake4() { return snake4; }
    public Point getFood() { return food; }
    public boolean isGameOver() { return gameOver; }
}
