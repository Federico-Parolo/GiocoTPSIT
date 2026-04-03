import java.awt.*;

public class Projectile extends Thread{

    int currentX;
    int currentY;
    int width,height;
    boolean running = true;
    int speed = 2;

    public Projectile(int currentX,int currentY) {
        width = 10;
        height = 15;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public void drawSprite(Graphics2D g2d) {
        g2d.fillRect(currentX,currentY,width,height);
    }

    @Override
    public void run() {
        while (running) {
            move();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    synchronized public void move() {
        currentY -= 10;
    }
}
