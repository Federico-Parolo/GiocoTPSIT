import java.awt.*;

public class Bomb extends Thread{

    volatile int currentX,currentY;
    int speed;
    static final int WIDTH = 15;
    static final int HEIGHT = 15;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public Bomb(int x,int y) {
        currentX = x;
        currentY = y;
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
        g2d.fillOval(currentX,currentY, WIDTH, HEIGHT);
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
