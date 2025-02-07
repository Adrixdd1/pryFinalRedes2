package game.utilities;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class SnakeGameInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private SoftSnakePlayer snake1;
    private SoftSnakePlayer snake2;
    private Point food;
    private boolean gameOver;
    public static final int BOARD_WIDTH = 640;
    public static final int BOARD_HEIGHT = 480;
    public static final int STEP_SIZE = 10;
    public static final int FOOD_SIZE = 10;

    public SnakeGameInfo(Point food, SoftSnakePlayer snake2, SoftSnakePlayer snake1, boolean gameOver) {
        this.food = food;
        this.snake2 = snake2;
        this.snake1 = snake1;
        this.gameOver = gameOver;
    }

    public SnakeGameInfo() {
    }

    public SoftSnakePlayer getSnake1() {
        return snake1;
    }

    public SoftSnakePlayer getSnake2() {
        return snake2;
    }

    public Point getFood() {
        return food;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
