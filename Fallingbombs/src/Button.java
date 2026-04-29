import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    public Button(String text) {
        super(text);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color base = new Color(40, 40, 80);
        Color hover = new Color(60, 60, 120);
        Color press = new Color(20, 20, 50);

        if (getModel().isPressed()) {
            g2d.setColor(press);
        } else if (getModel().isRollover()) {
            g2d.setColor(hover);
        } else {
            g2d.setColor(base);
        }
        // base shape
        g2d.fillRoundRect(0, 0, w, h, 20, 20);


        g2d.setColor(new Color(100, 150, 255, 120));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);

        // text content
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();

        int x = (w - textWidth) / 2;
        int y = (h + textHeight) / 2 - 3;

        // shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(getText(), x + 1, y + 1);

        // main text
        g2d.setColor(getForeground());
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }
}