import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LobbyPanel extends JPanel {

    JLabel gameTitle;
    JButton startButton;
    JButton exitButton;
    JLabel highScoreLabel;
    String highScoreTxt = "High Score: ";
    JLabel currentDiffLabel;
    String diffTxt = "Selected: ";
    int currentHighScore = 0;
    int diff = 1;

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
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton diffEasyButton = new JButton("Easy");
        diffEasyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 1;
                currentDiffLabel.setText(diffTxt + "Easy");
            }
        });
        JButton diffMedButton = new JButton("Medium");
        diffMedButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 2;
                currentDiffLabel.setText(diffTxt + "Medium");
            }
        });
        JButton diffHardButton = new JButton("Hard");
        diffHardButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = 4;
                currentDiffLabel.setText(diffTxt + "Hard");
            }
        });
        currentDiffLabel = new JLabel(diffTxt + "Easy");
        currentDiffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diffPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffPanel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        diffPanel.add(diffEasyButton);
        diffPanel.add(diffMedButton);
        diffPanel.add(diffHardButton);
        diffPanel.setMaximumSize(diffPanel.getPreferredSize());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel,BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(30,0,30,0));
        middlePanel.add(highScoreLabel);
        middlePanel.add(diffPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(30,30)));
        middlePanel.add(currentDiffLabel);

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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
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
