import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

public class BufferPoints {
    private HashMap<String, BufferPoint> bufferPoints;

    public BufferPoints() {
        bufferPoints = new HashMap<>();
    }

    public void add(BufferPoint b) {
        bufferPoints.put(b.getName(), b);
    }

    public BufferPoint getBufferPointByName(String name) {
        return bufferPoints.get(name);
    }

    public boolean isBufferPoint(String name) {
        if (bufferPoints.containsKey(name)) {
            return true;
        }
        else {
            return false;
        }
    }

    // returns an array of the XY coordinates from the bufferpoint, withs id is given
    public int[] getXY(String name){
        if (isBufferPoint(name)) {
            return new int[] {bufferPoints.get(name).getX(), bufferPoints.get(name).getY()};
        }
        else {
            return new int[]{-1, -1};
        }
    }
}
