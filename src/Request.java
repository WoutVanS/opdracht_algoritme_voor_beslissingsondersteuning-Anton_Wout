import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Request {

    private int ID;
    private String pickupLocation;
    private String placeLocation;
    private String boxID;

    private ArrayList<String> boxIDs;
    private Constants.statusRequest status;
    private int startTime;
    private int stopTime;
    private Box box;
    private int[] pickupLocationXY;
    private int[] placeLocationXY;
    private Location pickup;
    private Location dropOff;

    public Request(int ID, Location pickup, Location place, String box ){
        this.ID = ID;
        this.pickupLocation = pickup.getName();
        this.placeLocation = place.getName();
        this.boxID = box;
        status = Constants.statusRequest.WAITING;
        startTime = -1;
        stopTime = -1;
        pickupLocationXY = new int[]{-1, -1};
        placeLocationXY = new int[]{-1, -1};
        this.pickup = pickup;
        dropOff = place;
        this.boxIDs = new ArrayList<>();
        boxIDs.add(box);
    }

    public Request(int ID, Location pickup, Location place, ArrayList<String> boxes) {
        this.ID = ID;
        this.pickupLocation = pickup.getName();
        this.placeLocation = place.getName();
        this.boxIDs = boxes;
        status = Constants.statusRequest.WAITING;
        startTime = -1;
        stopTime = -1;
        pickupLocationXY = new int[]{-1, -1};
        placeLocationXY = new int[]{-1, -1};
        this.pickup = pickup;
        dropOff = place;
    }

    //getters and setters
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getPickupLocation() {
        return pickupLocation;
    }
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
        Location newPickup = Main.locations.get(pickupLocation);
        pickup = newPickup;
        System.out.println(pickup);
        pickupLocationXY = newPickup.getLocationXY();
    }
    public String getPlaceLocation() {
        return placeLocation;
    }
    public void setPlaceLocation(String placeLocation) {
        this.placeLocation = placeLocation;
    }
//    public String getBoxID() {
//        return boxID;
//    }
//    public void setBoxID(String boxID) {
//        this.boxID = boxID;
//    }
    public void setStatus(Constants.statusRequest newStatus) {
        status = newStatus;
    }
    public Constants.statusRequest getStatus() {
        return status;
    }
    public void setStartTime(int start) {
        if (status == Constants.statusRequest.INPROGRESS) {
            startTime = start;
        }
    }
    public void setStopTime(int stop) {
        if (status == Constants.statusRequest.DONE) {
            stopTime = stop;
        }
    }
    public void setPickupLocationXY(int[] pickupLocationXY) {
        this.pickupLocationXY = pickupLocationXY;
    }
    public int[] getPickupLocationXY() {
        return pickupLocationXY;
    }
    public int[] getPlaceLocationXY() {
        return placeLocationXY;
    }
    public void setPlaceLocationXY(int[] placeLocationXY) {
        this.placeLocationXY = placeLocationXY;
    }
    public Location getPickup() {
        return pickup;
    }
    public void setPickup(Location pickup) {
        this.pickup = pickup;
    }
    public Location getDropOff() {
        return dropOff;
    }
    public void setDropOff(Location dropOff) {
        this.dropOff = dropOff;
    }
    public String getBoxIDsToString() {
        StringBuilder sb = new StringBuilder();
        for (String boxId: boxIDs) {
            sb.append(boxId).append(", ");
        }
        return sb.toString();
    }

    public void setBoxIDs(ArrayList<String> boxIDs) {this.boxIDs = boxIDs;}

    public ArrayList<String> getBoxIDs() {
        return boxIDs;
    }
    public void vehicleTakesBox() {
        for (String boxID: boxIDs) {
            pickup.popBox(boxID);
        }
    }
    public String getBoxID() {
        return boxID;
    }
    public String printBoxIds() {
        if (boxIDs.isEmpty()) return "";
        else {
            StringBuilder sb = new StringBuilder();
            for (String boxId: boxIDs) {
                sb.append(boxId).append("; ");
            }
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "ID=" + ID +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", placeLocation='" + placeLocation + '\'' +
                ", boxIDs='" + printBoxIds() + '\'' +
                '}';
    }
}
