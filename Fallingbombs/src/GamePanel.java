import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GamePanel extends JPanel{

    int difficulty = 1;
    int currentPoints = 0;
    int bombPoints = 10;
    private long lastFireTime;
    private long fireDelay = 300;
    static final int DEF_FIRE_DELAY = 300;
    private boolean leftPressed = false,rightPressed = false;
    Cannon c;
    PausePanel pausePanel;
    private Timer refresh;
    private Timer spawnBomb;
    private Timer deleteInvalidEntity;
    private Timer spawnPowerUp;
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


    public GamePanel(int w,int h) {
        super();
        setLayout(new BorderLayout());
        setSize(w,h);
        c = new Cannon(w/2,baseY);
        pausePanel = new PausePanel();
        bombPoints = bombPoints * difficulty;
        bombs = Collections.synchronizedList(new ArrayList<>());
        projectiles = Collections.synchronizedList(new ArrayList<>());
        explosions = Collections.synchronizedList(new ArrayList<>());
        powerUps = Collections.synchronizedList(new ArrayList<>());
        activePowerUps = Collections.synchronizedList(new ArrayList<>());;

        // used to set up the input gathering and the actions to perform on key pressed/released
        initInput(getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW),getActionMap());

        refresh = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (leftPressed) {
                    c.currentX -= (c.currentX <= 0) ? 0 : getWidth()/c.MOVEMENT;
                    //System.out.println("left");
                }
                if (rightPressed) {
                    c.currentX += (c.currentX + Cannon.WIDTH >= getWidth()) ? 0 : getWidth()/c.MOVEMENT;
                    //System.out.println("right");
                }
                ArrayList<Bomb> toRemoveB = new ArrayList<>();
                ArrayList<Projectile> toRemoveP = new ArrayList<>();
                ArrayList<PowerUp> toRemovePup = new ArrayList<>();

                // bomb interactions
                synchronized (projectiles) {
                    for (Projectile p : projectiles) {
                        Rectangle rect = new Rectangle(p.currentX,p.currentY,p.WIDTH,p.HEIGHT);
                        synchronized (bombs) {
                            for (Bomb b : bombs) {
                                if (b.currentY > 0 && rect.intersects(new Rectangle(b.currentX,b.currentY, Bomb.WIDTH, Bomb.HEIGHT))){
                                    toRemoveP.add(p);
                                    toRemoveB.add(b);
                                    explosions.add(new Explosion(Explosion.BOMB_COLLISION,p.currentX,p.currentY));
                                }
                            }
                        }
                    }
                    bombs.removeAll(toRemoveB);
                    projectiles.removeAll(toRemoveP);
                    currentPoints += toRemoveB.size() * bombPoints *  difficulty;

                    // powerUp interactions

                    synchronized (projectiles) {
                        for (Projectile p : projectiles) {
                            Rectangle rect = new Rectangle(p.currentX,p.currentY,p.WIDTH,p.HEIGHT);
                            synchronized (powerUps) {
                                for (PowerUp pUp : powerUps) {
                                    if (pUp.currentY > 0 && rect.intersects(new Rectangle(pUp.currentX,pUp.currentY,PowerUp.WIDTH,PowerUp.HEIGHT))) {
                                        toRemoveP.add(p);
                                        toRemovePup.add(pUp);
                                        synchronized (activePowerUps) {
                                            activePowerUps.add(pUp);
                                        }
                                        explosions.add(new Explosion(Explosion.POWER_UP,p.currentX,p.currentY));
                                    }
                                }
                            }
                        }
                        powerUps.removeAll(toRemovePup);
                        projectiles.removeAll(toRemoveP);
                        applyPowerUps();
                    }
                }

                // check if bombs are removed this way = game finished
                synchronized (bombs) {
                    for (Bomb b : bombs) {
                        if (b.currentY /*+ Bomb.HEIGHT*/ >= baseY) {
                            resetGame();
                            break;
                        }
                    }
                }

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
                    Bomb b = new Bomb(r.nextInt(0,getWidth()-Bomb.WIDTH),r.nextInt(-200,0));
                    bombs.add(b);
                    b.start();
                }
            }
        });
        deleteInvalidEntity = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (projectiles) {
                    projectiles.removeIf(obj -> obj.currentY < 0);
                }
                synchronized (powerUps) {
                    powerUps.removeIf(obj -> obj.currentY > baseY); // TODO modify to getHeight()
                }
            }
        });
        spawnPowerUp = new Timer(4800 + 200 * difficulty, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (powerUps) {
                    PowerUp p = new PowerUp(r.nextInt(0,getWidth()-PowerUp.WIDTH),r.nextInt(-200,0));
                    powerUps.add(p);
                    p.start();
                }
            }
        });
        
        pausePanel.resumeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeGame();
            }
        });
        add(pausePanel);
        
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(100,255,255));
        g2d.fillRect(0,0,getWidth(),getHeight());


        g2d.setColor(new Color(25,200,25));
        g2d.fillRect(0,baseY,getWidth(),getHeight());

        g2d.setColor(new Color(0,0,0));
        g2d.setFont(new Font(Font.MONOSPACED,Font.BOLD,18));
        g2d.drawString("Points: " + currentPoints,5,20);


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

    public void initInput(InputMap inputMap, ActionMap actionMap) {
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0),"Left-Pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0),"Right-Pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true),"Left-Released");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true),"Right-Released");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0),"Fire");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),"Pause");

        actionMap.put("Left-Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    leftPressed = true;
                    //System.out.println("pressL");
                }
            }
        });
        actionMap.put("Right-Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    rightPressed = true;
                    //System.out.println("pressR");
                }
            }
        });
        actionMap.put("Left-Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    leftPressed = false;
                    //System.out.println("UNpressL");
                }
            }
        });
        actionMap.put("Right-Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    rightPressed = false;
                    //System.out.println("UNpressR");
                }
            }
        });
        actionMap.put("Fire", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.currentTimeMillis();
                if (gameRunning && (now - lastFireTime >= fireDelay)) {
                    lastFireTime = now;
                    Projectile p = c.fire();
                    spawnProjectile(p);

                }
            }
        });
        actionMap.put("Pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Pause click");
                if (isPaused()) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });
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
        if (powerUpEn) powerUps.clear();
        pausePanel.setVisible(false);
        paused = false;
        changeGameState(false);
    }

    public void startNewGame(int diff, boolean powerUpEn) {
        difficulty = diff;
        paused = false;
        this.powerUpEn = powerUpEn;
        spawnBomb.setDelay(1100 - 100 * difficulty);
        currentPoints = 0;
        bombs.clear();
        projectiles.clear();
        spawnBomb.start();
        if (powerUpEn) spawnPowerUp.start();
        refresh.start();
        deleteInvalidEntity.start();
        pausePanel.setVisible(false);
        changeGameState(true);
    }

    public void pauseGame() {
        paused = true;
        spawnBomb.stop();
        spawnPowerUp.stop();
        refresh.stop();
        deleteInvalidEntity.stop();
        synchronized (bombs) {
            for (Bomb b : bombs) {
                b.pauseBomb();
            }
        }
        synchronized (projectiles) {
            for (Projectile p : projectiles) {
                p.pauseProjectile();
            }
        }
        synchronized (powerUps) {
            for (PowerUp p : powerUps) {
                p.pausePowerUp();
            }
        }
        pausePanel.setVisible(true);
    }
    public void resumeGame() {
        pausePanel.setVisible(false);
        System.out.println(pausePanel.isVisible());
        synchronized (bombs) {
            for (Bomb b : bombs) {
                b.resumeBomb();
            }
        }
        synchronized (projectiles) {
            for (Projectile p : projectiles) {
                p.resumeProjectile();
            }
        }
        synchronized (powerUps) {
            for (PowerUp p : powerUps) {
                p.resumePowerUp();
            }
        }
        spawnBomb.start();
        spawnPowerUp.start();
        refresh.start();
        paused = false;
    }

    public boolean getGameRunning() {
        return gameRunning;
    }

    public void changeGameState(boolean newState) {
        boolean oldState = gameRunning;
        gameRunning = newState;

        firePropertyChange("gameRunning",oldState,gameRunning);
    }


    public void spawnProjectile(Projectile p) {
        synchronized (projectiles) {
            projectiles.add(p);
            p.start();
        }
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
        // negro usali
        for (PowerUp p : activePowerUps) {
            System.out.println(p.type);
            switch (p.type) {
                case FireRate -> {
                    Timer t = new Timer(0, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireDelay = 150;
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            fireDelay = DEF_FIRE_DELAY;
                        }
                    });
                    t.setRepeats(false);
                    t.start();
                }
                case SlowMo -> {
                    Timer t = new Timer(0, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });
                    t.setRepeats(false);
                    t.start();
                }
                case Speed -> {
                    Timer t = new Timer(0, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            c.MOVEMENT = 60;
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            c.MOVEMENT = Cannon.DEF_MOVEMENT;
                        }
                    });
                    t.setRepeats(false);
                    t.start();
                }
                case TriShot -> {
                    Timer t = new Timer(0, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // do something
                        }
                    });
                    t.setRepeats(false);
                    t.start();
                }
                case Immortal -> {
                    Timer t = new Timer(0, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireDelay = 150;
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            fireDelay = DEF_FIRE_DELAY;
                        }
                    });
                    t.setRepeats(false);
                    t.start();
                }
                case null, default -> System.out.println("Not recognized power Up");
            }
        }
    }
}
