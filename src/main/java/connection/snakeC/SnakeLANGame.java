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
import java.util.ArrayList;
import java.util.List;


public class SnakeLANGame extends GameFrame {
    private List<Socket> clients = new ArrayList<>();
    private ObjectOutputStream outputStream;
    private BufferedReader inputStream;
    private boolean running = true;

    public SnakeLANGame(Socket cliente) {
        super(false);
        clients.add(cliente);
        if (clients.size() == 1) {
            // Iniciar solo una vez el gameLoop
            new Thread(this::gameLoop).start();
        }
        // Configurar entrada/salida para cada cliente
        configurarCliente(cliente, clients.size());
    }
    private void configurarCliente(Socket cliente, int playerNumber) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            
            new Thread(() -> {
                try {
                    String command;
                    while ((command = input.readLine()) != null) {
                        SnakeGame juego = super.getInfo();
                        switch (playerNumber) {
                            case 1: juego.getSnake2().setDirection(command); break;
                            case 2: juego.getSnake3().setDirection(command); break;
                            case 3: juego.getSnake4().setDirection(command); break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (clients != null) clients.get(1).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SnakeGameInfo getGameInfo() {
        SnakeGame juego = super.getInfo();
        return new SnakeGameInfo(
            juego.getFood(),
            new SoftSnakePlayer(juego.getSnake2().getBody().toArray(new Point[0]), juego.getSnake2().getColor()),
            new SoftSnakePlayer(juego.getSnake3().getBody().toArray(new Point[0]), juego.getSnake3().getColor()),
            new SoftSnakePlayer(juego.getSnake4().getBody().toArray(new Point[0]), juego.getSnake4().getColor()),
            juego.isGameOver()
        );
    }
}
