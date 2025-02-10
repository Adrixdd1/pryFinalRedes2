package game.utilities;

import game.principal.SnakeGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyListener extends KeyAdapter {
    private SnakeGame game;

    public GameKeyListener(SnakeGame game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Tecla para salir
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
         //Controles para el jugador 1 (flechas)
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.getSnake1().setDirection("RIGHT");
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            game.getSnake1().setDirection("LEFT");
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            game.getSnake1().setDirection("UP");
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            game.getSnake1().setDirection("DOWN");
        }
        // Controles para el jugador 2 (WASD)
        if (e.getKeyCode() == KeyEvent.VK_D) {
            game.getSnake2().setDirection("RIGHT");
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            game.getSnake2().setDirection("LEFT");
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            game.getSnake2().setDirection("UP");
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            game.getSnake2().setDirection("DOWN");
        }
        // Nueva partida
        if (e.getKeyCode() == KeyEvent.VK_N) {
            game.startGame();
        }
    }
}