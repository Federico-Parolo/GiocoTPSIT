import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class LobbyPanel extends JPanel {

    JLabel gameTitle;
    JButton startButton;
    JButton exitButton;
    JLabel highScoreLabel;
    String highScoreTxt = "High Score: ";
    JLabel currentDiffLabel;
    String diffTxt = "Selected: ";
    JLabel difficultyDetails;
    JCheckBox powerUpEn;
    int currentHighScore = 0;
    private int diff;
    private final int EASY = 1;
    private final int MED = 2;
    private final int HARD = 4;

    private final HashMap<Integer,String> diffs;

    public LobbyPanel(int w,int h) {
        super();

        setLayout(new BorderLayout());
        setSize(new Dimension(w,h));
        setBackground(new Color(0,0,0));

        diffs = new HashMap<>();
        initDiffs();

        gameTitle = new JLabel("Falling Bombs");
        gameTitle.setFont(new Font(Font.MONOSPACED,Font.BOLD,45));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30,0,30,0));
        titlePanel.add(gameTitle);

        highScoreLabel = new JLabel(highScoreTxt + currentHighScore);
        highScoreLabel.setSize(100,50);
        highScoreLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton easyDiffButton = new JButton("Easy");
        easyDiffButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDifficulty(EASY);
            }
        });
        JButton medDiffButton = new JButton("Medium");
        medDiffButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDifficulty(MED);
            }
        });
        JButton hardDiffButton = new JButton("Hard");
        hardDiffButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDifficulty(HARD);
            }
        });
        currentDiffLabel = new JLabel();
        currentDiffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diffPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffPanel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        diffPanel.add(easyDiffButton);
        diffPanel.add(medDiffButton);
        diffPanel.add(hardDiffButton);
        diffPanel.setMaximumSize(diffPanel.getPreferredSize());

        difficultyDetails = new JLabel();
        difficultyDetails.setAlignmentX(Component.CENTER_ALIGNMENT);

        powerUpEn = new JCheckBox("Enable Power-ups");
        powerUpEn.setAlignmentX(Component.CENTER_ALIGNMENT);
        powerUpEn.setSelected(false);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel,BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(30,0,30,0));
        middlePanel.add(highScoreLabel);
        middlePanel.add(diffPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(0,30)));
        middlePanel.add(currentDiffLabel);
        middlePanel.add(Box.createRigidArea(new Dimension(0,30)));
        middlePanel.add(difficultyDetails);
        middlePanel.add(Box.createRigidArea(new Dimension(0,30)));
        middlePanel.add(powerUpEn);

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

        selectDifficulty(EASY);
    }

    public void selectDifficulty(int d) {
        diff = d;
        currentDiffLabel.setText(diffTxt + diffs.get(diff));
        switch (d) {
            case EASY:
                difficultyDetails.setText("easy");
                break;
            case MED:
                difficultyDetails.setText("med");
                break;
            case HARD:
                difficultyDetails.setText("hard");
                break;
            default:
                difficultyDetails.setText("problem");
        }
    }

    public void updateUI() {
        if (highScoreLabel != null) {
            highScoreLabel.setText(highScoreTxt + currentHighScore);
        }
    }

    public void initDiffs() {
        diffs.put(1,"Easy");
        diffs.put(2,"Medium");
        diffs.put(4,"Hard");
    }

    public int getDiff() {
        return diff;
    }

    public boolean getPowerUpEn() {
        return powerUpEn.isSelected();
    }
}
