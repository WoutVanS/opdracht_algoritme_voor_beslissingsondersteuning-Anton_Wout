import java.util.*;

public class BufferPoint {
    private int x;
    private int y;
    private List<Box> boxes;

    public BufferPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.boxes = new ArrayList<>();

    }

    public BufferPoint(int x, int y, Box box) {
        this.x = x;
        this.y = y;
        this.boxes = new ArrayList<>();
        this.boxes.add(box);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Box getBox(int associatedBoxId) {
        for(Box box: boxes){
            if (box.getId() == associatedBoxId){
                boxes.remove(box);
                return box;
            }
        }
        return null;
    }

    public void AddBox(Box box) {
        this.boxes.add(box);
    }
}
