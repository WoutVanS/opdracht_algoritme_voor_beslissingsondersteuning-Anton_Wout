import java.util.*;
public class BoxStack {
    private String name;
    private int id;
    private int x;
    private int y;
    private int capacity;
    private Stack<Box> boxes;

    //Constructors
    public BoxStack(String name, int id, int x, int y, int capacity){
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        this.boxes = new Stack<>();
    }
    public BoxStack(String name, int id, int x, int y, int capacity, Stack<Box> boxes){
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        this.boxes = boxes;
    }

    //getters and setters
    public String getName(){ return name;}
    public int getId() {
        return id;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getCapacity() {
        return capacity;
    }
    public Stack<Box> getBoxes() {
        return boxes;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setBoxes(Stack<Box> boxes) {
        this.boxes = boxes;
    }

    //METHODS
    //check if stack is full
    public boolean notFull() {
        if (boxes.size() < capacity) {
            return true;
        } else {
            return false;
        }
    }

    public void addBox(Box b) {
        boxes.push(b);
    }

    public String peekStack(){
        return boxes.peek().getId();
    }

    public int distanceToPoint(int destX, int destY){
        int deltaX = destX - this.x;
        int deltaY = destY - this.y;

        return (int)Math.sqrt(deltaX*deltaX - deltaY*deltaY);
    }

    public int SearchDepthByID(String associatedBoxId){
        for(int i = 0; i < boxes.size(); i++){
            if(boxes.get(i).getId().equals(associatedBoxId))
                return i;
        }
        return -1;
    }
}
