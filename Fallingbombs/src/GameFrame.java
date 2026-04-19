import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameFrame extends JFrame {

    int w = 400;
    int h = 700;
    GamePanel gamePanel;
    LobbyPanel lobbyPanel;
    GameOverPanel gameOverPanel;
    PausePanel pausePanel;
    public final String GAME = "game";
    public final String LOBBY = "lobby";
    public final String END = "end";
    JPanel container;
    CardLayout cardLayout;
    long lastFireTime;
    public final long FIRE_DELAY = 300;

    public GameFrame() {
        super("Falling Bombs");
        setSize(new Dimension(w,h));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        container = new JPanel();
        container.setLayout(cardLayout);
        gamePanel = new GamePanel(w,h);
        pausePanel = gamePanel.pausePanel;
        lobbyPanel = new LobbyPanel(w,h);
        gameOverPanel = new GameOverPanel(w,h);
        container.add(lobbyPanel, LOBBY); // adding this first to display it on top
        container.add(gamePanel, GAME);
        container.add(gameOverPanel,END);

        gamePanel.addPropertyChangeListener("gameRunning",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Game running: " + evt.getNewValue());
                if(!evt.getNewValue().equals(true)) {
                    gameOverPanel.setScoreLabel(gamePanel.currentPoints);
                    if (lobbyPanel.currentHighScore < gamePanel.currentPoints) lobbyPanel.currentHighScore = gamePanel.currentPoints;
                    lobbyPanel.updateUI();
                    cardLayout.show(container, END);
                }

            }
        });

        lobbyPanel.startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,GAME);
                gamePanel.startNewGame(lobbyPanel.getDiff(), lobbyPanel.getPowerUpEn());

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
                gamePanel.startNewGame(lobbyPanel.getDiff(),lobbyPanel.getPowerUpEn());
            }
        });

        pausePanel.lobbyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,LOBBY);
                gamePanel.resetGame();
            }
        });
        pausePanel.newGameButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.resetGame();
                gamePanel.startNewGame(lobbyPanel.getDiff(), lobbyPanel.getPowerUpEn());
            }
        });

        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0),"Left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0),"Right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0),"Fire");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),"Pause");
        actionMap.put("Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getGameRunning()) {
                    gamePanel.c.currentX -= (gamePanel.c.currentX <= 0) ? 0 : getWidth()/gamePanel.c.MOVEMENT;
                    System.out.println("left");
                }
            }
        });
        actionMap.put("Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getGameRunning()) {
                    gamePanel.c.currentX += (gamePanel.c.currentX + gamePanel.c.WIDTH >= getWidth()) ? 0 : getWidth()/gamePanel.c.MOVEMENT;
                    System.out.println("right");
                }
            }
        });
        actionMap.put("Fire", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.currentTimeMillis();
                if (gamePanel.getGameRunning() && (now - lastFireTime >= FIRE_DELAY)) {
                    lastFireTime = now;

                    Projectile p = gamePanel.c.fire();
                    gamePanel.spawnProjectile(p);

                }
            }
        });
        actionMap.put("Pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pause click");
                if (gamePanel.isPaused()) {
                    gamePanel.resumeGame();
                } else {
                    gamePanel.pauseGame();
                }
            }
        });
        add(container);
    }


    public void startGame() {
        this.setVisible(true);
    }

}