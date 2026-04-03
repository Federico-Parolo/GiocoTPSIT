import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    JButton backToLobbyButton;
    JButton newGameButton;
    JLabel gameOverLabel;
    JLabel scoreLabel;
    JPanel buttonPanel;

    public GameOverPanel(int w,int h) {
        super();
        setSize(w,h);
        setLayout(new BorderLayout());
        backToLobbyButton = new JButton("Lobby");
        newGameButton = new JButton("New Game");
        gameOverLabel = new JLabel("Game Over");
        scoreLabel = new JLabel("Score: EXAMPLE");
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(newGameButton);
        buttonPanel.add(backToLobbyButton);

        add(buttonPanel,BorderLayout.SOUTH);
        add(gameOverLabel,BorderLayout.NORTH);
        add(scoreLabel,BorderLayout.CENTER);
    }


    public void setScoreLabel(int score) {
        this.scoreLabel.setText("Score: " + score);
    }

}
