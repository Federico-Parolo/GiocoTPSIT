import java.awt.*;

public class Explosion implements Drawable{
    int currentX,currentY;
    volatile private int lifespan = 20; // intended in frames (how many refreshes survives)
    int type;
    int diameter = 10;

    public static final int BOMB_COLLISION = 0;
    public static final int ENDGAME = 1;
    public static final int POWER_UP = 2;
    public static final int IMMORTAL_ACTIVE = 3;
    public static final int SLOW_MO_ACTIVE = 4;
    public static final int FIRE_RATE_ACTIVE = 5;
    public static final int SPEED_ACTIVE = 6;
    public static final int TRISHOT_ACTIVE = 7;

    public Explosion(int type, int currentX, int currentY) {
        this.type = type;
        this.currentX = currentX + diameter/2;
        this.currentY = currentY + diameter/2;
    }

    public void drawSprite(Graphics2D g2d) {

        if (type == BOMB_COLLISION) {
            g2d.setColor(new Color(250,150,10, 255-diameter*4));
            g2d.fillOval(currentX - diameter/2,currentY - diameter/2,diameter,diameter);
        } else if (type == POWER_UP) {
            g2d.setColor(new Color(10,150,250, 255-diameter*4));
            g2d.fillRoundRect(currentX - diameter/2,currentY - diameter/2,diameter,diameter, 5,5);
        } else if (type == ENDGAME) {
            g2d.setColor(new Color(250,150,10, 255-diameter*4));
            g2d.fillOval(currentX,currentY- diameter,15,diameter);
        } else if (type == IMMORTAL_ACTIVE) {
            g2d.setColor(new Color(0,0,0));
            g2d.drawString("IMMORTAL",currentX,currentY);
        } else if (type == SLOW_MO_ACTIVE) {
            g2d.setColor(new Color(0,0,0));
            g2d.drawString("SLOWMO",currentX,currentY);
        } else if (type == FIRE_RATE_ACTIVE) {
            g2d.setColor(new Color(0,0,0));
            g2d.drawString("FIRERATE",currentX,currentY);
        } else if (type == SPEED_ACTIVE) {
            g2d.setColor(new Color(0,0,0));
            g2d.drawString("SPEED",currentX,currentY);
        } else if (type == TRISHOT_ACTIVE) {
            g2d.setColor(new Color(0,0,0));
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
