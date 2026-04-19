import java.awt.*;
import java.util.Random;

public class PowerUp extends Thread implements Drawable{
    int currentX,currentY;
    volatile private int lifespan = 3500; // ms
    public enum Type {
        Speed,
        FireRate,
        // not implemented
        SlowMo,
        TriShot,
        Immortal;
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
        speed = 10;
        type = types[r.nextInt(types.length)];
    }

    public void drawSprite(Graphics2D g2d) {
        switch (type) {
            case Speed -> g2d.setColor(new Color(0, 255, 42));
            case SlowMo -> g2d.setColor(new Color(150,250,250));
            case FireRate -> g2d.setColor(new Color(255,100,50));
            case null, default -> g2d.setColor(new Color(255,255,255));
        }
        g2d.fillRoundRect(currentX,currentY,WIDTH,HEIGHT,ARCLEN,ARCLEN);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(currentX,currentY,WIDTH,HEIGHT,ARCLEN,ARCLEN);
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
