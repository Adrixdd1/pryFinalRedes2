package game.principal;

import game.utilities.SnakeGameInfo;

import javax.swing.*;
import java.awt.*;

public class ClientGamePanel extends JPanel {
    private SnakeGameInfo game;
    private static final int TIMER_X = 20;
    private static final int TIMER_Y = 30;
    private static final int TIMER_WIDTH = 150;
    private static final int TIMER_HEIGHT = 30;


    public ClientGamePanel(SnakeGameInfo game) {
        this.game = game;
        setFocusable(true);
    }
    public void setGameInfo(SnakeGameInfo snakeGameInfo){
        this.game=snakeGameInfo;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (game == null) {
            System.out.println("game es null");
            return;
        }
        if (game.isGameOver()) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRect(0, 0, SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);

        // Dibuja la serpiente del jugador 1
        g.setColor(game.getSnake1().getColor());
        for (Point p : game.getSnake1().getBody()) {
            g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
        }

        // Dibuja la serpiente del jugador 2
        g.setColor(game.getSnake2().getColor());
        for (Point p : game.getSnake2().getBody()) {
            g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
        }

        // Dibuja la comida
        g.setColor(Color.RED);
        Point food = game.getFood();
        g.fillRect(food.x, food.y, SnakeGame.FOOD_SIZE, SnakeGame.FOOD_SIZE);

        // Mensaje de Game Over
        if (game.isGameOver()) {
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", 300, 200);
            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            int score1 = game.getSnake1().getBody().length - 1;
            int score2 = game.getSnake2().getBody().length - 1;
            g.drawString("SCORE P1: " + score1 + "  P2: " + score2, 300, 240);
            g.drawString("N to Start New Game", 100, 320);
            g.drawString("ESC to Exit", 100, 340);
        }
    }
}
