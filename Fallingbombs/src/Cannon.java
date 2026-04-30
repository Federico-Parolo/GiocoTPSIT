import java.awt.*;

public class Cannon implements Drawable{

    int currentX,currentY;
    static final int WIDTH = 25;
    static final int HEIGHT = 30;
    int MOVEMENT = 75; // defines the ratio of screen that the cannon moves in a single input
    static final int DEF_MOVEMENT = 75;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y - 10;
    }


    public void drawSprite(Graphics2D g2d) {

        //g2d.setColor(new Color(0,0,0));
        //g2d.fillRect(currentX,currentY- HEIGHT /2, WIDTH, HEIGHT);

        // center
        g2d.setColor(new Color(180, 180, 180));
        g2d.fillRect(currentX + 4, currentY, 17, 22);

        // top
        g2d.setColor(new Color(200, 0, 0));
        g2d.fillRect(currentX + 8, currentY - 3, 9, 3);
        g2d.fillRect(currentX + 9, currentY - 6, 7, 3);

        // wings
        g2d.fillRect(currentX, currentY + 12, 6, 10);
        g2d.fillRect(currentX + 19, currentY + 12, 6, 10);

        g2d.fillRect(currentX + 2, currentY + 10, 3, 3);
        g2d.fillRect(currentX + 20, currentY + 10, 3, 3);

        g2d.setColor(new Color(0, 150, 255));
        g2d.fillRect(currentX + 9, currentY + 6, 7, 6);

        // trail
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(currentX + 10, currentY + 22, 3, 6);
        g2d.fillRect(currentX + 13, currentY + 22, 3, 6);

        g2d.setColor(Color.YELLOW);
        g2d.fillRect(currentX + 10, currentY + 28, 6, 2);
    }


    public Projectile fire() {
        return new Projectile(currentX + WIDTH /2 - Projectile.WIDTH/2,currentY);
    }
}
