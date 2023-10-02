public class BufferPoint {
    private int x;
    private int y;
    private int capacity;
    private Box box;

    public BufferPoint(int x, int y, int capacity) {
        this.x = x;
        this.y = y;
        this.capacity = capacity;
    }

    public BufferPoint(int x, int y, int capacity, Box box) {
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        this.box = box;
    }
}
