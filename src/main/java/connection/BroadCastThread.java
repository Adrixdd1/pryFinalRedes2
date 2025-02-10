package connection;

import java.io.IOException;
import java.net.*;

public class BroadCastThread extends Thread {
    private static final int BROADCAST_PORT = 9800; // Puerto de broadcast
    private volatile boolean running = true;
    private String nombreServidor;

    public BroadCastThread(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            String message = "sala " + nombreServidor + " disponible en IP: " + InetAddress.getLocalHost().getHostAddress();
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), BROADCAST_PORT);

            while (running) {
                socket.send(packet);
                System.out.println("Enviando broadcast: " + message); // Debug
                try {
                    Thread.sleep(5000); // Espera 5 segundos entre broadcasts
                } catch (InterruptedException e) {
                    break; // Salir si se interrumpe
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopBroadcast() {
        running = false;
        this.interrupt(); // Interrumpe el sleep si está en curso
    }
}