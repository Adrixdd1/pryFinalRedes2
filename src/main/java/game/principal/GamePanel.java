package game.principal;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

public class GamePanel extends JPanel {
    private SnakeGame game;
    private static final int TIMER_X = 20;
    private static final int TIMER_Y = 30;
    private static final int TIMER_WIDTH = 150;
    private static final int TIMER_HEIGHT = 30;

    public GamePanel(SnakeGame game) {
        this.game = game;
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fondo
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

        // Dibuja la serpiente del jugador 3
        g.setColor(game.getSnake3().getColor());
        for (Point p : game.getSnake3().getBody()) {
            g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
        }

        // Dibuja la serpiente del jugador 4
        g.setColor(game.getSnake4().getColor());
        for (Point p : game.getSnake4().getBody()) {
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
            int score1 = game.getSnake1().getBody().size() - 1;
            int score2 = game.getSnake2().getBody().size() - 1;
            int score3 = game.getSnake3().getBody().size() - 1;
            int score4 = game.getSnake4().getBody().size() - 1;
            g.drawString("SCORE P1: " + score1 + "  P2: " + score2 + "  P3: " + score3 + "  P4: " + score4, 100, 240);
            g.drawString("N to Start New Game", 100, 320);
            g.drawString("ESC to Exit", 100, 340);
        }
    }
}