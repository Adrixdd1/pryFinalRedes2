package game.principal;

import game.utilities.GameLoop;
import game.utilities.SnakePlayer;

import java.awt.Point;
import java.util.Random;
import java.awt.Color;

public class SnakeGame {
    private SnakePlayer snake1;
    private SnakePlayer snake2;
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
        snake1 = new SnakePlayer(new Point(320, 240), "RIGHT", Color.BLUE, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        snake2 = new SnakePlayer(new Point(320, 300), "LEFT", Color.GREEN, STEP_SIZE, BOARD_WIDTH, BOARD_HEIGHT);
        gameOver = false;
    }

    public SnakePlayer getSnake1() {
        return snake1;
    }

    public SnakePlayer getSnake2() {
        return snake2;
    }

    public Point getFood() {
        return food;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    // Genera una posición aleatoria para la comida (alineada al STEP_SIZE)
    public void generateFood() {
        Random rnd = new Random();
        int x = rnd.nextInt(BOARD_WIDTH);
        x = (x / STEP_SIZE) * STEP_SIZE;
        int y = rnd.nextInt(BOARD_HEIGHT);
        y = (y / STEP_SIZE) * STEP_SIZE;
        food = new Point(x, y);
    }

    // Actualiza el estado del juego: mueve las serpientes y comprueba si han comido la comida.
    public void update() {
        if (!gameOver) {
            //comprueba colision con uno mismo
            boolean colisionp1 = snake1.move();
            if(colisionp1){
               gameOver = true;
            }
            boolean colisionp2 = snake2.move();
            if(colisionp1){
                gameOver = true;
            }

            // Comprueba si la serpiente 1 come la comida
            Point head1 = snake1.getHead();
            if (head1.distance(food) < STEP_SIZE) {
                snake1.grow();
                generateFood();
            }

            // Comprueba si la serpiente 2 come la comida
            Point head2 = snake2.getHead();
            if (head2.distance(food) < STEP_SIZE) {
                snake2.grow();
                generateFood();
            }

            // Aquí podrías agregar más comprobaciones de colisión (por ejemplo, contra sí mismas o entre ambas serpientes)
        }
    }
}

