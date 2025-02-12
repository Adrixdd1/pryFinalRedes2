package connection;

import game.Online.SnakeLANClientGame;

import java.io.*;
import java.net.*;

public class Client {
    private static final int BROADCAST_PORT = 9800; // Puerto de broadcast
    private static final int PORT = 12345; // Puerto del servidor
    private static String serverIp = "localhost";

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

            // Espera un mensaje de broadcast
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Mensaje recibido: " + msg); // Debug

            // Procesar el mensaje
            if (msg.startsWith("sala ")) {
                String[] parts = msg.split(" ");
                serverIp = parts[parts.length - 1]; // La IP es el último elemento
                System.out.println("Servidor detectado en: " + serverIp);
            }
        } catch (IOException e) {
            System.out.println("No se encontró ningún servidor, usando localhost");
            serverIp = "localhost";
        }
    }
}