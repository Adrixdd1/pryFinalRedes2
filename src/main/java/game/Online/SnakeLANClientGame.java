package game.Online;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import game.principal.SnakeGame;
import game.principal.StartScreen;
import game.utilities.Online.ClientGameLoop;
import game.utilities.Online.GameClientKeyListener;
import game.utilities.Online.SnakeGameInfo;

public class SnakeLANClientGame extends JFrame {
    private ObjectInputStream in;
    private PrintWriter out;
    private Thread loop;
    private Socket socket;
    private DefaultListModel<String> jugadoresModel;
    private JList<String> jugadoresList;
    protected  StartScreen startScreen;
    private ClientGamePanel gamePanel;
    private SnakeGameInfo game;
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
public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }
    public StartScreen getStartScreen() {
        return startScreen;
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
                    if (msg.equals("SERVER_CLOSED")) {
                        SwingUtilities.invokeLater(() -> {
                            this.dispose();
                            startScreen.setVisible(true);
                        });
                        break;
                    } else if (msg.equals("START")) {
                        // Recibir información inicial del juego
                        SnakeGameInfo info = (SnakeGameInfo) in.readObject();
                        iniciarJuego(info);
                    } else {
                        actualizarListaJugadores(msg);
                    }
                } else if (mensaje instanceof SnakeGameInfo) {
                    // Si el juego ya inició, actualizar el panel
                    if (gamePanel != null) { 
                        gamePanel.setGameInfo((SnakeGameInfo) mensaje);
                        gamePanel.repaint();
                    } else {
                        // Iniciar juego si es el primer mensaje
                        iniciarJuego((SnakeGameInfo) mensaje);
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
            gamePanel = new ClientGamePanel(info);
            // Configuración de la ventana para el juego
            setTitle("Snake - Client");
            setSize(SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

            ClientGamePanel panel;
            panel = new ClientGamePanel(info);
            panel.addKeyListener(new GameClientKeyListener(out,this));

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
    public void setGameInfo(SnakeGameInfo gameInfo) {
        this.game = gameInfo;
    }
}
