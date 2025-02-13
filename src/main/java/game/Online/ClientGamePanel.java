package game.Online;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import game.principal.SnakeGame;
import game.utilities.Online.SnakeGameInfo;
import game.utilities.Online.SoftSnakePlayer;

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

        drawSnake(g, game.getSnake1());
        drawSnake(g, game.getSnake2());
        drawSnake(g, game.getSnake3());
        drawSnake(g, game.getSnake4());

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
            String scoreText = "";
            if (game.getSnake1().isActive()) {
                int score1 = game.getSnake1().getBody().length - 1;
                scoreText += "SCORE P1: " + score1 + "  ";
            }
            
            // Comprobamos si el jugador 2 está activo, y en ese caso obtenemos y añadimos su score
            if (game.getSnake2().isActive()) {
                int score2 = game.getSnake2().getBody().length - 1;
                scoreText += "SCORE P2: " + score2 + "  ";
            }
            
            if (game.getSnake3().isActive()) {
                int score3 = game.getSnake3().getBody().length - 1;
                scoreText += "SCORE P3: " + score3 + "  ";
            }
            if (game.getSnake4().isActive()) {
                int score4 = game.getSnake4().getBody().length - 1;
                scoreText += "SCORE P4: " + score4 + "  ";
            }
            g.drawString("N to Start New Game", 100, 320);
            g.drawString("ESC to Exit", 100, 340);
        }
    }
    private void drawSnake(Graphics g, SoftSnakePlayer snake) {
        if (!snake.isActive()) {
            return;
        }
    
        // Para las serpientes activas, se utiliza su color asignado.
        g.setColor(snake.getColor());
        
        // Dibujar el cuerpo de la serpiente.
        for (Point p : snake.getBody()) {
            g.fillRect(p.x, p.y, SnakeGame.STEP_SIZE, SnakeGame.STEP_SIZE);
        }
    }
}
