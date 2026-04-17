import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GamePanel extends JPanel{

    int difficulty = 1;
    int currentPoints = 0;
    int bombPoints = 10;
    Cannon c;
    PausePanel pausePanel;
    private Timer refresh;
    private Timer spawnBomb;
    private Timer deleteProjectile;
    private Timer spawnPowerUp;
    private final java.util.List<Bomb> bombs;
    private final java.util.List<Projectile> projectiles;
    private final java.util.List<Explosion> explosions;
    Random r = new Random();
    private int baseY = 500;
    private volatile boolean gameRunning = false;
    private boolean paused;


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

        refresh = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Bomb> toRemoveB = new ArrayList<>();
                ArrayList<Projectile> toRemoveP = new ArrayList<>();
                synchronized (projectiles) {
                    for (Projectile p : projectiles) {
                        synchronized (bombs) {
                            Rectangle rect = new Rectangle(p.currentX,p.currentY,p.width,p.height);
                            for (Bomb b : bombs) {
                                if (rect.intersects(new Rectangle(b.currentX,b.currentY, Bomb.WIDTH, Bomb.HEIGHT)) && b.currentY > 0){
                                    toRemoveP.add(p);
                                    toRemoveB.add(b);
                                    explosions.add(new Explosion(Explosion.COLLISION,p.currentX,p.currentY));
                                }
                            }
                        }
                    }
                    bombs.removeAll(toRemoveB);
                    projectiles.removeAll(toRemoveP);
                    currentPoints += toRemoveB.size() * bombPoints *  difficulty;
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

        deleteProjectile = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (projectiles) {
                    projectiles.removeIf(obj -> obj.currentY < 0);
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

        c.drawSprite(g2d);

    }


    public void resetGame() {
        spawnBomb.stop();
        deleteProjectile.stop();
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
        bombs.clear();
        projectiles.clear();
        pausePanel.setVisible(false);
        paused = false;
        changeGameState(false);
    }

    public void startNewGame(int diff) {
        difficulty = diff;
        paused = false;
        spawnBomb.setDelay(1100 - 100 * difficulty);
        currentPoints = 0;
        bombs.clear();
        projectiles.clear();
        spawnBomb.start();
        refresh.start();
        deleteProjectile.start();
        pausePanel.setVisible(false);
        changeGameState(true);
    }

    public void pauseGame() {
        paused = true;
        spawnBomb.stop();
        refresh.stop();
        deleteProjectile.stop();
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
        spawnBomb.start();
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
}
