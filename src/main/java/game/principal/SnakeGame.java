package game.principal;

import game.utilities.SnakePlayer;
import java.awt.Point;
import java.util.Random;
import java.awt.Color;

public class SnakeGame {
    private SnakePlayer snake1;
    private SnakePlayer snake2;
    private SnakePlayer snake3;
    private SnakePlayer snake4;
    private Point food;
    private boolean gameOver;

    public static final int BOARD_WIDTH = 640;
    public static final int BOARD_HEIGHT = 480;
    public static final int STEP_SIZE = 10;
    public static final int FOOD_SIZE = 10;

    public SnakeGame() {
        startGame();
    }

    public void startGame() {
        food = new Point(200, 100);
        snake1 = new SnakePlayer(new Point(320, 240), "RIGHT", Color.BLUE, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT,true);
        snake2 = new SnakePlayer(new Point(320, 300), "LEFT", Color.GREEN, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT,false);
        snake3 = new SnakePlayer(new Point(300, 240), "UP", Color.YELLOW, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT,false);
        snake4 = new SnakePlayer(new Point(300, 300), "DOWN", Color.MAGENTA, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT,false);
        
        gameOver = false;
    }

    public SnakePlayer getSnake1() { return snake1; }
    public SnakePlayer getSnake2() { return snake2; }
    public SnakePlayer getSnake3() { return snake3; }
    public SnakePlayer getSnake4() { return snake4; }
    public Point getFood() { return food; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public void activateSnake2() {
        snake2.setActive(true);
    }
    public void activateSnake3() {
        snake3.setActive(true);
    }
    public void activateSnake4() {
        snake4.setActive(true);
    }
    public void generateFood() {
        Random rnd = new Random();
        int x = (rnd.nextInt(BOARD_WIDTH) / STEP_SIZE) * STEP_SIZE;
        int y = (rnd.nextInt(BOARD_HEIGHT) / STEP_SIZE) * STEP_SIZE;
        food = new Point(x, y);
    }

    public void update() {
        if (!gameOver) {
            if (snake1.isActive()) checkCollision(snake1);
            if (snake2.isActive()) checkCollision(snake2);
            if (snake3.isActive()) checkCollision(snake3);
            if (snake4.isActive()) checkCollision(snake4);
        }
    }

    private void checkCollision(SnakePlayer snake) {
        if (snake.move()) gameOver = true;
        if (snake.getHead().distance(food) < STEP_SIZE) {
            snake.grow();
            generateFood();
        }
    }
}