
import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class Network {
    private Allocator allocator;
    private BoxStacks boxStacks;
    private Vehicles vehicles;
    private BufferPoints bufferPoints;

    private Requests requests;


    //constructor
    public Network(Allocator allocator, BoxStacks boxStacks, Vehicles vehicles, BufferPoints bufferPoints) {
        this.allocator = allocator;
        this.boxStacks = boxStacks;
        this.vehicles = vehicles;
        this.bufferPoints = bufferPoints;
    }

    public Network(Allocator allocator, BoxStacks boxStacks, Vehicles vehicles, BufferPoints bufferPoints, Requests requests) {
        this.allocator = allocator;
        this.boxStacks = boxStacks;
        this.vehicles = vehicles;
        this.bufferPoints = bufferPoints;
        this.requests = requests;
    }

    // run methode the main of network. It is responsible for handling the instructions from the requestList
    // it instructs vehicles what to do.
    public void run(){
            while(!requests.isEmpty()){
                Request request = requests.getNextRequest();
                String pickupLocationName = request.getPickupLocation();               // search for the ID number in the hashmap
                String placeLocationName = request.getPlaceLocation();
                String associatedBoxId = request.getBoxID();

                System.out.println("current instruction: " + request);

                if(!checkBoxLocationInPickupLocation(pickupLocationName, associatedBoxId)){         // checks if the Box is in the pickuplocation and if it sits on top
                                                                                                    // Execute the reallocation algorithm so to associated Box Id gets to the top
                }

                if(!checkPlaceLocation(placeLocationName)){                                         // checks if the boxstack is not full
                    placeLocationName = allocator.findEmptySpace(boxStacks, placeLocationName);     // Search for an empty stack

                }


                if(pickupLocationName != null && placeLocationName != null)                             // allocate a vehicle to pick up the box if the pickup and place location are valid
                    allocateInstructionToVehicle(pickupLocationName, placeLocationName, associatedBoxId);
                else
                    System.err.println("there whas an error that prevent the allocation of the instruction to a vehicle");
            }

    }


    // this methode checks if the box is the first box in the pickup location and thus can be pickup up directly
    public Boolean checkBoxLocationInPickupLocation(String pickupLocationName, String associatedBoxId){
        if(bufferPoints.isBufferPoint(pickupLocationName)){                             // (if the pickup location is a bufferpoint then a box should first be added.)
            BufferPoint bp = bufferPoints.getBufferPointByName(pickupLocationName);     // get the correct bufferpoint
            addBoxToBufferPoint(associatedBoxId, bp);                                   // add box to the bufferpoint
            return true;
        }
        else{
            return boxStacks.getTopBoxIdOfBoxStack(pickupLocationName).equals(associatedBoxId);
        }
    }

    //this methode checks if the place location is not full
    public Boolean checkPlaceLocation(String placeLocationName){
        return bufferPoints.isBufferPoint(placeLocationName) || boxStacks.checkBoxStackNotFull(placeLocationName);
    }

    // Methode that adds the new box to the bufferPoint
    public void addBoxToBufferPoint(String associatedBoxId, BufferPoint bp){
        Box box = new Box(associatedBoxId);
        bp.AddBox(box);
    }

    // this function gets the x and y coordinates of the pickup and place location and gives them to the vehicles object
    public void allocateInstructionToVehicle(String pickupLocationName, String placeLocationName, String associatedBoxId) {

        int[] pickupLocationXY = (bufferPoints.isBufferPoint(pickupLocationName)) ? bufferPoints.getXY(pickupLocationName) : boxStacks.getXY(pickupLocationName);
        int[] placeLocationXY = (bufferPoints.isBufferPoint(placeLocationName)) ?  bufferPoints.getXY(placeLocationName) : boxStacks.getXY(placeLocationName);

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
