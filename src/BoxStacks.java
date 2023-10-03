import java.util.ArrayList;
import java.util.List;

public class BoxStacks {
    private List<BoxStack> boxStacks;

    public BoxStacks(){
        boxStacks = new ArrayList<>();
    }
    public BoxStacks(List<BoxStack> boxStacks){
        this.boxStacks = boxStacks;
    }

    //getters and setters
    public List<BoxStack> getBoxStacks() {
        return boxStacks;
    }
    public void setBoxStacks(List<BoxStack> boxStacks) {
        this.boxStacks = boxStacks;
    }
    public void appendBoxStacks(BoxStack boxStack){
        this.boxStacks.add(boxStack);
    }
    public BoxStack getBoxStack(int x, int y) {
        return null;
    }

    // checks if the boxStack , withs id is given, is not full. return true or false unless a non existing id is given
    public Boolean checkBoxStackNotFull(int boxStackId){
        for(BoxStack boxStack : boxStacks){
            if(boxStack.getId() == boxStackId){
                return boxStack.notFull();
            }
        }
        System.err.println("an non exciting boxStackId was given!!!");
        return null;
    }

    // returns the id of the top box of a certain boxStack , withs id is given. return positive box id unless a non existing boxStack id is given
    public int getTopBoxIdOfBoxStack(int boxStackId){
        for(BoxStack boxStack : boxStacks){
            if(boxStack.getId() == boxStackId){
                return boxStack.peekStack();
            }
        }
        System.err.println("an non exciting boxStackId was given!!!");
        return -1;
    }

    // returns an array of the XY coordinates from the boxStack, withs id is given
    public int[] getXY(int boxStackId){
        for(BoxStack boxStack: boxStacks){
            if(boxStack.getId() == boxStackId) {
                return new int[]{boxStack.getX(), boxStack.getY()};
            }
        }
    }
}
