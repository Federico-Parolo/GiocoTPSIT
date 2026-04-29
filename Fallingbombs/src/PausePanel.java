import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {

    Button resumeButton;
    Button lobbyButton;
    Button newGameButton;

    public PausePanel() {
        super();
        setBackground(new Color(0,0,0,0));
        setLayout(new BorderLayout());
        resumeButton = new Button("Resume Game");
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        lobbyButton = new Button("Lobby");
        lobbyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton = new Button("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel,BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(250,30,30,30));

        middlePanel.add(resumeButton);
        middlePanel.add(Box.createRigidArea(new Dimension(0,20)));
        middlePanel.add(lobbyButton);
        middlePanel.add(Box.createRigidArea(new Dimension(0,20)));
        middlePanel.add(newGameButton);
        add(middlePanel,BorderLayout.CENTER);

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel pausedLabel = new JLabel("Paused");
        northPanel.add(Box.createRigidArea(new Dimension(0,30)));
        northPanel.add(pausedLabel);

        add(northPanel,BorderLayout.NORTH);
        middlePanel.setOpaque(false);
        northPanel.setOpaque(false);
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(100,100,100,100));
        g2d.fillRect(0,0,getWidth(),getHeight());
    }
}
