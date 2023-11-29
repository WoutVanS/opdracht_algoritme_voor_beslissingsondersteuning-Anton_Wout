import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Allocator {
    //constructor
    public Allocator(){}

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

        nearestEmptyBoxStacks.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));

        int numberOfReallocations = pickupBoxStack.SearchDepthByID(associatedBoxId);
        System.out.println("number of relocations necessary: " + numberOfReallocations);

        List<Request> requests = new ArrayList<>();
        int numberOfFreeSpace = nearestEmptyBoxStacks.get(0).freeSpace();

        for(int i = 0; i < numberOfReallocations; i++){

            if(numberOfFreeSpace == 0){
                nearestEmptyBoxStacks.remove(0);
                numberOfFreeSpace = nearestEmptyBoxStacks.get(0).freeSpace();
            }

            Random rand = new Random();
            int ID = 6969000 + rand.nextInt(1000);
            Request request = new Request(ID, pickupBoxStack, nearestEmptyBoxStacks.get(0), pickupBoxStack.boxes.get(pickupBoxStack.boxes.size() - i -1));
            requests.add(request);
            numberOfFreeSpace--;

            System.out.println(request);
        }

        return requests;
    }
}
