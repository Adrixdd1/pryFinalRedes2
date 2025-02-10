package game.utilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GameClientKeyListener extends KeyAdapter {
    private final PrintWriter out;
    private final int playerId;

    public GameClientKeyListener(PrintWriter out, int playerId) {
        this.out = out;
        this.playerId = playerId;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String command = null;
        switch (playerId) {
            case 2: // Flechas
                command = getArrowKey(e);
                break;
            case 3: // WASD
                command = getWASD(e);
                break;
            case 4: // IJKL
                command = getIJKL(e);
                break;
        }
        if (command != null) {
            out.println(command);
            out.flush();
        }
    }

    private String getArrowKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT: return "RIGHT";
            case KeyEvent.VK_LEFT: return "LEFT";
            case KeyEvent.VK_UP: return "UP";
            case KeyEvent.VK_DOWN: return "DOWN";
            default: return null;
        }
    }

    private String getWASD(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D: return "RIGHT";
            case KeyEvent.VK_A: return "LEFT";
            case KeyEvent.VK_W: return "UP";
            case KeyEvent.VK_S: return "DOWN";
            default: return null;
        }
    }

    private String getIJKL(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_L: return "RIGHT";
            case KeyEvent.VK_J: return "LEFT";
            case KeyEvent.VK_I: return "UP";
            case KeyEvent.VK_K: return "DOWN";
            default: return null;
        }
    }
}
