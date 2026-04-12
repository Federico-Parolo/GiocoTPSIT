import java.awt.*;

public class Cannon {

    int currentX,currentY;
    int width,height ;
    int MOVEMENT = 50;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y;
        height = 30;
        width = 25;
    }


    public void drawSprite(Graphics2D g2d) {
        g2d.fillRect(currentX,currentY- height /2, width, height);
    }

    public Projectile fire() {
        System.out.println("Fired");
        return new Projectile(currentX + width /2,currentY);
    }
}
