import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Vehicles {
    private ArrayList<Vehicle> vehicles;
    private int amountOfVehicles;

    public Vehicles(int amountOfVehicles) {
        this.amountOfVehicles = amountOfVehicles;
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    // give the closest available vehicle to the pickup location the instruction to execute
    public boolean allocateInstructionToFreeVehicle(int[] pickupLocationXY, int[] placeLocationXY, int associatedBoxId){
        List<Vehicle> availableVehicles = new ArrayList<>();                    // make a new list to add the available vehicles to
        for(Vehicle vehicle: vehicles)
            if (vehicle.isAvailible())
                availableVehicles.add(vehicle);

        if(availableVehicles.isEmpty()) return false;                           // if the list is empty that means there are no vehicles available

        int x = pickupLocationXY[0];
        int y = pickupLocationXY[1];
        availableVehicles.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));  // sort the list of available vehicles based on the distance to the pickup point

        availableVehicles.getFirst().set                                        // use the first vehicle in the sorted list
        return true;
    }
    public void update() {
        for (Vehicle v: vehicles) {
            v.updatePosition();
        }
    }
}
