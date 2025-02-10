package game.utilities;

import game.principal.ClientGamePanel;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientGameLoop extends Thread {
    private ObjectInputStream datosServer;
    private ClientGamePanel clientGamePanel;
    private boolean running = true;

    public ClientGameLoop(ClientGamePanel clientGamePanel, ObjectInputStream datosServer) {
        this.clientGamePanel = clientGamePanel;
        this.datosServer = datosServer;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Recibe el estado del juego
                SnakeGameInfo game = (SnakeGameInfo) datosServer.readObject();
                clientGamePanel.setGameInfo(game);
                clientGamePanel.repaint();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                running = false;  // Termina el bucle en caso de error
            }
        }
        // Cerrar conexión cuando termine
        try {
            datosServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}