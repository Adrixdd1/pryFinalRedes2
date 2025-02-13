package game.Online;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import game.principal.GameFrame;
import game.principal.SnakeGame;
import game.principal.StartScreen;
import game.utilities.GameKeyListener;
import game.utilities.Online.ServerKeyListener;
import game.utilities.Online.SnakeGameInfo;
import game.utilities.Online.SoftSnakePlayer;

public class SnakeLANGame extends GameFrame {
    private List<ClientHandler> clients = new ArrayList<>();
    protected StartScreen startScreen;
    public SnakeLANGame() {
        super(false);
        panel.addKeyListener(new ServerKeyListener(super.game,this));
        panel.removeKeyListener(new GameKeyListener(game));
    }
    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }
    public StartScreen getStartScreen() {
        return startScreen;
    }
    public void broadcastServerClosed() {
        for (ClientHandler client : clients) {
            try {
                client.out.writeObject("SERVER_CLOSED");
                client.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void addClient(int playerId,ObjectOutputStream out, InputStreamReader in) {
        ClientHandler client = new ClientHandler(playerId,out,in);
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
    public void startGame() {
        this.setTitle("Snake -"+ (clients.size()+1) + "jugadores");
        boolean algo = true;
        game.startGame(algo);
        game.getSnake1().setActive(true);
        for (ClientHandler client : clients) {
            switch (client.playerId) {
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
        }
        game.setGameOver(false);
        System.out.println("reiniciando juego desde acá");
    }


    private class ClientHandler extends Thread {
        private final int playerId;
        private final ObjectOutputStream out ;
        private final BufferedReader in ;
        public ClientHandler( int playerId,ObjectOutputStream out, InputStreamReader in) {
            this.playerId = playerId;
            this.in=new BufferedReader(in);
            this.out=out;
        }
        @Override
        public void run() {
            try {
                new Thread(() -> {
                    try {
                        while (true) {
                            // Se obtiene la información actual del juego
                            SnakeGameInfo info = getGameInfo();
                            // Se envía la información al cliente
                            out.writeObject(info);
                            out.flush();
                            // Pausa breve para controlar la frecuencia de actualización
                            Thread.sleep(50);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
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
                    if ("CLIENT_DISCONNECT".equals(command)) {
                        // Marcar jugador como inactivo
                        switch (playerId) {
                            case 2: game.getSnake2().setActive(false); break;
                            case 3: game.getSnake3().setActive(false); break;
                            case 4: game.getSnake4().setActive(false); break;
                        }
                        clients.remove(this);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}