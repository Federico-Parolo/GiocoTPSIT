public class Bomb extends Thread{

    volatile int currentX,currentY;
    int speed;
    boolean running = true;

    public Bomb(int x,int y) {
        currentX = x;
        currentY = y;
        speed = (int)(Math.random() * 10) + 1;
    }

    @Override
    public void run() {
        while (running) {
            move();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    synchronized void  move() {
        currentY += speed;
    }
}
