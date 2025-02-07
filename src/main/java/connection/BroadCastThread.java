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
                // Espera 5 segundos entre broadcasts, pero se interrumpe si se detiene el broadcast.
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // Permite salir del ciclo si se interrumpe el sleep.
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para detener el broadcast
    public void stopBroadcast() {
        running = false;
        this.interrupt(); // Interrumpe el sleep si está en curso.
    }
}
