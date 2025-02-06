package connection;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final int BROADCAST_PORT = 12346;
    private static final int PORT = 12345;
    private static String serverIp = "localhost"; // Se actualizará al detectar el servidor

    public static void main(String[] args) {
        discoverServer();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Servidor detectado en " + serverIp + ". ¿Deseas conectarte? (sí/no): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        if (!respuesta.equals("sí")) {
            System.out.println("Conexión cancelada.");
            return;
        }

        try (Socket socket = new Socket(serverIp, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Conectado al servidor en " + serverIp + ". Escribe un mensaje:");
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String userMessage = scanner.nextLine();
                out.println(userMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void discoverServer() {
        try (DatagramSocket socket = new DatagramSocket(BROADCAST_PORT, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Buscando servidor...");
            socket.receive(packet);
            serverIp = new String(packet.getData(), 0, packet.getLength()).split(" ")[5];
            System.out.println("Servidor detectado en: " + serverIp);
        } catch (IOException e) {
            System.out.println("No se encontró ningún servidor, usando localhost");
        }
    }
}
