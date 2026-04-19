import java.awt.*;

public class Explosion implements Drawable{
    int currentX,currentY;
    volatile private int lifespan = 20; // intended in frames (how many refreshes survives)
    String type;
    int diameter = 10;
    public static final String BOMB_COLLISION = "bomb-projectile";
    public static final String ENDGAME = "ground";
    public static final String POWER_UP = "powerUP-projectile";

    public Explosion(String type, int currentX, int currentY) {
        this.type = type;
        this.currentX = currentX + diameter/2;
        this.currentY = currentY + diameter/2;
    }

    public void drawSprite(Graphics2D g2d) {

        if (type.equals(BOMB_COLLISION)) {
            g2d.setColor(new Color(250,150,10, 255-diameter*4));
            g2d.fillOval(currentX - diameter/2,currentY - diameter/2,diameter,diameter);
        } else if (type.equals(POWER_UP)) {
            g2d.setColor(new Color(10,150,250, 255-diameter*4));
            g2d.fillRoundRect(currentX - diameter/2,currentY - diameter/2,diameter,diameter, 5,5);
        }
        /* else if (type.equals(ENDGAME)) {
            g2d.fillOval(currentX,currentY,10,20);
        }*/
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
