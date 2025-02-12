package game.Online;

import game.principal.SnakeGame;
import game.utilities.Online.ClientGameLoop;
import game.utilities.Online.GameClientKeyListener;
import game.utilities.Online.SnakeGameInfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class SnakeLANClientGame extends JFrame {
    private ObjectInputStream in;
    private PrintWriter out;
    private Thread loop;
    private Socket socket;
    private DefaultListModel<String> jugadoresModel;
    private JList<String> jugadoresList;

    public SnakeLANClientGame(Socket socket) {
        this.socket = socket;
        try {
            // Crea el stream una sola vez
            this.in = new ObjectInputStream(socket.getInputStream());
            // Inicializa el PrintWriter para enviar datos, si lo necesitas
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        }catch (Exception e){
            e.printStackTrace();
        }
        initWaitingScreen();
        new Thread(this::escucharServidor).start(); // Iniciar escucha de mensajes del servidor
    }

    // Inicializa la pantalla de espera con la lista de jugadores
    private void initWaitingScreen() {
        setTitle("Esperando jugadores...");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        jugadoresModel = new DefaultListModel<>();
        jugadoresList = new JList<>(jugadoresModel);
        add(new JScrollPane(jugadoresList));

        setVisible(true);
    }

    private void escucharServidor() {
        try {
            while (true) {
                Object mensaje = in.readObject();
                if (mensaje instanceof String) {
                    String msg = (String) mensaje;

                    if (!msg.equals("START")) {
                        actualizarListaJugadores(msg);
                    }else{
                        // Al recibir "START", leer el siguiente objeto que se espera sea SnakeGameInfo
                        SnakeGameInfo info = (SnakeGameInfo) in.readObject();
                        iniciarJuego(info);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Actualiza la lista de jugadores en la pantalla de espera
    private void actualizarListaJugadores(String msg) {
        SwingUtilities.invokeLater(() -> {
            jugadoresModel.clear();
            String[] partes = msg.split(",");
            for (String jugador : partes) {
                if (!jugador.trim().isEmpty()) {
                    jugadoresModel.addElement(jugador.trim());
                }
            }
        });
    }

    // Inicia el juego reemplazando la pantalla de espera por el panel del juego
    private void iniciarJuego(SnakeGameInfo info) {
        SwingUtilities.invokeLater(() -> {
            // Elimina la UI de espera
            getContentPane().removeAll();

            // Configuración de la ventana para el juego
            setTitle("Snake - Client");
            setSize(SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

            ClientGamePanel panel;
            panel = new ClientGamePanel(info);
            panel.addKeyListener(new GameClientKeyListener(out));

            add(panel);
            revalidate();
            repaint();

            panel.setFocusable(true);
            panel.requestFocusInWindow();

            // Inicia el bucle del juego en un hilo separado
            new Thread(() -> {
                loop = new ClientGameLoop(panel, in);
                loop.start();
            }).start();
        });
    }
}
