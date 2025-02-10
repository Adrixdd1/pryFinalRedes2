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
        System.out.println("Servidor iniciado...");
        // Inicia el broadcast para que los clientes lo detecten.
        broadcastThread = new BroadCastThread(nombre);
        broadcastThread.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Esperando conexión de cliente en el puerto " + PORT);
            clientConnected = serverSocket.accept();
            System.out.println("Cliente conectado: " + clientConnected.getInetAddress());
            // Detiene el broadcast una vez que se conecta un cliente.
            broadcastThread.stopBroadcast();
            // Inicia el juego en modo servidor.
            new SnakeLANGame(clientConnected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}