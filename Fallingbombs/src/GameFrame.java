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
        lobbyPanel.autoPlayButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,GAME);
                gamePanel.startNewAIGame();
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

        add(container);
    }


    public void startGame() {
        this.setVisible(true);
    }

}