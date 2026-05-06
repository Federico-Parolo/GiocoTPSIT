import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameFrame extends JFrame {

    int w = 400;
    int h = 700;
    GamePanel gamePanel;
    LobbyPanel lobbyPanel;
    GameOverPanel gameOverPanel;
    PausePanel pausePanel;
    Leaderboard leaderboard;
    public final String GAME = "game";
    public final String LOBBY = "lobby";
    public final String END = "end";
    public final String RANKING = "ranking";
    JPanel container;
    CardLayout cardLayout;

    public GameFrame() {
        super("Falling Bombs");
        setSize(new Dimension(w,h));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(w,h));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                leaderboard.writeLeaderboard();
                System.exit(0);
            }
        });
        cardLayout = new CardLayout();
        container = new JPanel();
        container.setLayout(cardLayout);
        gamePanel = new GamePanel(w,h);
        pausePanel = gamePanel.pausePanel;
        lobbyPanel = new LobbyPanel(w,h);
        gameOverPanel = new GameOverPanel(w,h);
        leaderboard = new Leaderboard(w,h);
        container.add(lobbyPanel, LOBBY); // adding this first to display it on top
        container.add(gamePanel, GAME);
        container.add(gameOverPanel,END);
        container.add(leaderboard, RANKING);

        gamePanel.addPropertyChangeListener("gameRunning",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Game running: " + evt.getNewValue());
                if(!evt.getNewValue().equals(true)) {
                    gameOverPanel.setScoreLabel(gamePanel.currentPoints);
                    if (lobbyPanel.currentHighScore < gamePanel.currentPoints) lobbyPanel.currentHighScore = gamePanel.currentPoints;
                    int points = gamePanel.currentPoints;
                    leaderboard.updateLeaderboard(lobbyPanel.getPlayerName().isEmpty() ?
                            "Player" : lobbyPanel.getPlayerName(), "" + points);
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
                gamePanel.startNewAIGame(lobbyPanel.getDiff());
            }
        });
        lobbyPanel.exitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Closing game...");
                leaderboard.writeLeaderboard();
                System.exit(0);
            }
        });

        lobbyPanel.rankingButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,RANKING);
            }
        });

        leaderboard.backButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container,LOBBY);
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