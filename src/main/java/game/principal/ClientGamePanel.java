package game.principal;

import game.utilities.SnakeGameInfo;
import game.utilities.SoftSnakePlayer;
import javax.swing.*;
import java.awt.*;

public class ClientGamePanel extends JPanel {
    private SnakeGameInfo game;

    public ClientGamePanel(SnakeGameInfo game) {
        this.game = game;
        setFocusable(true);
    }

    public void setGameInfo(SnakeGameInfo snakeGameInfo) {
        this.game = snakeGameInfo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (game == null) {
            System.out.println("game es null");
            return;
        }

        // Fondo
        g.setColor(game.isGameOver() ? Color.BLACK : Color.WHITE);
        g.fillRect(0, 0, SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);

        // Dibujar serpientes
        drawSnake(g, game.getSnake1());
        drawSnake(g, game.getSnake2());
        drawSnake(g, game.getSnake3());
        drawSnake(g, game.getSnake4());

        // Dibujar comida
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
            int score3 = game.getSnake3().getBody().length - 1;
            int score4 = game.getSnake4().getBody().length - 1;
            g.drawString("SCORE P1: " + score1 + "  P2: " + score2 + "  P3: " + score3 + "  P4: " + score4, 300, 240);
            g.drawString("N to Start New Game", 100, 320);
            g.drawString("ESC to Exit", 100, 340);
        }
    }

    private void drawSnake(Graphics g, SoftSnakePlayer snake) {
        if (snake == null || snake.getBody() == null) return;
        g.setColor(snake.getColor());
        for (Point p : snake.getBody()) {
            g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
        }
    }
}