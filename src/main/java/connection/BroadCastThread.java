package connection;

import java.io.IOException;
import java.net.*;

public class BroadCastThread extends Thread {
    private static final int BROADCAST_PORT = 12346;
    private volatile boolean running = true;

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            String message = "Snake Server disponible en IP: " + InetAddress.getLocalHost().getHostAddress();
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), BROADCAST_PORT);

            while (running) {
                socket.send(packet);
                Thread.sleep(5000); // Enviar cada 5 segundos
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopBroadcast() {
        running = false;
        this.interrupt();
    }
}
