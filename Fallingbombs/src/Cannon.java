import java.awt.*;

public class Cannon implements Drawable{

    int currentX,currentY;
    static final int WIDTH = 25;
    static final int HEIGHT = 30;
    int MOVEMENT = 75; // defines the ratio of screen that the cannon moves in a single input
    static final int DEF_MOVEMENT = 75;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y;
    }


    public void drawSprite(Graphics2D g2d) {

        g2d.setColor(new Color(0,0,0));
        g2d.fillRect(currentX,currentY- HEIGHT /2, WIDTH, HEIGHT);
    }

    public Projectile fire() {
        return new Projectile(currentX + WIDTH /2 - Projectile.WIDTH/2,currentY);
    }
}
