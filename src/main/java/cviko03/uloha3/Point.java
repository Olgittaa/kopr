package cviko03.uloha3;
public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point ip) {
        this.x = ip.getX();
        this.y = ip.getY();
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized void setPositionX(int x, int y) {
        this.x = x;this.y = y;
    }
}
