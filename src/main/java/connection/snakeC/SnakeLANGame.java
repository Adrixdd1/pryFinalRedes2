package connection.snakeC;

import game.principal.GameFrame;
import game.principal.SnakeGame;
import game.utilities.SnakeGameInfo;
import game.utilities.SoftSnakePlayer;

import java.awt.Point;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SnakeLANGame extends GameFrame {
    private List<ObjectOutputStream> clientOutputs = new ArrayList<>(); // Lista para almacenar los ObjectOutputStream
    private boolean running = true;

    public SnakeLANGame(Socket cliente) {
        super(false);
        try {
            // Crear ObjectOutputStream para el cliente y almacenarlo
            ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
            clientOutputs.add(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clientOutputs.size() == 1) { // Iniciar el gameLoop solo una vez
            new Thread(this::gameLoop).start();
        }
        configurarCliente(cliente, clientOutputs.size());
    }

    private void configurarCliente(Socket cliente, int playerNumber) {
        try {
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
                SnakeGameInfo gameInfo = getGameInfo();
                for (ObjectOutputStream output : clientOutputs) {
                    output.writeObject(gameInfo); // Reutilizar el mismo ObjectOutputStream
                    output.flush();
                }
                Thread.sleep(10);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
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