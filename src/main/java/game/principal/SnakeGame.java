package game.principal;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import game.utilities.SnakePlayer;

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
     public void startGame(boolean gameOver) {
        food = new Point(200, 100);
        snake1 = new SnakePlayer(new Point(320, 240), "RIGHT", Color.BLUE, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake2 = new SnakePlayer(new Point(320, 300), "LEFT", Color.GREEN, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake3 = new SnakePlayer(new Point(320, 200), "UP", Color.YELLOW, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake4 = new SnakePlayer(new Point(320, 340), "DOWN", Color.MAGENTA, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        gameOver = true;
    }
    public void startGame() {
        food = new Point(200, 100);
        snake1 = new SnakePlayer(new Point(320, 240), "RIGHT", Color.BLUE, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake2 = new SnakePlayer(new Point(320, 300), "LEFT", Color.GREEN, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake3 = new SnakePlayer(new Point(320, 200), "UP", Color.YELLOW, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake4 = new SnakePlayer(new Point(320, 340), "DOWN", Color.MAGENTA, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        gameOver = false;
    }

    public SnakePlayer getSnake1() { return snake1; }
    public SnakePlayer getSnake2() { return snake2; }
    public SnakePlayer getSnake3() { return snake3; }
    public SnakePlayer getSnake4() { return snake4; }

    public Point getFood() { return food; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public void generateFood() {
        Random rnd = new Random();
        int x = rnd.nextInt(BOARD_WIDTH);
        x = (x / STEP_SIZE) * STEP_SIZE;
        int y = rnd.nextInt(BOARD_HEIGHT);
        y = (y / STEP_SIZE) * STEP_SIZE;
        food = new Point(x, y);
    }

    public void update() {
        if (!gameOver) {
            boolean c1=false;
            boolean c2=false;
            boolean c3=false;
            boolean c4=false;
            if (snake1.isActive()){
                 c1 = snake1.move();}
            if (snake2.isActive()){
                 c2 = snake2.move();
            } 
            if (snake3.isActive()){
                 c3 = snake3.move();
            }
            if (snake4.isActive()) {
                 c4 = snake4.move();}

            if (c1 || c2 || c3 || c4) gameOver = true;
            peroSiChocoONeh();
            if (gameOver) {
                return;
            }
            checkFood(snake1);
            checkFood(snake2);
            checkFood(snake3);
            checkFood(snake4);
        }
    }

    private void checkFood(SnakePlayer snake) {
        Point head = snake.getHead();
        if (head.distance(food) < STEP_SIZE) {
            snake.grow();
            generateFood();
        }
    }
    //para checar si algún jugador colisionó con otro
    private void peroSiChocoONeh() {
        SnakePlayer[] snakes = {snake1, snake2, snake3, snake4};
        for (int i = 0; i < snakes.length; i++) {
            Point head = snakes[i].getHead();
            for (int j = 0; j < snakes.length; j++) {
                if (i != j) {
                    if (snakes[j].getBody().contains(head)) {
                        gameOver = true;
                        return;
                    }
                }
            }
        }
    }
}