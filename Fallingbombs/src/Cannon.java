import java.awt.*;

public class Cannon implements Drawable{

    int currentX,currentY;
    static final int WIDTH = 25;
    static final int HEIGHT = 30;
    int MOVEMENT = 75; // defines the ratio of screen that the cannon moves in a single input
    static final int DEF_MOVEMENT = 75;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y;
    }


    public void drawSprite(Graphics2D g2d) {
        /*
        g2d.setColor(new Color(0,0,0));
        g2d.fillRect(currentX,currentY- HEIGHT /2, WIDTH, HEIGHT);*/
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = currentX + WIDTH / 2;

// ---- BASE ----
        int baseHeight = (int)(HEIGHT * 0.4);

        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRoundRect(
                currentX,
                currentY + HEIGHT - baseHeight,
                WIDTH,
                baseHeight,
                8, 8
        );

// ---- BARREL ----
        int barrelWidth = (int)(WIDTH * 0.4);
        int barrelHeight = (int)(HEIGHT * 0.5);

        int barrelX = centerX - barrelWidth / 2;
        int barrelY = currentY + (int)(HEIGHT * 0.1);

        g2d.setColor(new Color(120, 120, 120));
        g2d.fillRoundRect(
                barrelX,
                barrelY,
                barrelWidth,
                barrelHeight,
                barrelWidth,
                barrelWidth
        );

// ---- MUZZLE ----
        int muzzleSize = (int)(barrelWidth * 0.8);

        int muzzleX = centerX - muzzleSize / 2;
        int muzzleY = barrelY - muzzleSize / 2;

        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(muzzleX, muzzleY, muzzleSize, muzzleSize);

// ---- DETAIL STRIPE ----
        g2d.setColor(new Color(90, 90, 90));
        g2d.fillRect(
                barrelX,
                barrelY + barrelHeight / 2,
                barrelWidth,
                (int)(HEIGHT * 0.05)
        );
    }

    public Projectile fire() {
        return new Projectile(currentX + WIDTH /2 - Projectile.WIDTH/2,currentY);
    }
}
