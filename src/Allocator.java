import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Allocator {
    //constructor
    public Allocator(){}

    //methods
    // methode finds the nearest empty boxStack to the placelocation boxstack
    public int findEmptySpace(BoxStacks boxStacks, int placeLocationId) {
        List<BoxStack> nearestEmptyBoxStacks = new ArrayList<>();
        BoxStack placeBoxStack = null;

        for(BoxStack boxStack: boxStacks.getBoxStacks()){
            if(boxStack.notFull())
                nearestEmptyBoxStacks.add(boxStack);

            if(boxStack.getId() == placeLocationId)
                placeBoxStack = boxStack;
        }

        if (placeBoxStack == null ) {
            System.err.println("the coordinates of the place boxStack could not be found");
            return -1;
        } else if (nearestEmptyBoxStacks.isEmpty()) {
            System.err.println("No empty stack can be found.");
            return -1;
        } else {
            int x = placeBoxStack.getX();
            int y = placeBoxStack.getY();

            nearestEmptyBoxStacks.sort(Comparator.comparingInt(v -> v.distanceToPoint(x, y)));
            return nearestEmptyBoxStacks.getFirst().getId();
        }
    }

    public Vehicle findClosestVehicleToBoxStack(BoxStack s, Vehicles v) {
        
    }

    public Vehicle findClosestVehicleToBuffer() {

    }

    public BoxStack relocate(Box b) {

    }
}
