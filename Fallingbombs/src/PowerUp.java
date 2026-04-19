import java.awt.*;
import java.util.Random;

public class PowerUp extends Thread implements Drawable{
    int currentX,currentY;
    public enum Type {
        Speed,FireRate,SlowMo;
    }
    public static Type[] types = Type.values();
    Type type;
    int speed;
    static final int WIDTH = 15;
    static final int HEIGHT = 15;
    static final int ARCLEN = 5;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private static Random r = new Random();

    public PowerUp(int x,int y) {
        currentX = x;
        currentY = y;
        speed = (int)(Math.random() * 10) + 1;
        type = types[r.nextInt(types.length)];
    }

    public void drawSprite(Graphics2D g2d) {
        g2d.setColor(new Color(0, 255, 42));
        g2d.fillRoundRect(currentX,currentY,WIDTH,HEIGHT,ARCLEN,ARCLEN);
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

    public void move() {
        currentY += speed;
    }

    public void pausePowerUp() {paused = true;}
    public void stopPowerUp() { running = false;}
    public void resumePowerUp() {paused = false;}
}
