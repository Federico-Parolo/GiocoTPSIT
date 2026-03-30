import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        int w = 400;
        int h = 700;


        JFrame window = new JFrame("Falling Bombs");
        window.setLayout(null);
        window.setSize(new Dimension(w,h));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel p = new GamePanel(w,h);
        window.add(p);

        window.addKeyListener(p);

        window.setVisible(true);
        p.repaint();
    }

}