package connection.snakeC;

import game.principal.ClientGamePanel;
import game.principal.SnakeGame;
import game.utilities.ClientGameLoop;
import game.utilities.GameClientKeyListener;
import game.utilities.SnakeGameInfo;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class SnakeLANClientGame extends JFrame {
    private ObjectInputStream servidor;
    private PrintWriter salida;

    public SnakeLANClientGame(Socket servidorSocket) {
        try {
            this.servidor = new ObjectInputStream(servidorSocket.getInputStream());
            salida = new PrintWriter(new OutputStreamWriter(servidorSocket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Configuración de la ventana...
        setTitle("Snake - Client");
        setSize(SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ClientGamePanel panel = new ClientGamePanel(null); // Inicializar sin datos
        add(panel);
        setVisible(true);

        // Configurar listeners...
        panel.addKeyListener(new GameClientKeyListener(salida));
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        // Hilo para leer actualizaciones del servidor
        new Thread(() -> {
            try {
                while (true) {
                    SnakeGameInfo gameInfo = (SnakeGameInfo) servidor.readObject();
                    panel.setGameInfo(gameInfo);
                    panel.repaint();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}