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
    private List<ClientHandler> clients = new ArrayList<>();

    public SnakeLANGame() {
        super(false);
        panel.addKeyListener(new ServerKeyListener(super.game));
        panel.removeKeyListener(new GameKeyListener(game));
    }

    public void addClient(Socket clientSocket, int playerId) {
        ClientHandler client = new ClientHandler(clientSocket, playerId);
        clients.add(client);
        client.start();
    }

    private SnakeGameInfo getGameInfo() {
        SnakeGame game = super.getInfo();
        return new SnakeGameInfo(
                game.getFood(),
                new SoftSnakePlayer(game.getSnake2().getBody().toArray(new Point[0]), game.getSnake2().getColor(),
                        game.getSnake2().isActive()),
                new SoftSnakePlayer(game.getSnake1().getBody().toArray(new Point[0]), game.getSnake1().getColor(),
                        game.getSnake1().isActive()),
                new SoftSnakePlayer(game.getSnake3().getBody().toArray(new Point[0]), game.getSnake3().getColor(),
                        game.getSnake3().isActive()),
                new SoftSnakePlayer(game.getSnake4().getBody().toArray(new Point[0]), game.getSnake4().getColor(),
                        game.getSnake4().isActive()),
                game.isGameOver());
    }

    private class ClientHandler extends Thread {
        private final Socket socket;
        private final int playerId;
        private ObjectOutputStream out;
        private BufferedReader in;

        public ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //out.writeObject(playerId); // Enviar ID al cliente
                //out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // Hilo para enviar estado del juego
                new Thread(() -> {
                    try {
                        SnakeGame game = getInfo();
                        switch (playerId) {
                            case 2:
                                game.getSnake2().setActive(true);
                                break;
                            case 3:
                                game.getSnake3().setActive(true);
                                break;
                            case 4:
                                game.getSnake4().setActive(true);
                                break;
                        }
                        while (true) {
                            SnakeGameInfo info = getGameInfo();
                            out.writeObject(info);
                            out.flush();
                            Thread.sleep(10);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                // Hilo para recibir comandos
                String command;
                while ((command = in.readLine()) != null) {

                    switch (playerId) {
                        case 2:
                            game.getSnake2().setDirection(command);
                            break;
                        case 3:
                            game.getSnake3().setDirection(command);
                            break;
                        case 4:
                            game.getSnake4().setDirection(command);
                            break;
                    }
                }
            } catch (IOException e) {
                switch (playerId) {
                    case 2:
                        game.getSnake2().setActive(false);
                        break;
                    case 3:
                        game.getSnake3().setActive(false);
                        break;
                    case 4:
                        game.getSnake4().setActive(false);
                        break;
                }
            }
        }

    }
}