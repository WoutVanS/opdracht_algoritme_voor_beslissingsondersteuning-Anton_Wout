
import java.io.*;
import java.util.Arrays;
import java.util.Optional;

public class Network {
    private Allocator allocator;
    private BoxStacks boxStacks;
    private Vehicles vehicles;
    private final BufferPoint bufferPoint;

    //constructor
    public Network(Allocator allocator, BoxStacks boxStacks, Vehicles vehicles, BufferPoint bufferPoint) {
        this.allocator = allocator;
        this.boxStacks = boxStacks;
        this.vehicles = vehicles;
        this.bufferPoint = bufferPoint;
    }

    // run methode the main of network. It is responsible for handling the instructions in the CSV file.
    // it instructs vehicles what to do.
    public void run(String csvFile, String csvDelimiter){
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) { // opening the CSV file
            while ((line = br.readLine()) != null) {                            // keep going while there are new instructions
                String[] data = line.split(csvDelimiter);
                int pickupLocationId = Integer.parseInt(data[0]);               // this is the id number of the location where the box should be picked up
                int placeLocationId = Integer.parseInt(data[1]);                // this is the id number of the location where the box should be dropped off
                int associatedBoxId = Integer.parseInt(data[2]);                // the id of the box associated with this instruction

                System.out.println("current instruction: " + Arrays.toString(data));

                if(!checkBoxLocationInPickupLocation(pickupLocationId, associatedBoxId)){
                    // Execute the reallocation algorithm so to associated Box Id gets to the top
                }

                if(!checkPlaceLocation(placeLocationId)){
                    // Search for an empty stack
                    placeLocationId = allocator.findEmptySpace(boxStacks, placeLocationId);

                }

                // allocate a vehicle to pick up the box if the pickup and place location are valid
                if(pickupLocationId != -1 && placeLocationId != -1)
                    allocateInstructionToVehicle(pickupLocationId, placeLocationId, associatedBoxId);
                else
                    System.err.println("there whas an error that prevent the allocation of the instruction to a vehicle");
            }

        } catch (IOException e) {
            System.err.println("error while loading the instrutions CSV file: " + e);
        }
    }


    // this methode checks if the box is the first box in the pickup location and thus can be pickup up directly
    public Boolean checkBoxLocationInPickupLocation(int pickupLocationId, int associatedBoxId){
        if(pickupLocationId == Constants.BUFFER_POINT_ID){                      // (if the pickup location is the bufferpoint then a box should first be added.)
            addBoxToBufferPoint(associatedBoxId);
            return true;
        }
        else{
            return boxStacks.getTopBoxIdOfBoxStack(pickupLocationId) == associatedBoxId;
        }
    }

    //this methode checks if the place location is not full
    public Boolean checkPlaceLocation(int placeLocationId){
        return placeLocationId == Constants.BUFFER_POINT_ID || boxStacks.checkBoxStackNotFull(placeLocationId);
    }

    // Methode that adds the new box to the bufferPoint
    public void addBoxToBufferPoint(int associatedBoxId){
        Box box = new Box(associatedBoxId);
        this.bufferPoint.AddBox(box);
    }

    // this function gets the x and y coordinates of the pickup and place location and gives them to the vehicles object
    public void allocateInstructionToVehicle(int pickupLocationId, int placeLocationId, int associatedBoxId){

        int[] pickupLocationXY = (pickupLocationId == Constants.BUFFER_POINT_ID) ?  new int[]{bufferPoint.getX(), bufferPoint.getY()} : boxStacks.getXY(pickupLocationId);
        int[] placeLocationXY = (placeLocationId == Constants.BUFFER_POINT_ID) ?  new int[]{bufferPoint.getX(), bufferPoint.getY()} : boxStacks.getXY(placeLocationId);

        vehicles.allocateInstructionToFreeVehicle(pickupLocationXY, placeLocationXY, associatedBoxId);
     }

    //getters and setters
    public Allocator getAllocator() {
        return allocator;
    }
    public void setAllocator(Allocator allocator) {
        this.allocator = allocator;
    }
    public BoxStacks getBoxStacks() {
        return boxStacks;
    }
    public void setBoxStacks(BoxStacks boxStacks) {
        this.boxStacks = boxStacks;
    }
    public Vehicles getVehicles() {
        return vehicles;
    }
    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }

    //methodes
    public void updateVehicles(){
        vehicles.update();
    }



}
