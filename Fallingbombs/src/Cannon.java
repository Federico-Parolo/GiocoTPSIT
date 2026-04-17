import java.awt.*;

public class Cannon implements Drawable{

    int currentX,currentY;
    static final int WIDTH = 25;
    static final int HEIGHT = 30;
    int MOVEMENT = 50;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y;

    }


    public void drawSprite(Graphics2D g2d) {
        g2d.setColor(new Color(0,0,0));
        g2d.fillRect(currentX,currentY- HEIGHT /2, WIDTH, HEIGHT);
    }

    public Projectile fire() {
        System.out.println("Fired");
        return new Projectile(currentX + WIDTH /2,currentY);
    }
}
