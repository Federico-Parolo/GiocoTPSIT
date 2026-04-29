import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    Button backToLobbyButton;
    Button newGameButton;
    JLabel gameOverLabel;
    JLabel scoreLabel;

    public GameOverPanel(int w, int h) {
        setPreferredSize(new Dimension(w, h));
        setLayout(new BorderLayout());

        gameOverLabel = new JLabel("<html>Game<br>Over</html>", SwingConstants.CENTER);
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 80));

        scoreLabel = new JLabel("Score: EXAMPLE", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(gameOverLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(scoreLabel);
        centerPanel.add(Box.createVerticalGlue());

        centerPanel.setBorder(BorderFactory.createEmptyBorder(-80, 0, 0, 0));

        newGameButton = new Button("New Game");
        backToLobbyButton = new Button("Lobby");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        buttonPanel.add(newGameButton);
        buttonPanel.add(backToLobbyButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setScoreLabel(int score) {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // background
        g.setColor(new Color(15, 15, 30));
        g.fillRect(0, 0, w, h);

        // simple grid
        g.setColor(new Color(255, 255, 255, 15));

        for (int x = 0; x < w; x += 50)
            g.drawLine(x, 0, x, h);

        for (int y = 0; y < h; y += 50)
            g.drawLine(0, y, w, y);
    }
}