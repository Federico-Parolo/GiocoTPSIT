import java.awt.*;

public class Projectile extends Thread implements Drawable{

    int currentX;
    int currentY;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 15;
    boolean running = true;
    int speed = 2;
    boolean paused = false;

    public Projectile(int currentX,int currentY) {
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public void drawSprite(Graphics2D g2d) {

        g2d.setColor(new Color(59, 59, 59));
        g2d.fillRect(currentX,currentY, WIDTH, HEIGHT);
    }

    @Override
    public void run() {
        while (running) {
            if (!paused) {
                move();
            }
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

    synchronized public void stopProjectile() {
        running = false;
    }

    synchronized public void pauseProjectile() {
        paused = true;
    }

    synchronized public void resumeProjectile() {
        paused = false;
    }
}
