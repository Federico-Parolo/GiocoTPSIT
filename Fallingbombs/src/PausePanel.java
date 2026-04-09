import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {

    JButton resumeButton;
    JButton lobbyButton;
    JButton newGameButton;

    public PausePanel() {
        super();
        setBackground(new Color(0,0,0,0));
        setLayout(new BorderLayout());
        resumeButton = new JButton("Resume Game");
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        lobbyButton = new JButton("Lobby");
        lobbyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        panel.add(resumeButton);
        panel.add(lobbyButton);
        panel.add(newGameButton);
        add(panel,BorderLayout.CENTER);
        panel.setOpaque(false);
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
