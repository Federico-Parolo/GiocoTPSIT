import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame g = new GameFrame();
            g.startGame();
        });
    }

}