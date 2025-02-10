package connection;

import connection.snakeC.SnakeLANGame;

import java.io.IOException;
import java.net.*;

public class ServerThread extends Thread {
    private final int PORT;
    private Socket clientConnected;
    private BroadCastThread broadcastThread;
    private String nombre;
    public ServerThread(int port,String nombre) throws IOException {
        this.PORT = port;
        this.nombre=nombre;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            SnakeLANGame game = new SnakeLANGame();
            for (int i = 0; i < 3; i++) {
                Socket client = serverSocket.accept();
                game.addClient(client, i + 2); // Asignar IDs 2,3,4
                
            }
            broadcastThread.stopBroadcast();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
