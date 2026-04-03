import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.Key;

public class GameFrame extends JFrame {

    int w = 400;
    int h = 700;
    GamePanel gamePanel;
    LobbyPanel lobbyPanel;
    GameOverPanel gameOverPanel;
    public final String GAME = "game";
    public final String LOBBY = "lobby";
    public final String END = "end";
    JPanel container;
    CardLayout cardLayout;

    public GameFrame() {
        super("Falling Bombs");
        setSize(new Dimension(w,h));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        container = new JPanel();
        container.setLayout(cardLayout);
        gamePanel = new GamePanel(w,h);
        lobbyPanel = new LobbyPanel(w,h);
        gameOverPanel = new GameOverPanel(w,h);
        container.add(lobbyPanel, LOBBY); // adding this first to display it on top
        container.add(gamePanel, GAME);
        container.add(gameOverPanel,END);
        // cardLayout.show(container,LOBBY);

        gamePanel.addPropertyChangeListener("gameRunning",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Game running: " + evt.getNewValue());
                if(!evt.getNewValue().equals(true)) {
                    cardLayout.show(container, END);
                }

            }
        });

        lobbyPanel.startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,GAME);
                gamePanel.startNewGame();

            }
        });

        gameOverPanel.backToLobbyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,LOBBY);
            }
        });
        gameOverPanel.newGameButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,GAME);
                gamePanel.startNewGame();
            }
        });

        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("LEFT"),"Left");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"),"Right");
        inputMap.put(KeyStroke.getKeyStroke("SPACE"),"Fire");
        actionMap.put("Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getGameRunning()) {
                    gamePanel.c.currentX -= 5;
                    System.out.println("left");
                }
            }
        });
        actionMap.put("Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getGameRunning()) {
                    gamePanel.c.currentX += 5;
                    System.out.println("right");
                }
            }
        });
        actionMap.put("Fire", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getGameRunning()) {
                    Projectile p = gamePanel.c.fire();
                    gamePanel.spawnProjectile(p);
                }
            }
        });
        add(container);
    }


    public void startGame() {
        this.setVisible(true);
    }
}