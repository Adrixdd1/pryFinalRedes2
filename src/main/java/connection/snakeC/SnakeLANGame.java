package connection.snakeC;

import game.principal.GameFrame;
import game.principal.SnakeGame;
import game.utilities.GameKeyListener;
import game.utilities.ServerKeyListener;
import game.utilities.SnakeGameInfo;
import game.utilities.SoftSnakePlayer;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class SnakeLANGame extends GameFrame {
    private Socket cliente;
    private ObjectOutputStream outputStream;
    private BufferedReader inputStream;
    private boolean running = true;

    public SnakeLANGame(Socket cliente) {
        super(false);
        this.cliente = cliente;
        super.panel.addKeyListener(new ServerKeyListener(super.game));
        super.panel.removeKeyListener(new GameKeyListener(game));
        try {
            outputStream = new ObjectOutputStream(cliente.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Iniciar hilos para enviar datos y recibir comandos
        new Thread(this::gameLoop).start();
        new Thread(this::listenForCommands).start();
    }

    private void gameLoop() {
        while (running) {
            try {
                // Obtiene el estado actual del juego
                SnakeGameInfo gameInfo = getGameInfo();

                // Envía la información al cliente
                outputStream.writeObject(gameInfo);
                outputStream.flush();

                // Pequeña pausa para evitar sobrecarga en la red
                Thread.sleep(10);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
        closeConnections();
    }

    private void listenForCommands() {
        try {
            String command;
            while (running && (command = inputStream.readLine()) != null) {
                System.out.println("recibido: "+command);
                SnakeGame juego = super.getInfo();
                juego.getSnake2().setDirection(command); // Aplicar la dirección al snake2
            }
        } catch (IOException e) {
            e.printStackTrace();
            running = false;
        }
    }

    private void closeConnections() {
        try {
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            if (cliente != null) cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SnakeGameInfo getGameInfo() {
        SnakeGame juego = super.getInfo();

        SoftSnakePlayer j1 = new SoftSnakePlayer(
                juego.getSnake1().getBody().toArray(new Point[0]),
                juego.getSnake1().getColor()
        );
        SoftSnakePlayer j2 = new SoftSnakePlayer(
                juego.getSnake2().getBody().toArray(new Point[0]),
                juego.getSnake2().getColor()
        );

        return new SnakeGameInfo(juego.getFood(), j2, j1, juego.isGameOver());
    }
}
