import java.awt.*;

public class Bomb extends Thread{

    volatile int currentX,currentY;
    int speed;
    static int width,height;
    boolean running = true;

    public Bomb(int x,int y) {
        currentX = x;
        currentY = y;
        width = height = 15;
        speed = (int)(Math.random() * 10) + 1;
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


    public void drawSprite(Graphics2D g2d) {
        g2d.fillOval(currentX,currentY,width,height);
    }

    synchronized void  move() {
        currentY += speed;
    }
}
