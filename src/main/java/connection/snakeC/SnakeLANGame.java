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
    private List<Socket> clients = new ArrayList<>();
    private boolean running = true;

    public SnakeLANGame(Socket cliente) {
        super(false);
        clients.add(cliente);
        if (clients.size() == 1) {
            new Thread(this::gameLoop).start();
        }
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
                SnakeGameInfo gameInfo = getGameInfo();
                for (Socket client : clients) {
                    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                    output.writeObject(gameInfo);
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