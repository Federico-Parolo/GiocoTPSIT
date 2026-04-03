import java.awt.*;

public class Cannon {

    int currentX,currentY;
    int h,w;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y;
        h = 50;
        w = 50;
    }


    public void drawSprite(Graphics2D g2d) {
        g2d.fillRect(currentX,currentY-h/2,w,h);
    }

    public Projectile fire() {
        System.out.println("Fired");
        return new Projectile(currentX + w/2,currentY);
    }
}
