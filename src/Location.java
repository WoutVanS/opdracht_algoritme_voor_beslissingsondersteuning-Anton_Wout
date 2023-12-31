import java.util.EmptyStackException;
import java.util.Stack;

public abstract class Location {
    private String name;
    private int id;
    private int x;
    private int y;
    protected Stack<String> boxes;

    public boolean isBussy;

    public Location(String name, int id, int x, int y) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        boxes = new Stack<>();
    }

    public Location(String name, int id, int x, int y, Stack<String> boxes) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.boxes = boxes;
    }

    //getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public Stack<String> getBoxes() {
        return boxes;
    }
    public void setBoxes(Stack<String> boxes) {
        this.boxes = boxes;
    }

    public void addBox(String b) {
        System.out.println("adding box to " + name + " -> " + b);
        boxes.push(b);
    }

    public int[] getLocationXY() {
        int[] res = new int[2];
        res[0] = x;
        res[1] = y;
        return res;
    }

    public boolean popBox(String boxId) {
        try {
            if (peekStack().equals(boxId)) {
                boxes.pop();
                return true;
            } else return false;
        }catch (EmptyStackException e) {
            return false;
        }

    }

    public String peekStack(){
        return boxes.peek();
    }

    public int distanceToPoint(int destX, int destY){
        int deltaX = destX - this.x;
        int deltaY = destY - this.y;

        return (int)Math.sqrt(deltaX*deltaX - deltaY*deltaY);
    }

    public int SearchDepthByID(String associatedBoxId){


        for(int i = 0; i < boxes.size(); i++){
            if(boxes.get(i).equals(associatedBoxId))
                return boxes.size() - i - 1;
        }
        return -1;
    }

    public abstract boolean notFull();

    public void printBoxes() {
        for (String boxId: boxes) {
            System.out.print(boxId + "; ");
        }
        System.out.println("");
    }
}
