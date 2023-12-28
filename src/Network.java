
import java.io.*;
import java.nio.Buffer;
import java.util.*;

// uitbreiding: requests preprocessen -> requests samen poolen om zo de capaciteit van vehicle vol te benutten

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

    //OPM ik zou dit anders aanpakken, elke loop van de while in main (dus een tijdseenheid) kijk je welke vehicles vrij zijn genkomen
    //en je voert het aantal requests uit dat er vrije vehicles zijn

    public boolean run(){
        ArrayList<Vehicle> availableVehicles = vehicles.findAllAvailableVehicles();
        if (!requests.isEmpty()) {
            for (int i = 0; i < availableVehicles.size(); i++) {
                Request request = requests.getNextRequest();

                if (request.getPickup() == request.getDropOff()) {            // sometimes a bad request is formed whith the same placelocation and destinationlocation -> skip these requests
                    continue;
                }

//                int counter = 1;
//                while (vehicles.boxesAlreadyInProgress(request)) {          // when another vehicle is already busy with the boxes in the request, we best wait untill he is done
//                    Request temp = request;                                 // because when we move the boxes before he arrives, this will cause an error
//                    request = requests.getNextRequest();
//                    requests.addAtIndex(counter, temp);
//                    counter++;
//                    System.out.println(counter);
//                }

                String pickupLocationName = request.getPickupLocation();               // search for the ID number in the hashmap
                String placeLocationName = request.getPlaceLocation();
                String associatedBoxId = request.getBoxIDs().get(0);

                Location dropOff = request.getDropOff();


                System.out.println("\ncurrent instruction: " + request);

                if (dropOff.getBoxes().size() == Main.stackcapacity && dropOff instanceof BoxStack) {    // destination boxstack is full -> reallocate box from destiantion to make space
                    // check if top boxes of dropoff is from incoming request, if not reallocate them
                    ArrayList<String> reallocate = new ArrayList<>();
                    int counter = 0;
                    int j = 0;
                    while (counter <= request.getBoxIDs().size()-1 && j < dropOff.getBoxes().size()) {      // find the boxes to reallocate
                        if (!Main.incomingBoxes.contains(dropOff.getBoxes().get(j))) {
                            reallocate.add(dropOff.getBoxes().get(j));
                            counter++;
                        }
                        j++;
                    }
                    System.out.println("REALOCATE BECAUSE BOXSTACK IS FULL");
                    // forge requests for the boxes to reallocate
//                    if (reallocate.size() > 1) {
                        List<Request> requestList = allocator.realocationAlgorithm(boxStacks, dropOff.getName(), reallocate.get(reallocate.size() - 1));
                        requestList.add(request);
                        requests.addInfront(requestList);
//                    } else {
//                        String newDropoff = allocator.findEmptySpace(boxStacks, placeLocationName);
//                        int ID = 6669000 + request.getID();
//                        Request newRequest = new Request(ID, request.getDropOff(), Main.locations.get(newDropoff), reallocate);
//                        requests.addInfront(request);
//                        requests.addInfront(newRequest);
//                    }
                    break;
                }


                if (request.getStatus() != Constants.statusRequest.INPROGRESS && !checkBoxLocationInPickupLocation(pickupLocationName, associatedBoxId)) {         // checks if the Box is in the pickuplocation and if it sits on top
                    List<Request> requestList = allocator.realocationAlgorithm(boxStacks, pickupLocationName, associatedBoxId);
                    System.out.print("IDs ReallocationRequests: ");
                    for (Request r: requestList) {
                        System.out.print(r.getID() + ", ");
                    }
                    request.setStatus(Constants.statusRequest.INPROGRESS);
                    requestList.add(request);

                    int indexOringinal = requestList.size()-1;
                    // enkel doen als bufferpoint involved is in request (anders is request zelf een reallocation)
                    if ((request.getPickup() instanceof BufferPoint) || (request.getDropOff() instanceof BufferPoint)) {
                        List<Request> reallocRequests = new ArrayList<>();
                        for (Request req: requestList) {
                            reallocRequests.add(req);
                        }
                        reallocRequests.remove(indexOringinal);
                        List<Request> mirrorRequests = allocator.mirrorRealocatedRequests(reallocRequests);  // to put the realocated requests back on their original place
                        for (Request mirrorRequest : mirrorRequests)
                            requestList.add(mirrorRequest);
                    }
                    requests.addInfront(requestList);
                    break;
                }

                if (!checkPlaceLocation(placeLocationName)) {                                         // checks if the boxstack is not full
                    placeLocationName = allocator.findEmptySpace(boxStacks, placeLocationName);     // Search for an empty stack

                }

                if (pickupLocationName != null && placeLocationName != null)                             // allocate a vehicle to pick up the box if the pickup and place location are valid
                    allocateInstructionToVehicle(request, pickupLocationName, placeLocationName);
                else
                    System.err.println("there whas an error that prevent the allocation of the instruction to a vehicle");

                request.setStartTime(Main.timeCount);                   //set the starttime for the handling of the request
                request.setStatus(Constants.statusRequest.INPROGRESS);

//                System.out.println("");
//                System.out.println("------ Printing boxStacks ------");
//                for (String loc : Main.locations.keySet()) {
//                    System.out.print(loc + ": ");
//                    Main.locations.get(loc).printBoxes();
//                }
//                System.out.println("--------------------------------");
//                System.out.println("");
            }
            return true;
        }
        return false;
    }


    // this methode checks if the box is the first box in the pickup location and thus can be pickup up directly
    public Boolean checkBoxLocationInPickupLocation(String pickupLocationName, String associatedBoxId){
        if(bufferPoints.isBufferPoint(pickupLocationName)){                             // (if the pickup location is a bufferpoint then a box should first be added.)
            BufferPoint bp = bufferPoints.getBufferPointByName(pickupLocationName);     // get the correct bufferpoint
            if(!bp.boxes.contains(associatedBoxId))                                     // check if the box doesn't exist yet
                bp.addBox(associatedBoxId);                                             // add box to the bufferpoint
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


    // this function gets the x and y coordinates of the pickup and place location and gives them to the vehicles object
    public void allocateInstructionToVehicle(Request request, String pickupLocationName, String placeLocationName) {
        int[] pickupLocationXY;
        Location pickup;
        int[] placeLocationXY;
        Location dropOff;

        if (bufferPoints.isBufferPoint(pickupLocationName)) {
            pickupLocationXY = bufferPoints.getXY(pickupLocationName);
            pickup = bufferPoints.getBufferPointByName(pickupLocationName);
        }
        else {
            pickupLocationXY = boxStacks.getXY(pickupLocationName);
            pickup = boxStacks.getBoxStackByName(pickupLocationName);
        }

        if (bufferPoints.isBufferPoint(placeLocationName)) {
            placeLocationXY = bufferPoints.getXY(placeLocationName);
            dropOff = bufferPoints.getBufferPointByName(placeLocationName);
        }
        else {
            placeLocationXY = boxStacks.getXY(placeLocationName);
            dropOff = boxStacks.getBoxStackByName(placeLocationName);
        }

        request.setPickupLocationXY(pickupLocationXY);
        request.setPickup(pickup);

        request.setPlaceLocationXY(placeLocationXY);
        request.setDropOff(dropOff);

        Request splittedRequest =  vehicles.allocateInstructionToFreeVehicle(request);

        if(splittedRequest != null) {
            splittedRequest.setStatus(Constants.statusRequest.INPROGRESS);
            requests.addInfront(splittedRequest);
        }
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




}
