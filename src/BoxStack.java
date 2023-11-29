import java.util.*;
public class BoxStack extends Location{
    private int capacity;

    //Constructors
    public BoxStack(String name, int id, int x, int y, int capacity){
        super(name, id, x, y);
        this.capacity = capacity;
    }

    public BoxStack(String name, int id, int x, int y, Stack<String> stack, int capacity){
        super(name, id, x, y, stack);
        this.capacity = capacity;
    }

        //check if stack is full
    public boolean notFull() {
        if (boxes.size() < capacity) {
            return true;
        } else {
            return false;
        }
    }

    public int freeSpace(){
        return capacity - boxes.size();
    }

//    public BoxStack(String name, int id, int x, int y, int capacity, Stack<String> boxes){
//        this.name = name;
//        this.id = id;
//        this.x = x;
//        this.y = y;
//        this.capacity = capacity;
//        this.boxes = boxes;
//    }
//
//    //getters and setters
//    public String getName(){ return name;}
//    public int getId() {
//        return id;
//    }
//    public int getX() {
//        return x;
//    }
//    public int getY() {
//        return y;
//    }
//    public int getCapacity() {
//        return capacity;
//    }
//    public Stack<String> getBoxes() {
//        return boxes;
//    }
//    public void setId(int id) {
//        this.id = id;
//    }
//    public void setX(int x) {
//        this.x = x;
//    }
//    public void setY(int y) {
//        this.y = y;
//    }
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//    public void setBoxes(Stack<String> boxes) {
//        this.boxes = boxes;
//    }
//
//    //METHODS
//    //check if stack is full
//    public boolean notFull() {
//        if (boxes.size() < capacity) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void addBox(String b) {
//        boxes.push(b);
//    }
//
//    public String peekStack(){
//        return boxes.peek();
//    }
//
//    public int distanceToPoint(int destX, int destY){
//        int deltaX = destX - this.x;
//        int deltaY = destY - this.y;
//
//        return (int)Math.sqrt(deltaX*deltaX - deltaY*deltaY);
//    }
//
//    public int SearchDepthByID(String associatedBoxId){
//        for(int i = 0; i < boxes.size(); i++){
//            if(boxes.get(i).equals(associatedBoxId))
//                return i;
//        }
//        return -1;
//    }
}
