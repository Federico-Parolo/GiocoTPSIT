import java.awt.*;

public class Explosion implements Drawable{
    int currentX,currentY;
    volatile private int lifespan = 20; // intended in frames (how many refreshes survives)
    EffectType type;
    int diameter = 10;

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
        g2d.setColor(new Color(0,0,0,200));
        if (type == EffectType.Collision) {
            g2d.setColor(new Color(250,150,10, 255-diameter*4));
            g2d.fillOval(currentX - diameter/2,currentY - diameter/2,diameter,diameter);
        } else if (type == EffectType.PowerUp) {
            g2d.setColor(new Color(10,150,250, 255-diameter*4));
            g2d.fillRoundRect(currentX - diameter/2,currentY - diameter/2,diameter,diameter, 5,5);
        } else if (type == EffectType.Endgame) {
            g2d.setColor(new Color(250,150,10, 255-diameter*4));
            g2d.fillOval(currentX,currentY- diameter,15,diameter);
        } else if (type == EffectType.ImmortalActive) {
            g2d.drawString("IMMORTAL",currentX-1,currentY-1);
            g2d.setColor(new Color(255,200,50));
            g2d.drawString("IMMORTAL",currentX,currentY);
        } else if (type == EffectType.SlowMoActive) {
            g2d.drawString("SLOWMO",currentX-1,currentY-1);
            g2d.setColor(new Color(150,250,250));
            g2d.drawString("SLOWMO",currentX,currentY);
        } else if (type == EffectType.FireRateActive) {
            g2d.drawString("FIRERATE",currentX-1,currentY-1);
            g2d.setColor(new Color(255,100,50));
            g2d.drawString("FIRERATE",currentX,currentY);
        } else if (type == EffectType.SpeedActive) {
            g2d.drawString("SPEED",currentX-1,currentY-1);
            g2d.setColor(new Color(0,255,42));
            g2d.drawString("SPEED",currentX,currentY);
        } else if (type == EffectType.TriShotActive) {
            g2d.drawString("TRISHOT",currentX-1,currentY-1);
            g2d.setColor(new Color(0,0,255));
            g2d.drawString("TRISHOT",currentX,currentY);
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
}
