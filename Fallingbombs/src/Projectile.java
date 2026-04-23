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
        /*
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);




        int centerX = currentX + WIDTH / 2;

        // ---- BODY ----
        int bodyWidth = (int)(WIDTH * 0.5);
        int bodyX = centerX - bodyWidth / 2;
        int bodyHeight = (int)(HEIGHT * 0.6);

        g2d.setColor(new Color(180, 180, 180));
        g2d.fillRoundRect(bodyX, currentY + (int)(HEIGHT * 0.2),
                bodyWidth, bodyHeight,
                bodyWidth, bodyWidth);

        // ---- TIP ----
        Polygon tip = new Polygon();
        tip.addPoint(centerX, currentY);
        tip.addPoint(bodyX, currentY + (int)(HEIGHT * 0.2));
        tip.addPoint(bodyX + bodyWidth, currentY + (int)(HEIGHT * 0.2));

        g2d.setColor(new Color(255, 120, 50));
        g2d.fillPolygon(tip);

        // ---- TAIL ----
        Polygon tail = new Polygon();
        int tailY = currentY + (int)(HEIGHT * 0.8);

        tail.addPoint(centerX, currentY + HEIGHT);
        tail.addPoint(bodyX, tailY);
        tail.addPoint(bodyX + bodyWidth, tailY);

        g2d.setColor(new Color(255, 200, 50));
        g2d.fillPolygon(tail);*/
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
