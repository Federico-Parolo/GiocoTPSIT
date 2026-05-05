import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame g = new GameFrame();
            g.startGame();
        });
    }

}