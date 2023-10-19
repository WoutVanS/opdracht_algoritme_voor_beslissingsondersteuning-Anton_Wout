import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Vehicles {
    private ArrayList<Vehicle> vehicles;
    private int amountOfVehicles;

    public Vehicles() {
        vehicles = new ArrayList<Vehicle>();
    }

    public Vehicles(int amountOfVehicles) {
        vehicles = new ArrayList<Vehicle>();
        this.amountOfVehicles = amountOfVehicles;
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public ArrayList<Vehicle> findAllAvailableVehicles() {
        ArrayList<Vehicle> res = new ArrayList<>();
        for (Vehicle v: vehicles) {
            if (v.isAvailible()) {
                res.add(v);
            }
        }
        return res;
    }

    // give the closest available vehicle to the pickup location the instruction to execute
    public void allocateInstructionToFreeVehicle(Request r){
        ArrayList<Vehicle> availableVehicles = findAllAvailableVehicles();

        if(availableVehicles.isEmpty()) return;                           // if the list is empty that means there are no vehicles available

        int x = r.getPickupLocationXY()[0];
        int y = r.getPickupLocationXY()[1];
        availableVehicles.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));  // sort the list of available vehicles based on the distance to the pickup point

        availableVehicles.get(0).MoveToPickup(r);                                       // use the first vehicle in the sorted list
    }

    public void updateVehicles() {
        for (Vehicle v: vehicles) {
            v.update();
        }
    }
}
