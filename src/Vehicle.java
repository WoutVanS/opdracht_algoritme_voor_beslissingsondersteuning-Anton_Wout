import java.awt.*;
import java.awt.image.LookupOp;
import java.sql.Array;
import java.util.Currency;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class Vehicle {
    private String name;
    private int x;
    private int y;
    private Constants.statusVehicle state;
    private int speed;
    private int id;
    private LinkedList<String> load;   //FIFO datastructure (eerste die geladen wordt is  voor eeste dest)
    private int destX;
    private int destY;
    private Location currentDest;
    private LinkedList<BoxStack> destinations;
    private int loadingCount;
    private int capacity;

    private Request currentRequest;
    private int startTime;
    private int stopTime;
    private int startX;
    private int startY;

    //constructor
    public Vehicle(int id, String name, int capacity, int speed, int x, int y) {
        this.id = id;
        this.name = name;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        state = Constants.statusVehicle.AVAILABLE;
        load = new LinkedList<String>();
        destX = 0;
        destY = 0;
        loadingCount = 0;
        currentRequest = null;
        startTime = -1;
        stopTime = -1;
    }

    //getters and setters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getName() {
        return name;
    }
    public void setName(String n) {
        name = n;
    }
    public boolean isAvailible() {
        return state == Constants.statusVehicle.AVAILABLE;
    }
    public int getSpeed() {
        return speed;
    }
    public LinkedList<String> getLoad() {
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
        this.state = Constants.statusVehicle.AVAILABLE;
    }
    public void setLoading() {
        this.state = Constants.statusVehicle.LOADING;
    }
    public void setMoving() {
        this.state = Constants.statusVehicle.MOVING;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getId(int id) {
        return id;
    }
    public void setLoad(LinkedList<String> load) {
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
    public Location getDestination() {
        return currentDest;
    };
    public void setDestinations(LinkedList<BoxStack> destinations) {
        this.destinations = destinations;
    };
    public void setCurrentRequest(Request request) {
        this.currentRequest = request;
    }
    public Request getCurrentRequest() {
        return currentRequest;
    }
    public int getCapacity() {return capacity;}

    //METHODS
    //each time unit, the vehicle moves to next position
    public void update(Requests requests) {
        if (state == Constants.statusVehicle.MOVING) {    //if vehicle is moving, that means it has a task, so it has to move
            move();
        }
        else if (state == Constants.statusVehicle.MOVINGTOPICKUP) {
            move();
        }
        else if (state == Constants.statusVehicle.UNLOADING) {    //if vehicle is loading, update the counter
            loadingCount++;
            if (loadingCount == Main.loadingDuration) {
                loadingCount = 0;
                finishDropOff(requests);
                System.out.print("load vehicle after dropoff: ");
                for (int i = 0; i < load.size(); i++) {
                    System.out.print(load.get(i) + "; ");
                }
                System.out.println(" ");
            }
        }
        else if (state == Constants.statusVehicle.LOADING) {    //if vehicle is loading, update the counter
            loadingCount++;
            if (loadingCount == Main.loadingDuration) {
                loadingCount = 0;
                finishLoading();
                System.out.print("load vehicle after loading: ");
                for (int i = 0; i < load.size(); i++) {
                    System.out.print(load.get(i) + "; ");
                }
                System.out.println(" ");
            }
        }
    }

    //function called when vehicle arrives at destination
    public void arrivedAtDest() {
        if (state == Constants.statusVehicle.MOVING) {
            handleDropOff();
        }
        else if (state == Constants.statusVehicle.MOVINGTOPICKUP) {
            handlePickup();
        }
    }

    //when the unloaded vehicle moves to its pickup location
    public void handlePickup() {
        state = Constants.statusVehicle.LOADING;
        //System.out.println("VehicleId: " + id + " is now loading");


        if(currentRequest.vehicleTakesBox()) {
            // remove boxes from stack
            System.out.println("vehicle " + id + " is loading boxes for request " + currentRequest.toString());

            // load boxes
            for (String boxId : currentRequest.getBoxIDs()) {
                load.push(boxId);
            }

            //give target coordinates to bring load to destination
            destX = currentRequest.getPlaceLocationXY()[0];
            destY = currentRequest.getPlaceLocationXY()[1];
        }else state = Constants.statusVehicle.MOVINGTOPICKUP;
    }

    public void finishLoading() {
        state = Constants.statusVehicle.MOVING;

        currentDest = currentRequest.getDropOff();

        //print the result of the dropoff operation
        //System.out.println(id + ";" + startX + ";" + startY + ";" + startTime + ";" + x + ";" + y + ";" + Main.timeCount + ";" + currentRequest.getBoxID() + ";PU");
        System.out.println("vehicleId: " + id + "; start (" + startX + ";" + startY + "); startTime: " + startTime + "; now (" + x + ";" + y + "); timecount: " + Main.timeCount + "; boxId: " + currentRequest.getBoxIDsToString() + ";PU");
        String str = "vehicleId: " + id + "; start (" + startX + ";" + startY + "); startTime: " + startTime + "; now (" + x + ";" + y + "); timecount: " + Main.timeCount + "; boxId: " + currentRequest.getBoxIDsToString() + ";PU";
        Main.outputArray.add(str);

        //set startTime and start coordinates for the PL operation
        startTime = Main.timeCount;
        startX = x;
        startY = y;
    }

    //when the loaded vehicle arrives at its destination
    public void handleDropOff() {
        state = Constants.statusVehicle.UNLOADING;
        //System.out.println("VehicleId: " + id + " arrived at destination and started unloading");

        //unload box
        if (currentDest.notFull()) {
            int size = load.size();
            for (int i = 0; i < size; i++) {
                currentDest.addBox(load.pop());
            }
            //destinations.removeFirst();
        } else {
            System.out.println("huhhh?");
        }

    //        //check if other box loaded -> load new destination (but still wait for loadingDuration to be over)
//        if (!load.isEmpty()) {
//            //pick new destination
//            currentDest = destinations.getFirst();
//            destX = currentDest.getX();
//            destY = currentDest.getY();
//        }
    }

    //set the state of the request to done and fill in the timeCount
    public void finishDropOff(Requests requests) {
        currentRequest.setStatus(Constants.statusRequest.DONE);
        currentRequest.setStopTime(Main.timeCount);

        //print the result of the dropoff operation
        //System.out.println(id + ";" + startX + ";" + startY + ";" + startTime + ";" + x + ";" + y + ";" + Main.timeCount + ";" + currentRequest.getBoxID() + ";PL");
        System.out.println("vehicleId: " + id + "; start (" + startX + ";" + startY + "); startTime: " + startTime + "; now (" + x + ";" + y + "); timecount: " + Main.timeCount + "; boxId: " + currentRequest.getBoxIDsToString() + ";PL");
        String str = "vehicleId: " + id + "; start (" + startX + ";" + startY + "); startTime: " + startTime + "; now (" + x + ";" + y + "); timecount: " + Main.timeCount + "; boxId: " + currentRequest.getBoxIDsToString() + ";PL";
        Main.outputArray.add(str);

        requests.updateRequests(currentRequest, currentDest);

        // prepare vehicle for next instruction
        currentRequest = null;
        currentDest = null;

        state = Constants.statusVehicle.AVAILABLE;
    }

    public void MoveToPickup(Request r) {
        state = Constants.statusVehicle.MOVINGTOPICKUP;
        currentRequest = r;
        //System.out.println("VehicleId: " + id + " is now moving to pickup");

        //give target coordinates
        destX = r.getPickupLocationXY()[0];
        destY = r.getPickupLocationXY()[1];

        //set the attributes for the pickup operation
        startTime = Main.timeCount;
        startX = x;
        startY = y;
    }

    public int distanceToPoint(int destX, int destY){
        int deltaX = destX - this.x;
        int deltaY = destY - this.y;

        return (int)Math.sqrt(deltaX*deltaX - deltaY*deltaY);
    }

    public void move() {
        if (destX > x) { x++; }
        else { x--; }
        if (destY > y) { y++; }
        else { y--; }

        //System.out.println("VehicleId: " + id + " Position: (" + x + "," + y +") and Destination: (" + destX + "," + destY+")" );

        if (destX == x && destY == y) { this.arrivedAtDest(); }
    }

    public void checkUpdateRequest() {      // function to check if the placelocation in the request has not changed (could be when a reallocation has happend)
        if (state == Constants.statusVehicle.MOVINGTOPICKUP) {
            destX = currentRequest.getPickupLocationXY()[0];
            destY = currentRequest.getPickupLocationXY()[1];
        }
        else if (state == Constants.statusVehicle.MOVING) {
            destX = currentRequest.getPlaceLocationXY()[0];
            destY = currentRequest.getPlaceLocationXY()[1];
        }
    }

}
