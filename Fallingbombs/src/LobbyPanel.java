import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LobbyPanel extends JPanel {

    JLabel gameTitle;
    JButton startButton;
    JButton exitButton;
    JLabel highScoreLabel;
    String highScoreTxt = "High Score: ";
    int currentHighScore = 0;

    public LobbyPanel(int w,int h) {
        super();

        setLayout(new BorderLayout());
        setSize(new Dimension(w,h));
        setBackground(new Color(0,0,0));

        gameTitle = new JLabel("Falling Bombs");
        gameTitle.setFont(new Font(Font.MONOSPACED,Font.BOLD,45));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30,0,30,0));
        titlePanel.add(gameTitle);

        highScoreLabel = new JLabel(highScoreTxt + currentHighScore);
        highScoreLabel.setSize(100,50);
        highScoreLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(30,0,30,0));
        middlePanel.add(highScoreLabel);

        startButton = new JButton("Start Game");
        startButton.setSize(100,100);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Closing game...");
                System.exit(0);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,100,0));
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);





        add(titlePanel,BorderLayout.NORTH);
        add(middlePanel,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);





    }

    public void updateUI() {
        if (highScoreLabel != null) {
            highScoreLabel.setText(highScoreTxt + currentHighScore);
        }
    }
}
