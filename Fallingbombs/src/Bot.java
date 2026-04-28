import java.util.HashSet;
import java.util.List;

public class Bot extends Thread {

    private List<Bomb> bombs;
    private final HashSet<Bomb> targeted;
    private Cannon c;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final GamePanel game;

    private long fireDelay = 300; // ms
    private long lastFireTime = 0;

    public Bot(GamePanel game, List<Bomb> bombs, Cannon c) {
        this.game = game;
        this.bombs = bombs;
        this.c = c;
        targeted = new HashSet<>();
    }

    public void setBombs(List<Bomb> bombs) {
        this.bombs = bombs;
    }

    public void setFireDelay(long delay) {
        this.fireDelay = delay;
    }

    @Override
    public void run() {
        while (running) {

            if (paused) {
                delay (20);
                continue;
            }
            Bomb target = checkClosestBomb();
            if (targeted.contains(target)) target = null;
            synchronized (targeted){
                if (target != null) {
                    moveToTarget(target);
                    game.fire();
                    targeted.add(target);
                }
            }

            delay(10); // delay between actions
        }
    }

    private Bomb checkClosestBomb() {
        Bomb closest = null;

        synchronized (bombs) {
            for (Bomb b : bombs) {
                if (b.currentY > 0) {
                    if (closest == null || (!targeted.contains(b) && b.currentY > closest.currentY)) {
                        closest = b;
                    }
                }
            }
        }

        return closest;
    }


    private void moveToTarget(Bomb target) {
        while (running && !paused) {
            int cannonCenter = c.currentX + Cannon.WIDTH / 2;
            int targetCenter = target.currentX + Bomb.WIDTH / 2;
            int dx = targetCenter - cannonCenter;
            if (Math.abs(dx) <= 4) {
                return;
            }
            if (dx < 0) {
                game.moveLeft();
            } else {
                game.moveRight();
            }
            delay(10);
        }
    }

    private void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    public synchronized void stopBot() {
        running = false;
    }

    public synchronized void pauseBot() {
        paused = true;
    }

    public synchronized void resumeBot() {
        paused = false;
    }
}