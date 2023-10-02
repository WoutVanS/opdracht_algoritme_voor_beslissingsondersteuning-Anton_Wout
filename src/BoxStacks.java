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

}
