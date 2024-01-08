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

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getAmountOfVehicles() {
        return amountOfVehicles;
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

    public boolean boxesAlreadyInProgress(Request request) {        // function to check if a vehicle is already moving boxes (to avoid a multiple vehicles trying to move the same box)
        for (Vehicle v: vehicles) {
            for (String boxId: request.getBoxIDs()) {
                if (!v.isAvailible() && v.getCurrentRequest().getBoxIDs().contains(boxId)) return true;
            }
        }
        return false;
    }

    public boolean movingVehicleCloser(Vehicle vehicle, Request request){

        int distanceClosestVehicle = Integer.MAX_VALUE;
        for(Vehicle v : vehicles){
            if(!v.isAvailible() && v.getCurrentRequest().getDropOff().equals(request.getPickup())) {
                int temp = v.distanceToDropOff();
                distanceClosestVehicle = Integer.min(temp, distanceClosestVehicle);
            }
        }

        return distanceClosestVehicle > request.getPickup().distanceToPoint(vehicle.getX(), vehicle.getY());
    }

    public boolean aVehicleMovingToLocation(Location location){

        for(Vehicle v : vehicles){
            if(!v.isAvailible() && v.getCurrentRequest().getDropOff().getName().equals(location.getName()))
                return true;
        }
        return false;
    }

    // give the closest available vehicle to the pickup location the instruction to execute
    public Request allocateInstructionToFreeVehicle(Request request){
        ArrayList<Vehicle> availableVehicles = findAllAvailableVehicles();

        if(availableVehicles.isEmpty()) return null;                           // if the list is empty that means there are no vehicles available

        int x = request.getPickupLocationXY()[0];
        int y = request.getPickupLocationXY()[1];
        availableVehicles.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));  // sort the list of available vehicles based on the distance to the pickup point

        Vehicle availableVehicle = availableVehicles.get(0);

        Request splittedRequest = null;
        if(request.getBoxIDs().size() > availableVehicle.getCapacity()){                // check if the vehicle can carry all the boxses from the request. if not make a new request.
//            System.out.println("\n---------- dividing request ------------");
//            System.out.println("number of boxes in request: " + request.getBoxIDs().size());
//            System.out.println("capacity of vehicle: " + availableVehicle.getCapacity());
//            System.out.println("free vehicles: " + availableVehicles.size());


            int newID = request.getID() * 100 + 1;                                      // add 001 to the new request to indicate that it's been divided

            List<String> beforeIndex = request.getBoxIDs().subList(0, availableVehicle.getCapacity());
            List<String> afterIndex = request.getBoxIDs().subList(availableVehicle.getCapacity(), request.getBoxIDs().size());

//            System.out.println("request Box list for vehicle: " + beforeIndex.toString());
//            System.out.println("new request Box list: " + afterIndex.toString());

            request.setBoxIDs(new ArrayList<>(beforeIndex));
            splittedRequest = new Request(newID, request.getPickup(), request.getDropOff(), new ArrayList<>(afterIndex));

            System.out.println("----------------------------------------");
        }
            availableVehicle.MoveToPickup(request);                                       // use the first vehicle in the sorted list
            return splittedRequest;
    }


    public boolean updateVehicles(Requests requests) {
        boolean vehiclesWorking = false;
        for (Vehicle v: vehicles) {
            v.update(requests);
            v.checkUpdateRequest();
            if(!v.isAvailible()) vehiclesWorking = true;
        }
        return  vehiclesWorking;
    }

    public void printStatusVehicles() {
        for (Vehicle v: vehicles) {
            if (v.getState() == Constants.statusVehicle.MOVING) {    //if vehicle is moving, that means it has a task, so it has to move
                System.out.println("Vehicle " + v.getId() + " is currently moving to destination: " + v.getDestination().getName() + " with load: " + v.getLoadToString());
                System.out.println("-> coordinates: (" + v.getX() + "," + v.getY() + ")");
                System.out.println("-> current request for vehicle: " + v.getCurrentRequest().toString());
            }
            else if (v.getState() == Constants.statusVehicle.MOVINGTOPICKUP) {
                System.out.println("Vehicle " + v.getId() + " is currently moving to pickup: " + v.getDestination().getName() + " with load: " + v.getLoadToString());
                System.out.println("-> coordinates: (" + v.getX() + "," + v.getY() + ")");
                System.out.println("-> current request for vehicle: " + v.getCurrentRequest().toString());
            }
            else if (v.getState() == Constants.statusVehicle.UNLOADING) {    //if vehicle is loading, update the counter
                System.out.println("Vehicle " + v.getId() + " is currently unloading load: " + v.getLoadToString());
                System.out.println("-> coordinates: (" + v.getX() + "," + v.getY() + ")");
                System.out.println("-> current request for vehicle: " + v.getCurrentRequest().toString());
            }
            else if (v.getState() == Constants.statusVehicle.LOADING) {    //if vehicle is loading, update the counter
                System.out.println("Vehicle " + v.getId() + " is currently loading: " + v.getLoadToString());
                System.out.println("-> coordinates: (" + v.getX() + "," + v.getY() + ")");
                System.out.println("-> current request for vehicle: " + v.getCurrentRequest().toString());
            }
        }
    }
}
