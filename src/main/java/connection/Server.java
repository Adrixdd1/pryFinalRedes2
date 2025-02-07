package connection;

import connection.snakeC.SnakeLANGame;
import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 12345;
    private static Socket clientConnected;
    private static BroadCastThread broadcastThread;

    public static void main(String[] args) {
        System.out.println("Servidor iniciado...");

        // Inicia el broadcast para que los clientes lo detecten.
        broadcastThread = new BroadCastThread();
        broadcastThread.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Esperando conexión de cliente para el juego LAN en el puerto " + PORT);
            clientConnected = serverSocket.accept();
            System.out.println("Cliente conectado: " + clientConnected.getInetAddress());
            // Detiene el broadcast una vez que se conecta un cliente.
            broadcastThread.stopBroadcast();
            // Inicia la transmisión del juego al cliente.
            new SnakeLANGame(clientConnected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
