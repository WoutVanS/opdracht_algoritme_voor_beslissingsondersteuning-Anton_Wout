import java.util.List;

public class Request {
        private int ID;
        private String pickupLocation;
        private String placeLocation;
        private String boxID;

        public Request(int ID, String pickupLocation, String placeLocation, String boxID ){
            this.ID = ID;
            this.pickupLocation = pickupLocation;
            this.placeLocation = placeLocation;
            this.boxID = boxID;
        }

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
