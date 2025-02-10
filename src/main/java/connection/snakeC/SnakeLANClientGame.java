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
    private Thread loop;

    public SnakeLANClientGame(Socket servidorSocket) {
        try {
            this.servidor = new ObjectInputStream(servidorSocket.getInputStream());
            salida = new PrintWriter(new OutputStreamWriter(servidorSocket.getOutputStream()), true);        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        setTitle("Snake - Client");
        setSize(SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

        ClientGamePanel panel;
        try {
            panel = new ClientGamePanel((SnakeGameInfo) this.servidor.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        add(panel);
        setVisible(true);

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(new GameClientKeyListener(salida)); // Agregar el KeyListener

        // Ejecutar el loop en un hilo separado
        new Thread(() -> {
            loop = new ClientGameLoop(panel, this.servidor);
            loop.start();
        }).start();
    }
}