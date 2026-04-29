import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends Thread {

    private final List<Bomb> bombs;
    private final Map<Bomb, Long> lastShotTime; // cooldown per bomb
    private final Cannon c;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final GamePanel game;

    private long perTargetCooldown = 2500; // avoid spamming same bomb

    public Bot(GamePanel game, List<Bomb> bombs, Cannon c) {
        this.game = game;
        this.bombs = bombs;
        this.c = c;
        this.lastShotTime = new HashMap<>();
    }

    @Override
    public void run() {
        while (running) {

            if (paused) {
                delay(20);
                continue;
            }

            cleanupCooldownMap();

            Bomb target = selectBestTarget();

            if (target != null) {
                moveToTarget(target);
                tryFire(target);
            }

            delay(10);
        }
    }

    // ---- TARGET SELECTION ----
    private Bomb selectBestTarget() {
        Bomb best = null;
        long now = System.currentTimeMillis();

        synchronized (bombs) {
            for (Bomb b : bombs) {

                if (b.currentY <= 0) continue;

                long last = lastShotTime.getOrDefault(b, 0L);
                if (now - last < perTargetCooldown) continue;

                if (best == null || b.currentY > best.currentY) {
                    best = b;
                }
            }
        }

        return best;
    }

    // ---- MOVEMENT ----
    private void moveToTarget(Bomb target) {
        while (running && !paused) {
            int cannonCenter = c.currentX + Cannon.WIDTH / 2;
            int targetCenter = target.currentX + Bomb.WIDTH / 2;

            int dx = targetCenter - cannonCenter;

            if (Math.abs(dx) <= 6) return;

            if (dx < 0) {
                game.moveLeft();
            } else {
                game.moveRight();
            }

            delay(10);
        }
    }

    // ---- FIRE CONTROL ----
    private void tryFire(Bomb target) {
        long now = System.currentTimeMillis();

        if (game.fire()) {
            lastShotTime.put(target, now);
        }
    }

    // ---- CLEANUP ----
    private void cleanupCooldownMap() {
        synchronized (bombs) {
            lastShotTime.keySet().retainAll(bombs);
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