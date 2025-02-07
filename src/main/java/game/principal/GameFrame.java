package game.principal;

import game.utilities.GameKeyListener;
import game.utilities.GameLoop;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GameFrame extends JFrame {
    protected SnakeGame game;
    protected GamePanel panel;
    private GameLoop loop;
    private final long frequency = 75; // Frecuencia de actualización en milisegundos

    public GameFrame() {
        setTitle("Snake - Two Player");
        game = new SnakeGame();
        panel = new GamePanel(game);

        // Añade el key listener al panel en lugar del JFrame
        panel.addKeyListener(new GameKeyListener(game));

        add(panel);

        setSize(SnakeGame.BOARD_WIDTH, SnakeGame.BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setUndecorated(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
        setVisible(true);

        // Asegúrate de que el panel tenga el foco
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        loop = new GameLoop(game, panel, frequency);
        loop.start();
    }
    protected SnakeGame getInfo(){
        return this.game;
    }
}
