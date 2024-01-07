import java.util.*;

public class BufferPoint extends Location{

    public BufferPoint(String name, int id, int x, int y) {
        super(name, id, x, y);
    }
    public BufferPoint(String name, int id, int x, int y, Stack<String> boxes) {
        super(name, id, x, y, boxes);
    }

    public boolean notFull() {
        return true;
    }

    public boolean popBox(String boxId) {
        if (boxes.contains(boxId)) {
            boxes.remove(boxId);
            return true;
        } else return false;
    }
}

//    private int id;
//    private String name;
//    private int x;
//    private int y;
//    private List<Box> boxes;
//
//    public BufferPoint(int id, String name, int x, int y) {
//        this.id = id;
//        this.name = name;
//        this.x = x;
//        this.y = y;
//        this.boxes = new ArrayList<>();
//
//    }
//
//    public BufferPoint(int id, String name, int x, int y, Box box) {
//        this.id = id;
//        this.name = name;
//        this.x = x;
//        this.y = y;
//        this.boxes = new ArrayList<>();
//        this.boxes.add(box);
//    }
//
//    //getters and setters
//    public int getX() {
//        return x;
//    }
//    public void setX(int x) {
//        this.x = x;
//    }
//    public int getY() {
//        return y;
//    }
//    public void setY(int y) {
//        this.y = y;
//    }
//    public int getId() {
//        return id;
//    }
//    public void setId(int id) {
//        this.id = id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Box getBox(int associatedBoxId) {
//        for(Box box: boxes){
//            if (box.getId().equals(associatedBoxId)){
//                boxes.remove(box);
//                return box;
//            }
//        }
//        return null;
//    }
//
//    public void AddBox(Box box) {
//        this.boxes.add(box);
//    }

