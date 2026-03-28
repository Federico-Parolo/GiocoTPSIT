import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GamePanel extends JPanel {

    int difficulty = 1;
    int currentPoints = 1;
    int bombPoints = 10;
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
        bombPoints = bombPoints * difficulty;
        bombs = Collections.synchronizedList(new ArrayList<>());
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_KP_LEFT) {
                    c.currentX -= 1;
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    c.currentX += 1;
                } else if (keyCode == KeyEvent.VK_SPACE) {
                    c.fire();
                } else {
                    System.out.println("Other key pressed: " + keyCode);
                }
            }
        });


        refresh = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int l = bombs.size();
                bombs.removeIf(obj -> obj.currentY > baseY);
                currentPoints += (l - bombs.size()) * bombPoints;
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
                 b.drawSprite(g2d);
            }
        }
        c.drawSprite(g2d);
        //g2d.fillRect(c.currentX,c.currentY,c.w,c.h);
    }
}
