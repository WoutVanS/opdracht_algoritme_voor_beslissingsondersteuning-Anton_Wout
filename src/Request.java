import java.util.List;

public class Request {

    private int ID;
    private String pickupLocation;
    private String placeLocation;
    private String boxID;
    private Constants.statusRequest status;
    private int startTime;
    private int stopTime;
    private Box box;
    private int[] pickupLocationXY;
    private int[] placeLocationXY;
    private Location pickup;
    private Location dropOff;

    public Request(int ID, String pickupLocation, String placeLocation, String box ){
        this.ID = ID;
        this.pickupLocation = pickupLocation;
        this.placeLocation = placeLocation;
        this.boxID = box;
        status = Constants.statusRequest.WAITING;
        startTime = -1;
        stopTime = -1;
        pickupLocationXY = new int[]{-1, -1};
        placeLocationXY = new int[]{-1, -1};
        pickup = null;
        dropOff = null;
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
        pickupLocationXY = newPickup.getLocationXY();
    }
    public String getPlaceLocation() {
        return placeLocation;
    }
    public void setPlaceLocation(String placeLocation) {
        this.placeLocation = placeLocation;
    }
    public String getBoxID() {
        return boxID;
    }
    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }
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

    public void vehicleTakesBox() {
        pickup.popBox(boxID);
    }

    @Override
    public String toString() {
        return "Request{" +
                "ID=" + ID +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", placeLocation='" + placeLocation + '\'' +
                ", boxID='" + boxID + '\'' +
                '}';
    }
}
