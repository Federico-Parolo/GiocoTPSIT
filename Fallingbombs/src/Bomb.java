import java.awt.*;

public class Bomb extends Thread{

    volatile int currentX,currentY;
    int speed;
    static int width,height;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public Bomb(int x,int y) {
        currentX = x;
        currentY = y;
        width = height = 15;
        speed = (int)(Math.random() * 10) + 1;
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


    public void drawSprite(Graphics2D g2d) {
        g2d.fillOval(currentX,currentY,width,height);
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
