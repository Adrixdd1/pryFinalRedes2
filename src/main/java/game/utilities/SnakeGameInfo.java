package game.utilities;

import java.awt.*;
import java.io.Serializable;

public class SnakeGameInfo implements Serializable {
    private SoftSnakePlayer snake1;
    private SoftSnakePlayer snake2;
    private SoftSnakePlayer snake3;
    private SoftSnakePlayer snake4;
    private Point food;
    private boolean gameOver;

    public SnakeGameInfo(Point food,SoftSnakePlayer s1, SoftSnakePlayer s2, SoftSnakePlayer s3, SoftSnakePlayer s4, boolean gameOver) {
        this.food = food;
        this.snake1 = s1;
        this.snake2 = s2;
        this.snake3 = s3;
        this.snake4 = s4;
        this.gameOver = gameOver;
    }

    public SoftSnakePlayer getSnake1() { return snake1; }
    public SoftSnakePlayer getSnake2() { return snake2; }
    public SoftSnakePlayer getSnake3() { return snake3; }
    public SoftSnakePlayer getSnake4() { return snake4; }
    public void setSnake1(SoftSnakePlayer snake1) {
        this.snake1 = snake1;
    }

    public void setSnake2(SoftSnakePlayer snake2) {
        this.snake2 = snake2;
    }

    public void setSnake3(SoftSnakePlayer snake3) {
        this.snake3 = snake3;
    }

    public void setSnake4(SoftSnakePlayer snake4) {
        this.snake4 = snake4;
    }
    public Point getFood() { return food; }
    public boolean isGameOver() { return gameOver; }
}