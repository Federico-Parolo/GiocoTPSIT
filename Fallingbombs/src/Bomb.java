import java.awt.*;

public class Bomb extends Thread implements Drawable{

    volatile int currentX,currentY;
    public volatile int speed;
    public final int DEF_SPEED;
    static final int WIDTH = 18;
    static final int HEIGHT = 18;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public Bomb(int x,int y) {
        currentX = x;
        currentY = y;
        speed = (int)(Math.random() * 5) + 1;
        DEF_SPEED = speed;
    }

    @Override
    public void run() {
        while (running) {
            if (!paused) {
                move();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void drawSprite(Graphics2D g2d) {
        //g2d.fillOval(currentX,currentY, WIDTH, HEIGHT);


        // Main meteorite body (gray)
        g2d.setColor(new Color(160, 160, 160));
        g2d.fillOval(currentX + 2, currentY + 2, WIDTH - 4, HEIGHT - 4);

        // Slight shading (darker edge)
        g2d.setColor(new Color(120, 120, 120));
        g2d.drawOval(currentX + 2, currentY + 2, WIDTH - 4, HEIGHT - 4);

        // Craters (darker holes)
        g2d.setColor(new Color(90, 90, 90));
        g2d.fillOval(currentX + 5, currentY + 5, 3, 3);
        g2d.fillOval(currentX + 10, currentY + 6, 2, 2);
        g2d.fillOval(currentX + 7, currentY + 11, 3, 2);

        // Small highlights for depth
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(currentX + 6, currentY + 4, 2, 2);
    }

    synchronized void  move() {
        currentY += speed;
    }

    synchronized public void stopBomb() {
        running = false;
    }

    synchronized public void pauseBomb() {
        paused = true;
    }

    synchronized public void resumeBomb() {
        paused = false;
    }
}
