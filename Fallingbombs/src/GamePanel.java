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
    Timer refresh;
    Timer spawnBomb;
    Timer deleteProjectile;
    final java.util.List<Bomb> bombs;
    final java.util.List<Projectile> projectiles;
    Random r = new Random();
    int baseY = 500;
    private volatile boolean gameRunning = false;


    public GamePanel(int w,int h) {
        super();
        setLayout(null);
        setSize(w,h);
        c = new Cannon(w/2,baseY);
        bombPoints = bombPoints * difficulty;
        bombs = Collections.synchronizedList(new ArrayList<>());
        projectiles = Collections.synchronizedList(new ArrayList<>());

        refresh = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int l = bombs.size();
                bombs.removeIf(obj -> obj.currentY > baseY);
                currentPoints += (l - bombs.size()) * bombPoints;
                if (l-bombs.size() != 0) {
                    resetGame();

                }
                repaint();
            }
        });

        spawnBomb = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (bombs) {
                    Bomb b = new Bomb(r.nextInt(0,w),r.nextInt(-100,100));
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
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(100,255,255));
        g2d.fillRect(0,0,getWidth(),getHeight());


        g2d.setColor(new Color(25,200,25));
        g2d.fillRect(0,500,getWidth(),getHeight());

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

        currentPoints = 0;
        bombs.clear();
        changeGameState(false);
    }

    public void startNewGame() {
        currentPoints = 0;
        bombs.clear();
        spawnBomb.start();
        refresh.start();
        changeGameState(true);
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
