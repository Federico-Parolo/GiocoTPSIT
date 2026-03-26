public class Cannon {

    int currentX,currentY;
    int h,w;

    public Cannon(int x,int y) {
        currentX = x;
        currentY = y;
        h = 50;
        w = 50;
    }


    public Bomb fire() {
        return new Bomb(currentX,currentY);
    }
}
