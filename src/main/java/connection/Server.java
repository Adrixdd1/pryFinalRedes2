package connection;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final Set<PrintWriter> clients = Collections.synchronizedSet(new HashSet<>());

    // Conservamos la referencia del BroadcastThread para detenerlo cuando sea necesario.
    private static BroadCastThread broadcastThread;

    public static void main(String[] args) {
        System.out.println("Servidor iniciado...");

        // Iniciar el broadcast y guardar la referencia
        broadcastThread = new BroadCastThread();
        broadcastThread.start();

        // Iniciamos también el ServerInputHandler, que podría incluir el comando para iniciar el juego.
        new ServerInputHandler().start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Aquí se aceptan conexiones de clientes mientras se espera el inicio del juego.
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo jugador conectado: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket, clients).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método de broadcast modificado para que reciba el remitente
    public static void broadcast(String message, PrintWriter sender) {
        synchronized(clients) {
            for (PrintWriter writer : clients) {
                if (writer != sender) {
                    writer.println(message);
                }
            }
        }
    }

    // Método para detener el broadcast (llámalo cuando inicie el juego)
    public static void stopBroadcast() {
        if (broadcastThread != null) {
            broadcastThread.stopBroadcast();
        }
    }
}
