import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GamePanel extends JPanel {

    int difficulty = 1;
    int currentPoints = 1;
    Cannon c;
    Timer refresh;
    Timer spawnBomb;
    final java.util.List<Bomb> bombs;
    Random r = new Random();
    int baseY = 500;


    public GamePanel(int width,int height) {
        super();
        setLayout(null);
        setSize(width,height);
        c = new Cannon(width/2,baseY);

        bombs = Collections.synchronizedList(new ArrayList<>());
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        refresh = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        spawnBomb = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (bombs) {
                    Bomb b = new Bomb(r.nextInt(0,width),r.nextInt(-100,100));
                    bombs.add(b);
                    b.start();
                }
            }
        });

        spawnBomb.start();
        refresh.start();
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
        g2d.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
        g2d.drawString("Points: " + currentPoints,5,20);


        // drawings every spawned bomb
        synchronized (bombs) {
            for (Bomb b : bombs) {
                if (b.currentY > baseY) {
                    bombs.remove(b);

                } else {
                    g2d.fillOval(b.currentX,b.currentY,10,10);
                }
            }
        }

        g2d.fillRect(c.currentX,c.currentY,c.w,c.h);
    }
}
