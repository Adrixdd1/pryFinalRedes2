package connection;

import connection.snakeC.SnakeLANClientGame;

import java.io.*;
import java.net.*;

public class Client {
    private static final int BROADCAST_PORT = 12346;
    private static final int PORT = 12345;
    private static String serverIp = "localhost"; // Se actualizará al detectar el servidor.

    public static void main(String[] args) {
        discoverServer();
        try {
            Socket serverSocket = new Socket(serverIp, PORT);
            new SnakeLANClientGame(serverSocket);
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
            String msg = new String(packet.getData(), 0, packet.getLength());
            String[] parts = msg.split(" ");
            serverIp = parts[parts.length - 1];
            System.out.println("Servidor detectado en: " + serverIp);
        } catch (IOException e) {
            System.out.println("No se encontró ningún servidor, usando localhost");
            serverIp = "localhost";
        }
    }
}
