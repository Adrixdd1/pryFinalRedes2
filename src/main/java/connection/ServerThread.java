package connection;

import game.Online.SnakeLANGame;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
public class ServerThread extends Thread {
    private final int PORT;
    private final String nombre;
    private final List<DatosCliente> clients;
    public boolean gameReady;
    private ServerSocket serverSocket;
    public DefaultListModel<String> jugadoresModel = new DefaultListModel<>();


    public ServerThread(int port, String nombre) throws IOException {
        this.PORT = port;
        this.nombre = nombre;
        this.clients = new ArrayList<>();
        this.gameReady = false;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            // Configuramos un timeout para que accept() no se bloquee indefinidamente
            serverSocket.setSoTimeout(1000); // 1 segundo de timeout
            System.out.println("Servidor iniciado en el puerto " + PORT);

            BroadCastThread bt = new BroadCastThread(nombre);
            bt.start();

            while (!gameReady) {
                enviarListaJugadores();
                try {
                    Socket client = serverSocket.accept();
                    synchronized (clients) {
                        clients.add(new DatosCliente(client));
                        System.out.println("Jugador conectado: " + client.getInetAddress());
                    }
                    /* También se puede establecer gameReady si se alcanzan 3 clientes
                    if (clients.size() >= 3) {
                        gameReady = true;
                    }*/
                } catch (SocketTimeoutException e) {
                    // Si se agota el timeout, simplemente se reitera el bucle
                    // Esto permite que se compruebe el valor de gameReady periódicamente.
                }

            }
            for (DatosCliente client : clients) {
                client.out.writeObject("START");
                client.out.flush();
            }
            bt.stopBroadcast();
            iniciarJuego();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void enviarListaJugadores() {
        int numeroJ = 2;
        StringBuilder lista = new StringBuilder();
        lista.append("Servidor").append(",");
        for (DatosCliente client : clients) {
            lista.append("jugador "+numeroJ++).append(",");
        }
        for (DatosCliente client : clients) {
            try {
                client.out.writeObject(lista.toString());
                client.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(() -> {
            jugadoresModel.clear();
            jugadoresModel.addElement("Servidor (tú)");
            int numero=2;
            for (DatosCliente jugador : clients) {
                jugadoresModel.addElement("jugador "+numero++);
            }
        });
    }

    private void iniciarJuego() {
        System.out.println("Iniciando juego con " + clients.size() + " jugadores.");
        SnakeLANGame game = new SnakeLANGame();
        int playerId = 2; // IDs empiezan en 2
        for (DatosCliente client : clients) {
            System.out.println("agregado cliente "+playerId);
            game.addClient(playerId++,client.out,client.in);
        }
        game.startGame();
    }
}
