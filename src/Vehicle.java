import java.sql.Array;
import java.util.LinkedList;

public class Vehicle {
    private int x;
    private int y;
    private boolean availible;
    private int speed;
    private int id;
    private LinkedList<Box> load;   //FIFO datastructure (eerste die geladen wordt is  voor eeste dest)
    private int destX;
    private int destY;
    private BoxStack currentDest;
    private LinkedList<BoxStack> destinations;

    //constructor
    public Vehicle(int id, int speed, int x, int y) {
        this.id = id;
        this.speed = speed;
        this.x = x;
        this.y = y;
        availible = true;
        load = new LinkedList<Box>();
        destX = 0;
        destY = 0;
    }

    //getters and setters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isAvailible() {
        return availible;
    }
    public int getSpeed() {
        return speed;
    }
    public LinkedList<Box> getLoad() {
        return load;
    }
    public int getDestY() {
        return destY;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setAvailible(boolean availible) {
        this.availible = availible;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getId(int id) {
        return id;
    }
    public void setLoad(LinkedList<Box> load) {
        this.load = load;
    }
    public void setDestX(int destX) {
        this.destX = destX;
    }
    public void setDestY(int destY) {
        this.destY = destY;
    }
    public void setDestination(BoxStack destination) {
        this.currentDest = destination;
    };
    public BoxStack getDestination() {
        return currentDest;
    };
    public void setDestinations(LinkedList<BoxStack> destinations) {
        this.destinations = destinations;
    };


    //METHODS
    //each time unit, the vehicle moves to next position
    public void updatePosition() {
        if (!availible) {    //if vehicle is not available, that means it has a task, so it has to move
            if (destX > x) { x++; }
            else { x--; }
            if (destY > y) { y++; }
            else { y--; }

            System.out.println("VehicleId: " + id + " Position: (" + x + "," + y +") and Destination: (" + destX + "," + destY+")" );

            if (destX == x && destY == y) { this.arrivedAtDest(); }
        }
    }

    //function called when vehicle arrives at destination
    public void arrivedAtDest() {
        System.out.println("VehicleId: " + id + " arrived at destination");

        //unload box
        if (currentDest.notFull()) {
            currentDest.addBox(load.removeFirst());
            destinations.removeFirst();
        }
        else {
            System.out.println("huhhh?");
        }

        //check if other box loaded -> load new destination
        if (!load.isEmpty()) {
            //pick new destination
            currentDest = destinations.getFirst();
            destX = currentDest.getX();
            destY = currentDest.getY();
        }
        else {
            //if not, set available
            availible = true;
        }
    }


}
