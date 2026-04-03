import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LobbyPanel extends JPanel {

    JLabel gameTitle;
    JButton startButton;
    JLabel highScoreLabel;
    // JLabel lastMatchRecap;
    String highScoreTxt = "High Score: ";
    int currentHighScore = 0;

    public LobbyPanel(int w,int h) {
        super();

        setLayout(new BorderLayout());
        setSize(new Dimension(w,h));
        setBackground(new Color(0,0,0));

        gameTitle = new JLabel("Falling Bombs");
        gameTitle.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));

        startButton = new JButton("Start Game");
        startButton.setSize(100,100);

        highScoreLabel = new JLabel(highScoreTxt + currentHighScore);
        highScoreLabel.setSize(100,50);
        highScoreLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));
        /*
        lastMatchRecap = new JLabel("Hit play!");
        lastMatchRecap.setSize(100,50);
        lastMatchRecap.setFont(new Font(Font.MONOSPACED,Font.BOLD,24));
        */

        add(gameTitle,BorderLayout.NORTH);
        add(highScoreLabel,BorderLayout.CENTER);
        // add(lastMatchRecap);
        add(startButton,BorderLayout.SOUTH);




    }
}
