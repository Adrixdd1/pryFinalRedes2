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

        // Dibuja la serpiente del jugador 1, si está activa
        if (game.getSnake1().isActive()) {
            g.setColor(game.getSnake1().getColor());
            for (Point p : game.getSnake1().getBody()) {
                g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
            }
        }

        // Dibuja la serpiente del jugador 2, si está activa
        if (game.getSnake2().isActive()) {
            g.setColor(game.getSnake2().getColor());
            for (Point p : game.getSnake2().getBody()) {
                g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
            }
        }

        // Dibuja la serpiente del jugador 3, si está activa
        if (game.getSnake3().isActive()) {
            g.setColor(game.getSnake3().getColor());
            for (Point p : game.getSnake3().getBody()) {
                g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
            }
        }

        // Dibuja la serpiente del jugador 4, si está activa
        if (game.getSnake4().isActive()) {
            g.setColor(game.getSnake4().getColor());
            for (Point p : game.getSnake4().getBody()) {
                g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
            }
        }

        // Dibuja la comida
        g.setColor(Color.RED);
        Point food = game.getFood();
        g.fillRect(food.x, food.y, SnakeGame.FOOD_SIZE, SnakeGame.FOOD_SIZE);

        // Mensaje de Game Over
        if (game.isGameOver()) {
            // Mostrar "GAME OVER" y configurar la fuente y color
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", 300, 200);

            // Variables para almacenar la información de jugadores activos y sus scores
            String activePlayers = "JUGADORES ACTIVOS: ";
            String activeScores = "";

            // Para cada jugador, comprobamos si está activo y, en ese caso, obtenemos su
            // score
            if (game.getSnake1().isActive()) {
                activePlayers += "P1 ";
                int score1 = game.getSnake1().getBody().size() - 1;
                activeScores += "SCORE P1: " + score1 + "  ";
            }
            if (game.getSnake2().isActive()) {
                activePlayers += "P2 ";
                int score2 = game.getSnake2().getBody().size() - 1;
                activeScores += "SCORE P2: " + score2 + "  ";
            }
            if (game.getSnake3().isActive()) {
                activePlayers += "P3 ";
                int score3 = game.getSnake3().getBody().size() - 1;
                activeScores += "SCORE P3: " + score3 + "  ";
            }
            if (game.getSnake4().isActive()) {
                activePlayers += "P4 ";
                int score4 = game.getSnake4().getBody().size() - 1;
                activeScores += "SCORE P4: " + score4 + "  ";
            }

            // Configuramos la fuente para mostrar los textos de status y score
            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            g.drawString(activeScores, 100, 240);
            g.drawString(activePlayers, 100, 280);

            // Mostrar opciones adicionales
            g.drawString("N to Start New Game", 100, 320);
            g.drawString("ESC to Exit", 100, 340);
        }

    }
}