import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

public class GamePanel extends JPanel{

    int difficulty = 1;
    int currentPoints = 0;
    int bombPoints = 10;
    Cannon c;
    PausePanel pausePanel;
    Timer refresh;
    Timer spawnBomb;
    Timer deleteProjectile;
    final java.util.List<Bomb> bombs;
    final java.util.List<Projectile> projectiles;
    Random r = new Random();
    int baseY = 500;
    private volatile boolean gameRunning = false;
    private boolean paused = false;


    public GamePanel(int w,int h) {
        super();
        setLayout(new BorderLayout());
        setSize(w,h);
        c = new Cannon(w/2,baseY);
        pausePanel = new PausePanel();
        bombPoints = bombPoints * difficulty;
        bombs = Collections.synchronizedList(new ArrayList<>());
        projectiles = Collections.synchronizedList(new ArrayList<>());

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
                                if (rect.intersects(new Rectangle(b.currentX,b.currentY, Bomb.width, Bomb.height)) && b.currentY > 0){
                                    toRemoveP.add(p);
                                    toRemoveB.add(b);
                                }
                            }
                        }
                    }
                    bombs.removeAll(toRemoveB);
                    projectiles.removeAll(toRemoveP);
                    currentPoints += toRemoveB.size() * bombPoints *  difficulty;
                }
                int l = bombs.size();
                bombs.removeIf(obj -> obj.currentY > baseY);
                if (l-bombs.size() != 0) {
                    resetGame();

                }
                baseY = getHeight() * 14 / 15;
                c.currentY = baseY;
                repaint();
            }
        });

        spawnBomb = new Timer(1100 - 100 * difficulty, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (bombs) {
                    Bomb b = new Bomb(r.nextInt(0,getWidth()-Bomb.width),r.nextInt(-200,0));
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

        c.drawSprite(g2d);

    }


    public void resetGame() {
        spawnBomb.stop();
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
        pausePanel.setVisible(false);
        changeGameState(true);
    }

    public void pauseGame() {
        if (paused) {
            paused = false;
            resumeGame();
        }
        paused = true;
        spawnBomb.stop();
        refresh.stop();
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
        paused = false;
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
}
