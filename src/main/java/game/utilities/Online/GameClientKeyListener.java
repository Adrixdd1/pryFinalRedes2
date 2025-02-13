package game.utilities.Online;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;

import game.Online.SnakeLANClientGame;

public class GameClientKeyListener extends KeyAdapter {
    private PrintWriter salida;
     private SnakeLANClientGame clientGame;


    public GameClientKeyListener(PrintWriter salida, SnakeLANClientGame clientGame) {
        this.salida = salida;
        this.clientGame = clientGame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Tecla para salir
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            salida.println("CLIENT_DISCONNECT");
            salida.flush();
            clientGame.dispose();
            clientGame.getStartScreen().setVisible(true); 
        }

        // Controles para el jugador 2 (WASD)
        try {
            String command = null;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                command = "RIGHT";
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                command = "LEFT";
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                command = "UP";
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                command = "DOWN";
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                command = "RIGHT";
            } else if (e.getKeyCode() == KeyEvent.VK_A) {
                command = "LEFT";
            } else if (e.getKeyCode() == KeyEvent.VK_W) {
                command = "UP";
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                command = "DOWN";
            }


            if (command != null) {
                salida.println(command);  // Enviar el comando con salto de línea
                salida.flush(); // Asegurar que se envía de inmediato
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}