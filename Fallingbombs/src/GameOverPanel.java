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

        gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,50));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(gameOverLabel);

        scoreLabel = new JLabel("Score: EXAMPLE");
        scoreLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        middlePanel.add(scoreLabel);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        backToLobbyButton = new JButton("Lobby");
        newGameButton = new JButton("New Game");
        buttonPanel.add(newGameButton);
        buttonPanel.add(backToLobbyButton);

        add(titlePanel,BorderLayout.NORTH);
        add(middlePanel,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);
    }


    public void setScoreLabel(int score) {
        this.scoreLabel.setText("Score: " + score);
    }

}
