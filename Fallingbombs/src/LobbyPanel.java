import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LobbyPanel extends JPanel {

    JButton startButton;
    JLabel highScoreLabel;
    JLabel lastMatchRecap;
    String highScoreTxt = "High Score: ";
    int currentHighScore = 0;

    public LobbyPanel(int w,int h) {
        super();

        setSize(new Dimension(w,h));
        setBackground(new Color(51, 49, 49, 60));
        setOpaque(false);

        startButton = new JButton("Start Game");
        startButton.setSize(100,100);

        highScoreLabel = new JLabel(highScoreTxt + currentHighScore);
        highScoreLabel.setSize(100,50);
        highScoreLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));
        lastMatchRecap = new JLabel("Hit play!");
        lastMatchRecap.setSize(100,50);
        lastMatchRecap.setFont(new Font(Font.MONOSPACED,Font.BOLD,24));

        add(highScoreLabel);
        add(lastMatchRecap);

        add(startButton);
        startButton.setMaximumSize(new Dimension(100,50));




    }
}
