import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class LobbyPanel extends JPanel {

    JLabel gameTitle;
    Button startButton;
    Button exitButton;
    Button autoPlayButton;
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
        gameTitle.setForeground(Color.WHITE);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30,0,30,0));
        titlePanel.add(gameTitle);

        highScoreLabel = new JLabel(highScoreTxt + currentHighScore);
        highScoreLabel.setSize(100,50);
        highScoreLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,32));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Button easyDiffButton = new Button("Easy");
        easyDiffButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDifficulty(EASY);
            }
        });
        Button medDiffButton = new Button("Medium");
        medDiffButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDifficulty(MED);
            }
        });
        Button hardDiffButton = new Button("Hard");
        hardDiffButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDifficulty(HARD);
            }
        });
        currentDiffLabel = new JLabel();
        currentDiffLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        currentDiffLabel.setForeground(Color.WHITE);
        currentDiffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diffPanel.setOpaque(false);
        diffPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffPanel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        diffPanel.add(easyDiffButton);
        diffPanel.add(medDiffButton);
        diffPanel.add(hardDiffButton);
        diffPanel.setMaximumSize(diffPanel.getPreferredSize());

        difficultyDetails = new JLabel();
        difficultyDetails.setForeground(Color.WHITE);
        difficultyDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyDetails.setHorizontalAlignment(SwingConstants.CENTER);
        difficultyDetails.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));

        powerUpEn = new JCheckBox("Enable Power-ups");
        powerUpEn.setFont(new Font(Font.MONOSPACED,Font.BOLD,18));
        powerUpEn.setForeground(Color.WHITE);
        powerUpEn.setOpaque(false);
        powerUpEn.setFocusPainted(false);
        powerUpEn.setAlignmentX(Component.CENTER_ALIGNMENT);
        powerUpEn.setSelected(false);
        // set custom icons
        powerUpEn.setIcon(createIcon(false));
        powerUpEn.setSelectedIcon(createIcon(true));

        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
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

        startButton = new Button("Start Game");
        startButton.setSize(100,100);
        exitButton = new Button("Exit");
        exitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Closing game...");
                System.exit(0);
            }
        });
        autoPlayButton = new Button("AI Mode");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,10,0));
        buttonPanel.add(startButton);
        buttonPanel.add(autoPlayButton);
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
                difficultyDetails.setText(
                        "<html><div style='text-align:center;'>"
                                + "Beginner:<br>Bombs: 1 sec<br>PowerUp: 5 sec"
                                + "</div></html>"
                );
                break;

            case MED:
                difficultyDetails.setText(
                        "<html><div style='text-align:center;'>"
                                + "Intermediate:<br>Bombs: 0.9 sec<br>PowerUp: 7 sec"
                                + "</div></html>"
                );
                break;

            case HARD:
                difficultyDetails.setText(
                        "<html><div style='text-align:center;'>"
                                + "Advanced:<br>Bombs: 0.7 sec<br>PowerUp: 9 sec"
                                + "</div></html>"
                );
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(
                0, 0, new Color(10, 10, 20),
                0, h, new Color(20, 20, 40)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);


        g2d.setColor(new Color(255, 255, 255, 20));
        int gridSize = 40;

        for (int x = 0; x < w; x += gridSize) {
            g2d.drawLine(x, 0, x, h);
        }
        for (int y = 0; y < h; y += gridSize) {
            g2d.drawLine(0, y, w, y);
        }

        g2d.setColor(new Color(255, 255, 255, 40));
        for (int i = 0; i < 40; i++) {
            int px = (int)(Math.random() * w);
            int py = (int)(Math.random() * h);
            g2d.fillOval(px, py, 2, 2);
        }
    }

    private Icon createIcon(boolean selected) {
        return new Icon() {
            int size = 16;

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();

                // background
                g2d.setColor(new Color(40, 40, 80));
                g2d.fillRoundRect(x, y, size, size, 6, 6);

                // border
                g2d.setColor(new Color(100, 150, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, y, size, size, 6, 6);

                // check mark
                if (selected) {
                    g2d.setColor(new Color(120, 200, 255));
                    g2d.setStroke(new BasicStroke(2));

                    g2d.drawLine(x + 4, y + 8, x + 7, y + 12);
                    g2d.drawLine(x + 7, y + 12, x + 12, y + 4);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
}
