import java.util.ArrayList;

public class Vehicles {
    private ArrayList<Vehicle> vehicles;
    private int amountOfVehicles;

    public Vehicles(int amountOfVehicles) {
        this.amountOfVehicles = amountOfVehicles;
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public void update() {
        for (Vehicle v: vehicles) {
            v.updatePosition();
        }
    }
}
