import javax.swing.*;
import java.awt.*;

public class Explosion implements Drawable{
    int currentX,currentY;
    volatile private int lifespan = 20; // intended in frames (how many refreshes survives)
    EffectType type;
    int diameter = 10;
    static JPanel gamePanel;

    public enum EffectType {
        Collision,
        Endgame,
        PowerUp,
        ImmortalActive,
        SlowMoActive,
        FireRateActive,
        SpeedActive,
        TriShotActive
    }

    public Explosion(EffectType type, int currentX, int currentY) {
        this.type = type;
        this.currentX = currentX + diameter/2;
        this.currentY = currentY + diameter/2;
    }

    public void drawSprite(Graphics2D g2d) {

        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));

        if (type == EffectType.Collision) {
            g2d.setColor(new Color(250,150,10, 255 - diameter * 4));
            g2d.fillOval(currentX - diameter/2, currentY - diameter/2, diameter, diameter);

        } else if (type == EffectType.PowerUp) {
            g2d.setColor(new Color(10,150,250, 255 - diameter * 4));
            g2d.fillRoundRect(currentX - diameter/2, currentY - diameter/2, diameter, diameter, 5, 5);

        } else if (type == EffectType.Endgame) {
            g2d.setColor(new Color(250,150,10, 255 - diameter * 4));
            g2d.fillOval(currentX, currentY - diameter, 15, diameter);

        } else if (type == EffectType.ImmortalActive) {
            Color c = new Color(255,200,50);
            String txt = "IMMORTAL";
            drawCenteredText(g2d,txt,c);


        } else if (type == EffectType.SlowMoActive) {
            Color c = new Color(150,250,250);
            String txt = "SLOWMO";
            drawCenteredText(g2d,txt,c);


        } else if (type == EffectType.FireRateActive) {
            Color c = new Color(255,100,50);
            String txt = "FIRERATE";
            drawCenteredText(g2d,txt,c);

        } else if (type == EffectType.SpeedActive) {
            Color c = new Color(0,255,42);
            String txt = "SPEED";
            drawCenteredText(g2d,txt,c);


        } else if (type == EffectType.TriShotActive) {
            Color c = new Color(0,0,255);
            String txt = "TRISHOT";
            drawCenteredText(g2d,txt,c);


        }
        else {
            g2d.setColor(new Color(255,0,0));
            g2d.fillOval(currentX,currentY,diameter,diameter);
        }
    }

    synchronized public void updateExplosion(int delta) {
        lifespan += delta;
        diameter++;
    }

    synchronized public int getLifespan() {
        return lifespan;
    }

    private void drawCenteredText(Graphics2D g2d,String s,Color c) {
        int off = g2d.getFontMetrics().stringWidth(s);
        int cx = gamePanel.getWidth() / 2 - off / 2;
        int cy = gamePanel.getHeight() / 2;
        g2d.setColor(new Color(0,0,0, 255 - diameter * 8));
        // drawing text outline
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                g2d.drawString(s, cx + dx, cy + dy);
            }
        }
        g2d.setColor(c);
        g2d.drawString(s, cx, cy);
    }

}

