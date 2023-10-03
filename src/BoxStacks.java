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

    }

    public Boolean checkBoxStackNotFull(int boxStackId){
        for(BoxStack boxStack : boxStacks){
            if(boxStack.getId() == boxStackId){
                return boxStack.notFull();
            }
        }
        System.err.println("an non exciting boxStackId was given!!!");
        return null;
    }

    public int getTopBoxIdOfBoxStack(int boxStackId){
        for(BoxStack boxStack : boxStacks){
            if(boxStack.getId() == boxStackId){
                return boxStack.peekStack();
            }
        }
        System.err.println("an non exciting boxStackId was given!!!");
        return -1;
    }

}
