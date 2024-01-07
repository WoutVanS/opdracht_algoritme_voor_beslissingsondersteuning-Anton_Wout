import java.util.*;

public class Allocator {
    //constructor
    public Allocator(){}
    public Random rand = new Random();

    //methods
    // methode finds the nearest empty boxStack to the placelocation boxstack
    public String findEmptySpace(BoxStacks boxStacks, String placeLocationName) {
        ArrayList<BoxStack> nearestEmptyBoxStacks = new ArrayList<>();
        BoxStack placeBoxStack = null;

        for(BoxStack boxStack: boxStacks.getBoxStacks()){
            if(boxStack.notFull())
                nearestEmptyBoxStacks.add(boxStack);

            if(boxStack.getName().equals(placeLocationName))
                placeBoxStack = boxStack;
        }

        if (placeBoxStack == null ) {
            System.err.println("the coordinates of the place boxStack could not be found");
            return "";
        } else if (nearestEmptyBoxStacks.isEmpty()) {
            System.err.println("No empty stack can be found.");
            return "";
        } else {
            int x = placeBoxStack.getX();
            int y = placeBoxStack.getY();

            nearestEmptyBoxStacks.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));
            return nearestEmptyBoxStacks.get(0).getName();
        }
    }

//    public Vehicle findClosestVehicleToBoxStack(BoxStack s, Vehicles v) {
//
//    }
//
//    public Vehicle findClosestVehicleToBuffer() {
//
//    }
//
//    public BoxStack realocate(Box b) {
////
//    }


    public List<Request> realocationAlgorithm(BoxStacks boxStacks, String pickupLocationName , String associatedBoxId){

        System.out.println();
        System.out.println("==================reallocation======================");


        List<BoxStack> nearestEmptyBoxStacks = new ArrayList<>();
        BoxStack pickupBoxStack = null;

        for(BoxStack boxStack: boxStacks.getBoxStacks()){
            if(boxStack.getName().equals(pickupLocationName))
                pickupBoxStack = boxStack;
            else if(boxStack.notFull())
                nearestEmptyBoxStacks.add(boxStack);

        }

        int x = pickupBoxStack.getX();
        int y = pickupBoxStack.getY();

//        nearestEmptyBoxStacks.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));

        nearestEmptyBoxStacks.sort(Comparator.comparingInt(l -> l.getBoxes().size()));      // sort on most empty boxes first

        int numberOfReallocations = pickupBoxStack.SearchDepthByID(associatedBoxId);
        System.out.println("number of relocations necessary: " + numberOfReallocations);
        Main.amountOfRelocations += numberOfReallocations;

        List<Request> requests = new ArrayList<>();
        int numberOfFreeSpace = nearestEmptyBoxStacks.get(0).freeSpace();

        ArrayList<String> boxes = new ArrayList<>();

        if(numberOfReallocations < 0) {
            System.err.println("error");
        }

        for(int i = 0; i < numberOfReallocations; i++){
            boxes.add(pickupBoxStack.boxes.get(pickupBoxStack.boxes.size() - i -1));
            numberOfFreeSpace--;

            if(numberOfFreeSpace == 0 || i == numberOfReallocations -1){
                int ID = 6969000 + rand.nextInt(1000);
                Request request = new Request(ID, pickupBoxStack, nearestEmptyBoxStacks.get(0), boxes);
                request.setStatus(Constants.statusRequest.INPROGRESS);
                requests.add(request);
                boxes = new ArrayList<>();
                System.out.println(request);

                nearestEmptyBoxStacks.remove(0);
                numberOfFreeSpace = (nearestEmptyBoxStacks.isEmpty())? 0 :  nearestEmptyBoxStacks.get(0).freeSpace();
            }
        }

        return requests;
    }

    public ArrayList<Request> mirrorRealocatedRequests(List<Request> reallocateRequests) {
        ArrayList<Request> mirroredRequests = new ArrayList<>();

        // for each reallocate request the request is broken up in individual mirrored reallocations (box per box), because we are unsure of the order in which they are dumped on the new stack
        for (Request request: reallocateRequests) {
            ArrayList<String> boxIds = request.getBoxIDs();
            Location newPickup = request.getDropOff();      // pickup and dropoff are reversed in mirrored request
            Location newDropOff = request.getPickup();
            for (int i = 0; i < boxIds.size(); i++) {
                int ID = 6968000 + rand.nextInt(1000);
                ArrayList<String> mirroredBoxes = new ArrayList<>();
                if (Main.incomingBoxes.contains(boxIds.get(i))) {           // we only put boxes back in their place if they where not in the system at the beginning.
//                    System.out.println("Bringing box back " + boxIds.get(i));
                    mirroredBoxes.add(boxIds.get(i));
                    Request mirr = new Request(ID, newPickup, newDropOff, mirroredBoxes);
                    System.out.println(mirr);
                    mirroredRequests.add(mirr);
                }
            }
        }
        Collections.reverse(mirroredRequests);
        return mirroredRequests;
    }
}
