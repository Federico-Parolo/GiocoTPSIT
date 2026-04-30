import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GamePanel extends JPanel {

    int difficulty = 1;
    int currentPoints = 0;
    int bombPoints = 10;
    private long lastFireTime;
    private long fireDelay = 300;
    static final int DEF_FIRE_DELAY = 300;
    private boolean leftPressed = false, rightPressed = false;
    Cannon c;
    PausePanel pausePanel;
    private final Timer refresh;
    private final Timer spawnBomb;
    private final Timer deleteInvalidEntity;
    private final Timer spawnPowerUp;
    private final java.util.List<Bomb> bombs;
    private final java.util.List<Projectile> projectiles;
    private final java.util.List<Explosion> explosions;
    private final java.util.List<PowerUp> powerUps;
    private final java.util.List<PowerUp> activePowerUps;
    Random r = new Random();
    private int baseY = 500;
    private volatile boolean gameRunning = false;
    private boolean paused;
    private boolean powerUpEn;
    private boolean immortal = false;
    private boolean triShotActive = false;


    // action variables to manage input
    public Action pauseAction;
    public Action fireAction;
    public Action leftPressAction;
    public Action rightPressAction;
    public Action leftUnPressAction;
    public Action rightUnPressAction;

    // Auto play Mode
    private Bot bot;
    private boolean aiModeOn = false;

    public GamePanel(int w, int h) {
        super();
        setLayout(new BorderLayout());
        setSize(w, h);
        Explosion.gamePanel = this;
        c = new Cannon(w / 2, baseY);
        pausePanel = new PausePanel();
        bombPoints = bombPoints * difficulty;
        // every method in these arrays is automatically synchronized
        bombs = Collections.synchronizedList(new ArrayList<>());
        projectiles = Collections.synchronizedList(new ArrayList<>());
        explosions = Collections.synchronizedList(new ArrayList<>());
        powerUps = Collections.synchronizedList(new ArrayList<>());
        activePowerUps = Collections.synchronizedList(new ArrayList<>());
        ;

        // setting up the cations to later chain to input system
        initActions();
        // used to set up the input gathering and the actions to perform on key pressed/released
        initInput(getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), getActionMap());

        refresh = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // cannon movement
                if (leftPressed) {
                    moveLeft();
                    //System.out.println("left");
                }
                if (rightPressed) {
                    moveRight();
                    //System.out.println("right");
                }
                ArrayList<Bomb> toRemoveB = new ArrayList<>();
                ArrayList<Projectile> toRemoveP = new ArrayList<>();
                ArrayList<PowerUp> toRemovePup = new ArrayList<>();

                // bomb interactions

                synchronized (projectiles){
                    for (Projectile p : projectiles) {
                        Rectangle rect = new Rectangle(p.currentX, p.currentY, Projectile.WIDTH, Projectile.HEIGHT);

                        synchronized (bombs){
                            for (Bomb b : bombs) {
                                if (b.currentY > 0 && rect.intersects(new Rectangle(b.currentX, b.currentY, Bomb.WIDTH, Bomb.HEIGHT))) {
                                    toRemoveP.add(p);
                                    toRemoveB.add(b);
                                    explosions.add(new Explosion(Explosion.EffectType.Collision, p.currentX, p.currentY));
                                }
                            }
                        }

                    }
                }
                bombs.removeAll(toRemoveB);
                projectiles.removeAll(toRemoveP);
                currentPoints += toRemoveB.size() * bombPoints * difficulty;

                // powerUp interactions


                synchronized (projectiles){
                    for (Projectile p : projectiles) {
                        Rectangle rect = new Rectangle(p.currentX, p.currentY, Projectile.WIDTH, Projectile.HEIGHT);

                        synchronized (powerUps){
                            for (PowerUp pUp : powerUps) {
                                if (pUp.currentY > 0 && rect.intersects(new Rectangle(pUp.currentX, pUp.currentY, PowerUp.WIDTH, PowerUp.HEIGHT))) {
                                    toRemoveP.add(p);
                                    toRemovePup.add(pUp);
                                    activePowerUps.add(pUp);

                                    explosions.add(new Explosion(Explosion.EffectType.PowerUp, p.currentX, p.currentY));
                                    explosions.add(new Explosion(getEffectType(pUp.type), pUp.currentX, pUp.currentY));
                                }
                            }
                        }

                    }
                }
                powerUps.removeAll(toRemovePup);
                projectiles.removeAll(toRemoveP);
                applyPowerUps();


                // check if bombs are removed this way = game finished

                synchronized (bombs){
                    for (Bomb b : bombs) {
                        if (b.currentY >= baseY) {
                            if (!immortal) resetGame();
                            toRemoveB.add(b);
                            explosions.add(new Explosion(Explosion.EffectType.Endgame, b.currentX, b.currentY));
                            break;
                        }
                    }
                }

                bombs.removeAll(toRemoveB);

                // update for cannon and grass height for resizable window
                baseY = getHeight() * 14 / 15;
                c.currentY = baseY;

                // update for all explosion so they disappear when lifespan <= 0
                updateExplosions();

                repaint();
            }
        });
        // spawn rate: EASY = 1000ms, MED = 900ms, HARD = 700ms
        spawnBomb = new Timer(1100 - 100 * difficulty, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (bombs) {
                    Bomb b = new Bomb(r.nextInt(0, getWidth() - Bomb.WIDTH), r.nextInt(-200, 0));
                    bombs.add(b);
                    b.start();
                }
            }
        });
        deleteInvalidEntity = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    projectiles.removeIf(obj -> obj.currentY < 0);

                    powerUps.removeIf(obj -> obj.currentY > getHeight());

            }
        });
        spawnPowerUp = new Timer(4000 + 1000 * difficulty, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    PowerUp p = new PowerUp(r.nextInt(0, getWidth() - PowerUp.WIDTH), r.nextInt(-200, 0));
                    powerUps.add(p);
                    p.start();
            }
        });

        pausePanel.resumeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeGame();
            }
        });
        add(pausePanel);

        // creation of the AI
        bot = new Bot(this, bombs, c);

        repaint();
    }

    public void moveLeft() {
        c.currentX -= (c.currentX <= 0) ? 0 : getWidth() / c.MOVEMENT;
    }

    public void moveRight() {
        c.currentX += (c.currentX + Cannon.WIDTH >= getWidth()) ? 0 : getWidth() / c.MOVEMENT;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 100));
        g2d.fillRect(0, 0, getWidth(), getHeight());


        g2d.setColor(new Color(0, 100, 10));
        g2d.fillRect(0, baseY, getWidth(), getHeight());

        if (immortal) {
            g2d.setColor(new Color(119, 119, 119));
            g2d.fillRect(0, baseY, getWidth(), 5);
            int triangleCount = 15;
            int triWidth = 20;
            int triHeight = 20;
            int totalW = getWidth();
            int totalTriW = triangleCount * triWidth;
            int spacing = Math.max(0, (totalW - totalTriW) / (triangleCount + 1));

            int x = spacing;

            for (int i = 0; i < triangleCount; i++) {
                int cx = x + triWidth / 2;

                Polygon t = new Polygon();
                t.addPoint(cx, baseY - triHeight); // top
                t.addPoint(x, baseY);              // left
                t.addPoint(x + triWidth, baseY);   // right

                g2d.fillPolygon(t);

                x += triWidth + spacing;
            }
        }

        // draw current points label
        g2d.setColor(new Color(0, 0, 0));
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        g2d.drawString("Points: " + currentPoints, 5, 20);


        // drawing every spawned bomb
        synchronized (bombs) {
            for (Bomb b : bombs) {
                b.drawSprite(g2d);
            }
        }

        //drawing every projectile fired
        synchronized (projectiles) {
            for (Projectile p : projectiles) {
                p.drawSprite(g2d);
            }
        }

        // drawing explosions for when objects collide or game ends
        synchronized (explosions) {
            for (Explosion e : explosions) {
                e.drawSprite(g2d);
            }
        }
        if (powerUpEn) {
            synchronized (powerUps) {
                for (PowerUp p : powerUps) {
                    p.drawSprite(g2d);
                }
            }
        }

        c.drawSprite(g2d);

    }

    private void initActions() {

        leftPressAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    leftPressed = true;
                }
            }
        };


        rightPressAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    rightPressed = true;
                }
            }
        };


        leftUnPressAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    leftPressed = false;
                }
            }
        };


        rightUnPressAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    rightPressed = false;
                }
            }
        };


        fireAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fire();
            }
        };


        pauseAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaused()) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        };
    }

    private void toggleInputEn(boolean state) {
        //pauseAction.setEnabled(false);
        fireAction.setEnabled(state);
        leftPressAction.setEnabled(state);
        rightPressAction.setEnabled(state);
        leftUnPressAction.setEnabled(state);
        rightUnPressAction.setEnabled(state);
    }

    private void initInput(InputMap inputMap, ActionMap actionMap) {
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left-Pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right-Pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "Left-Released");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "Right-Released");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Fire");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Pause");

        actionMap.put("Left-Pressed", leftPressAction);
        actionMap.put("Right-Pressed", rightPressAction);
        actionMap.put("Left-Released", leftUnPressAction);
        actionMap.put("Right-Released", rightUnPressAction);
        actionMap.put("Fire", fireAction);
        actionMap.put("Pause", pauseAction);
    }


    public void resetGame() {
        spawnBomb.stop();
        if (powerUpEn) spawnPowerUp.stop();
        deleteInvalidEntity.stop();
        refresh.stop();
        // unnecessary only to prevent threads from running without the JVM knowing
        synchronized (bombs) {
            for (Bomb b : bombs) {
                b.stopBomb();
            }
        }
        synchronized (projectiles) {
            for (Projectile p : projectiles) {
                p.stopProjectile();
            }
        }
        if (powerUpEn) {
            synchronized (powerUps) {
                for (PowerUp p : powerUps) {
                    p.stopPowerUp();
                }
            }
        }
        bombs.clear();
        projectiles.clear();
        explosions.clear();
        if (powerUpEn) powerUps.clear();
        pausePanel.setVisible(false);
        paused = false;
        aiModeOn = false;
        bot.stopBot();
        bot = new Bot(this, bombs, c);
        toggleInputEn(true);
        changeGameState(false);
    }

    public void startNewGame(int diff, boolean powerUpEn) {
        //System.out.println(powerUpEn);
        difficulty = diff;
        paused = false;
        this.powerUpEn = powerUpEn;
        spawnBomb.setDelay(1100 - 100 * difficulty);
        currentPoints = 0;
        bombs.clear();
        projectiles.clear();
        explosions.clear();
        spawnBomb.start();
        if (powerUpEn) spawnPowerUp.start();
        refresh.start();
        deleteInvalidEntity.start();
        pausePanel.setVisible(false);
        changeGameState(true);
    }

    public void startNewAIGame(int diff) {
        aiModeOn = true;
        toggleInputEn(false);
        difficulty = diff;
        paused = false;
        this.powerUpEn = false;
        spawnBomb.setDelay(1100 - 100 * difficulty);
        currentPoints = 0;
        bombs.clear();
        projectiles.clear();
        spawnBomb.start();
        refresh.start();
        deleteInvalidEntity.start();
        pausePanel.setVisible(false);
        changeGameState(true);
        bot.start();
    }

    public void pauseGame() {
        paused = true;
        spawnBomb.stop();
        spawnPowerUp.stop();
        refresh.stop();
        deleteInvalidEntity.stop();
        for (Bomb b : bombs) {
            b.pauseBomb();
        }
        for (Projectile p : projectiles) {
            p.pauseProjectile();
        }
        for (PowerUp p : powerUps) {
            p.pausePowerUp();
        }
        if (aiModeOn) bot.pauseBot();
        pausePanel.setVisible(true);
    }

    public void resumeGame() {
        pausePanel.setVisible(false);
        for (Bomb b : bombs) {
            b.resumeBomb();
        }
        for (Projectile p : projectiles) {
            p.resumeProjectile();
        }
        for (PowerUp p : powerUps) {
            p.resumePowerUp();
        }
        if (aiModeOn) bot.resumeBot();
        spawnBomb.start();
        if (powerUpEn) spawnPowerUp.start();
        refresh.start();
        paused = false;
    }

    public void changeGameState(boolean newState) {
        boolean oldState = gameRunning;
        gameRunning = newState;

        firePropertyChange("gameRunning", oldState, gameRunning);
    }


    public void spawnProjectile(Projectile p) {
        projectiles.add(p);
        p.start();
    }

    public boolean isPaused() {
        return paused;
    }

    synchronized public void updateExplosions() {
        for (Explosion e : explosions) {
            e.updateExplosion(-1);
        }
        explosions.removeIf(e -> e.getLifespan() <= 0);
    }

    public void applyPowerUps() {
        ArrayList<PowerUp> toRemovePuP = new ArrayList<>();
        for (PowerUp p : activePowerUps) {

            p.updateLifespan(-1);
            // for every power up apply it and if finished revert back stats
            switch (p.type) {

                case FireRate -> {
                    if (p.getLifespan() < 0) {
                        toRemovePuP.add(p);
                        fireDelay = DEF_FIRE_DELAY;
                        System.out.println("FireDelay Deactivated");
                    } else {
                        if (fireDelay != DEF_FIRE_DELAY / 2) {
                            fireDelay = DEF_FIRE_DELAY / 2;
                            System.out.println("FireDelay Active");
                        }
                    }
                }
                case SlowMo -> {
                    // slows down every bomb to a predefined speed that scales on difficulty
                    if (p.getLifespan() < 0) {
                        toRemovePuP.add(p);
                        synchronized (bombs) {
                            for (Bomb b : bombs) {
                                b.speed = b.DEF_SPEED;
                            }
                        }
                        System.out.println("SlowMo Deactivated");
                    } else {
                        synchronized (bombs) {
                            for (Bomb b : bombs) {
                                b.speed = difficulty;
                            }
                        }
                    }
                }
                case Speed -> {
                    // with every input the cannon moves faster horizontally
                    if (p.getLifespan() < 0) {
                        toRemovePuP.add(p);
                        c.MOVEMENT = Cannon.DEF_MOVEMENT;
                        System.out.println("Speed deactivated");
                    } else {
                        if (c.MOVEMENT != (int) (Cannon.DEF_MOVEMENT * 0.66)) {
                            c.MOVEMENT = (int) (Cannon.DEF_MOVEMENT * 0.66);
                            System.out.println("Speed Active");
                        }
                    }
                }
                // the cannon fires 3 projectiles in a rapid sequence
                case TriShot -> {
                    if (p.getLifespan() < 0) {
                        toRemovePuP.add(p);
                        triShotActive = false;
                        System.out.println("TriShot Deactivated");
                    } else {
                        triShotActive = true;
                    }
                }
                case Immortal -> {
                    if (p.getLifespan() < 0) {
                        toRemovePuP.add(p);
                        immortal = false;
                        System.out.println("Immortal Deactivated");
                    } else {
                        immortal = true;
                        //System.out.println("Immortal Active");
                    }
                }
                case null, default -> {
                    toRemovePuP.add(p);
                    System.out.println("Not recognized power Up");
                }
            }
        }
        // removing reference to used powerUps
        activePowerUps.removeAll(toRemovePuP);
    }

    public Explosion.EffectType getEffectType(PowerUp.Type t) {
        switch (t) {
            case FireRate -> {
                return Explosion.EffectType.FireRateActive;
            }
            case SlowMo -> {
                return Explosion.EffectType.SlowMoActive;
            }
            case Speed -> {
                return Explosion.EffectType.SpeedActive;
            }
            case TriShot -> {
                return Explosion.EffectType.TriShotActive;
            }
            case Immortal -> {
                return Explosion.EffectType.ImmortalActive;
            }
            case null, default -> {
                return Explosion.EffectType.Collision;
            }
        }

    }

    public boolean fire() {
        long now = System.currentTimeMillis();
        if (gameRunning && (now - lastFireTime >= fireDelay)) {
            lastFireTime = now;

            if (triShotActive) {
                Projectile p = c.fire();
                spawnProjectile(p);

                p = c.fire();
                p.currentX -= 20;
                spawnProjectile(p);

                p = c.fire();
                p.currentX += 20;
                spawnProjectile(p);
            } else {
                Projectile p = c.fire();
                spawnProjectile(p);
            }
            return true;
        }
        return false;
    }
}
