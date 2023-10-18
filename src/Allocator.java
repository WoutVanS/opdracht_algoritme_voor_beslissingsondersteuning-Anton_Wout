import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Allocator {
    //constructor
    public Allocator(){}

    //methods
    // methode finds the nearest empty boxStack to the placelocation boxstack
    public String findEmptySpace(BoxStacks boxStacks, String placeLocationName) {
        List<BoxStack> nearestEmptyBoxStacks = new ArrayList<>();
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
            return nearestEmptyBoxStacks.getFirst().getName();
        }
    }

    public Vehicle findClosestVehicleToBoxStack(BoxStack s, Vehicles v) {
        
    }

    public Vehicle findClosestVehicleToBuffer() {

    }

    public BoxStack realocate(Box b) {

    }

//    public Request realocationAlgorithm(BoxStacks boxStacks, String pickupLocationName , String associatedBoxId){
//        List<BoxStack> nearestEmptyBoxStacks = new ArrayList<>();
//        BoxStack pickupBoxStack = null;
//
//        for(BoxStack boxStack: boxStacks.getBoxStacks()){
//            if(boxStack.notFull())
//                nearestEmptyBoxStacks.add(boxStack);
//
//            if(boxStack.getName().equals(pickupLocationName))
//                pickupBoxStack = boxStack;
//        }
//
//        int numberOfReallocations = pickupBoxStack.SearchDepthByID(associatedBoxId);
//        List<Request> requests;
//        for(int i = 0; i < numberOfReallocations; i++){
//
//        }
//    }
}
