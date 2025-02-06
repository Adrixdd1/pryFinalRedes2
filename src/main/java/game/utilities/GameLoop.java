package game.utilities;

import game.principal.GamePanel;
import game.principal.SnakeGame;

public class GameLoop extends Thread {
    private SnakeGame game;
    private GamePanel panel;
    private long frequency;

    public GameLoop(SnakeGame game, GamePanel panel, long frequency) {
        this.game = game;
        this.panel = panel;
        this.frequency = frequency;
    }

    @Override
    public void run() {
        long last = 0;
        while (true) {
            if (System.currentTimeMillis() - last > frequency) {
                if (!game.isGameOver()) {
                    game.update();
                }
                panel.repaint();
                last = System.currentTimeMillis();
            }
        }
    }
}
