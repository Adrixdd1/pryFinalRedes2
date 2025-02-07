package game.principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class StartScreen extends JFrame {
    private JTextField nombreField;
    private JButton btnCrearSala;
    private JButton btnUnirseSala;
    private DefaultListModel<String> salasModel;
    private List<InetAddress> salasDisponibles;

    public StartScreen() {
        setTitle("Snake Game - Pantalla de Inicio");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Snake Game", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(76, 175, 80)); // Verde
        panel.add(titulo, BorderLayout.NORTH);

        // Campo para ingresar el nombre
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(15);
        nombrePanel.add(nombreLabel);
        nombrePanel.add(nombreField);
        panel.add(nombrePanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel botonesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnCrearSala = new JButton("Crear Sala");
        btnCrearSala.setBackground(new Color(76, 175, 80)); // Verde
        btnCrearSala.setForeground(Color.WHITE);
        btnCrearSala.setFocusPainted(false);
        btnCrearSala.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearSala();
            }
        });
        botonesPanel.add(btnCrearSala);

        btnUnirseSala = new JButton("Unirse a Sala");
        btnUnirseSala.setBackground(new Color(76, 175, 80)); // Verde
        btnUnirseSala.setForeground(Color.WHITE);
        btnUnirseSala.setFocusPainted(false);
        btnUnirseSala.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSalasDisponibles();
            }
        });
        botonesPanel.add(btnUnirseSala);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        salasModel = new DefaultListModel<>();
        

        // Iniciar detección de salas
        salasDisponibles = new ArrayList<>();
        new Thread(this::detectarSalas).start();

        // Agregar panel al JFrame
        add(panel);
        setVisible(true);
    }

    private void crearSala() {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa tu nombre.");
            return;
        }

        // Lógica para crear sala
        JOptionPane.showMessageDialog(this, "Sala creada. Esperando jugadores...");
        dispose(); // Cierra la pantalla de inicio
        new GameFrame().setVisible(true); // Abre el juego
    }

    private void mostrarSalasDisponibles() {
        // Diálogo con lista actualizable
        JDialog dialog = new JDialog(this, "Salas Disponibles", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JList<String> listaSalas = new JList<>(salasModel);
        listaSalas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnUnirse = new JButton("Unirse");
        btnUnirse.addActionListener(e -> {
            int indice = listaSalas.getSelectedIndex();
            if (indice != -1) {
                InetAddress ip = salasDisponibles.get(indice);
                unirseASala(ip);
                dialog.dispose();
            }
        });

        dialog.add(new JScrollPane(listaSalas), BorderLayout.CENTER);
        dialog.add(btnUnirse, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    private void unirseASala(InetAddress ip) {
        // Lógica de conexión
        try {
            JOptionPane.showMessageDialog(this, "Conectando a " + ip.getHostAddress());
            dispose();
            new GameFrame().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + e.getMessage());
        }
    }

    private void detectarSalas() {
        try (DatagramSocket socket = new DatagramSocket(12346, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String mensaje = new String(packet.getData(), 0, packet.getLength());
                if (mensaje.startsWith("Snake Server disponible en IP: ")) {
                    String ip = mensaje.split(" ")[5];
                    InetAddress address = InetAddress.getByName(ip);

                    SwingUtilities.invokeLater(() -> {
                        if (!salasDisponibles.contains(address)) {
                            salasDisponibles.add(address);
                            salasModel.addElement("Sala en " + ip);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}