package game.utilities.Online;

import game.Online.SnakeLANGame;
import game.principal.SnakeGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServerKeyListener extends KeyAdapter {
    private SnakeGame game;
    private SnakeLANGame gameFrame;  // Referencia al frame principal del juego

    public ServerKeyListener(SnakeGame game, SnakeLANGame gameFrame) {
        this.game = game;
        this.gameFrame = gameFrame; // Guardamos la referencia al frame
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
        if (e.getKeyCode() == KeyEvent.VK_D) {
            game.getSnake1().setDirection("RIGHT");
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            game.getSnake1().setDirection("LEFT");
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            game.getSnake1().setDirection("UP");
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            game.getSnake1().setDirection("DOWN");
        }
        // Nueva partida
        if (e.getKeyCode() == KeyEvent.VK_N) {
            gameFrame.startGame();
        }
    }
}
