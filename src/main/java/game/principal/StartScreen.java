package game.principal;

import connection.ServerThread;
import game.Online.SnakeLANClientGame;
import game.utilities.Online.Sala;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StartScreen extends JFrame {
    private Thread discovery;
    private ServerThread serverThread;
    private JTextField nombreField;
    private JButton btnCrearSala;
    private JButton btnUnirseSala;
    private JButton btnIniciarJuego;
    private DefaultListModel<Sala> salasModel;
    private JList<String> jugadoresList;
    private List<Sala> salasDisponibles;
    private List<String> jugadoresConectados;

    // Panel de sala que se muestra al crear o conectar a una sala
    private JPanel salaVisualPanel;

    public StartScreen() {
        setTitle("Snake Game - Pantalla de Inicio");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Snake Game", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(76, 175, 80));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nombrePanel.add(new JLabel("Nombre Sala:"));
        nombreField = new JTextField(15);
        nombrePanel.add(nombreField);
        panel.add(nombrePanel, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnCrearSala = new JButton("Crear Sala");
        btnCrearSala.setBackground(new Color(76, 175, 80));
        btnCrearSala.setForeground(Color.WHITE);
        btnCrearSala.addActionListener(e -> crearSala());
        botonesPanel.add(btnCrearSala);

        btnUnirseSala = new JButton("Unirse a Sala");
        btnUnirseSala.setBackground(new Color(76, 175, 80));
        btnUnirseSala.setForeground(Color.WHITE);
        btnUnirseSala.addActionListener(e -> mostrarSalasDisponibles());
        botonesPanel.add(btnUnirseSala);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        salasModel = new DefaultListModel<>();
        salasDisponibles = new ArrayList<>();
        jugadoresConectados = new ArrayList<>();
        // Creación del panel de sala, inicialmente oculto
        salaVisualPanel = new JPanel(new BorderLayout());
        salaVisualPanel.setBorder(BorderFactory.createTitledBorder("Jugadores Conectados"));
        salaVisualPanel.setVisible(false); // No se muestra hasta conectarse o crear una sala

        jugadoresList = new JList<>(new DefaultListModel<>());
        salaVisualPanel.add(new JScrollPane(jugadoresList), BorderLayout.CENTER);

        btnIniciarJuego = new JButton("Iniciar Juego");
        btnIniciarJuego.setBackground(new Color(255, 87, 34));
        btnIniciarJuego.setForeground(Color.WHITE);
        btnIniciarJuego.addActionListener(e -> iniciarJuego());
        salaVisualPanel.add(btnIniciarJuego, BorderLayout.SOUTH);

        panel.add(salaVisualPanel, BorderLayout.EAST);

        add(panel);
        setVisible(true);
    }

    private void crearSala() {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa el nombre de sala.");
            return;
        }
        try {
            serverThread = new ServerThread(12345, nombre);
            serverThread.start();
            JOptionPane.showMessageDialog(this, "Sala creada. Esperando jugadores...");

            // Deshabilitar controles de creación y conexión
            nombreField.setEnabled(false);
            btnCrearSala.setEnabled(false);
            btnUnirseSala.setEnabled(false);

            // Mostrar el panel de sala con los jugadores conectados e iniciar juego
            salaVisualPanel.setVisible(true);
            jugadoresList = new JList<>(serverThread.jugadoresModel);
            salaVisualPanel.add(new JScrollPane(jugadoresList));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al crear sala: " + e.getMessage());
        }
    }

    private void iniciarJuego() {
        JOptionPane.showMessageDialog(this, "Iniciando el juego...");
        this.serverThread.gameReady=true;
        this.dispose();
    }

    private void unirseASala(Sala sala) {
        try {
            JOptionPane.showMessageDialog(this, "Conectando a " + sala.getNombre() + " en " + sala.getDireccionIP().getHostAddress());
            Socket socket = new Socket(sala.getDireccionIP(), 12345);
            new SnakeLANClientGame(socket).setVisible(true);
            discovery.interrupt();
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + e.getMessage());
        }
    }

    private void mostrarSalasDisponibles() {
        discovery = new Thread(this::detectarSalas);
        discovery.start();
        JDialog dialog = new JDialog(this, "Salas Disponibles", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JList<Sala> listaSalas = new JList<>(salasModel);
        listaSalas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnUnirse = new JButton("Unirse");
        btnUnirse.addActionListener(e -> {
            int indice = listaSalas.getSelectedIndex();
            if (indice != -1) {
                Sala sala = salasDisponibles.get(indice);
                unirseASala(sala);
                dialog.dispose();
            }
        });

        dialog.add(new JScrollPane(listaSalas), BorderLayout.CENTER);
        dialog.add(btnUnirse, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void detectarSalas() {
        boolean buscar = true;
        try (DatagramSocket socket = new DatagramSocket(9800, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (buscar) {
                if(Thread.interrupted()){
                    buscar=false;
                }
                socket.receive(packet);
                String mensaje = new String(packet.getData(), 0, packet.getLength());
                //System.out.println("Mensaje recibido: " + mensaje);

                if (mensaje.startsWith("sala ")) {
                    String[] partes = mensaje.split(" ");
                    String nombreSala = partes[1];
                    String ip = partes[partes.length - 1];
                    InetAddress address = InetAddress.getByName(ip);

                    Sala sala = new Sala(nombreSala, address);
                    SwingUtilities.invokeLater(() -> {
                        if (!salasDisponibles.contains(sala)) {
                            salasDisponibles.add(sala);
                            salasModel.addElement(sala);
                            System.out.println("Sala agregada: " + sala);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}