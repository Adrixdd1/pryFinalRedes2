package connection;

import connection.snakeC.SnakeLANGame;
import java.io.IOException;
import java.net.*;

public class ServerThread extends Thread {
    private final int PORT;
    private BroadCastThread broadcastThread;
    private String nombre;

    public ServerThread(int port, String nombre) throws IOException {
        this.PORT = port;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        System.out.println("Servidor iniciado...");
        broadcastThread = new BroadCastThread(nombre);
        broadcastThread.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Esperando conexión de cliente en el puerto " + PORT);
            for (int i = 0; i < 4; i++) {
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());
                new SnakeLANGame(client);
            }
            broadcastThread.stopBroadcast();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}