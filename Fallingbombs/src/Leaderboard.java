import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;

public class Leaderboard extends JPanel {

    String filePath = "leaderboard.txt";
    private String[][] leaderboard; // associates name with points
    BufferedReader br;
    BufferedWriter bw;
    Button backButton;
    int startX = 30,startY = 100;

    public Leaderboard(int w, int h) {
        setSize(w,h);
        setLayout(null);
        if (!readLeaderboard()) {
            writeLeaderboard();
        }
        backButton = new Button("<");
        backButton.setBounds(0,0,50,50);
        add(backButton);
    }


    // writes the top 10 players
    public void writeLeaderboard() {
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            if (leaderboard == null) return;
            for (int i = 0; i < 10; i++) {
                bw.append(leaderboard[i][0]);
                bw.append("-");
                bw.append(leaderboard[i][1]);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Writing error");
        }

    }

    public boolean readLeaderboard() {
        leaderboard = new String[10][2];
        for (int i = 0; i < leaderboard.length; i++) {
            leaderboard[i] = new String[2];
        }
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            int lineCount = 0;
            while (lineCount < 9) {
                line = br.readLine();
                if (line == null || line.isBlank()) break;
                String[] entry = line.split("-");
                leaderboard[lineCount][0] = entry[0];
                leaderboard[lineCount][1] = entry[1];
                lineCount++;
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error in reading the leaderboard.txt file");
            return false;
        }
    }

    public void updateLeaderboard(String name, String points) {
        for (int i = 0; i < leaderboard.length; i++) {
            if (leaderboard[i][0] == null || leaderboard[i][1] == null || leaderboard[i][0].equals("null") || leaderboard[i][1].equals("null")) {
                leaderboard[i][0] = name;
                leaderboard[i][1] = points;
                break;
            } else {
                if (Integer.parseInt(points) >= Integer.parseInt(leaderboard[i][1])) {
                    String tempPoints = leaderboard[i][1];
                    String tempName = leaderboard[i][0];
                    leaderboard[i][1] = points;
                    leaderboard[i][0] = name;
                    name = tempName;
                    points = tempPoints;
                }
            }
        }
        System.out.println(Arrays.deepToString(leaderboard));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(20,20,40));
        g2d.fillRect(0,0,getWidth(),getHeight());

        g2d.setColor(new Color(200,200,200));
        g2d.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));
        FontMetrics fm = g2d.getFontMetrics();

        // text content
        String s = "Name    Points";
        g2d.drawString(s,getWidth()/2 - fm.stringWidth(s)/2,startY);


        g2d.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));

        for (int i = 0; i < leaderboard.length; i++) {
            if (leaderboard[i][0] == null || leaderboard[i][1] == null) break;
            String line = leaderboard[i][0] + "    " + leaderboard[i][1];
            int textHeight = fm.getAscent();
            g2d.drawString(line, startX, startY + textHeight * i + 80);
        }
    }

    public void getLeaderboard() {
        for (int i = 0; i < leaderboard.length; i++) {
            for (int j = 0; j < leaderboard[i].length; j++) {
                System.out.print(leaderboard[i][j] + " ");
            }
            System.out.println();
        }
    }



    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(400,700);
        f.setLocationRelativeTo(null);
        Leaderboard l = new Leaderboard(400,700);
        f.add(l);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        l.getLeaderboard();
    }
}
