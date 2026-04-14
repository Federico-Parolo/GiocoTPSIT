import java.awt.*;

public class Explosion {
    int currentX,currentY;
    volatile private int lifespan = 20; // intended in frames (how many refreshes survives)
    String type;
    public static final String COLLISION = "bomb-projectile";
    public static final String ENDGAME = "ground";

    public Explosion(String type, int currentX, int currentY) {
        this.type = type;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public void drawSprite(Graphics2D g2d) {
        g2d.setColor(new Color(250,150,10));
        if (type.equals(COLLISION)) {
            g2d.fillOval(currentX,currentY,10,10);
        } else if (type.equals(ENDGAME)) {
            g2d.fillOval(currentX,currentY,10,20);
        } else {
            g2d.setColor(new Color(255,0,0));
            g2d.fillOval(currentX,currentY,10,10);
        }
    }

    synchronized public void changeLifespan(int delta) {
        lifespan += delta;
    }

    synchronized public int getLifespan() {
        return lifespan;
    }
}
