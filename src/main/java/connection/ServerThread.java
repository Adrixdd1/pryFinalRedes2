package connection;

import connection.snakeC.SnakeLANGame;
import java.io.IOException;
import java.net.*;

public class ServerThread extends Thread {
    private final int PORT;
    private BroadCastThread broadcastThread;
    private String nombre;
    private int connectedPlayers;
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
           
            while (connectedPlayers < 4) { // Aceptar hasta 4 jugadores
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());
                new SnakeLANGame(client);
                connectedPlayers++;
            }
            broadcastThread.stopBroadcast();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}