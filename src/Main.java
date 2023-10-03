
public class Main {


    public static void main(String[] args) {
        Vehicles vehicles = new Vehicles(Constants.V);

        for(int i = 0; i < Constants.V; i++) {
            Vehicle v = new Vehicle(i+1, 1, 0, 0);
            vehicles.addVehicle(v);
        }

        BoxStacks boxStacks = new BoxStacks();
        for(int i = 0; i < Constants.S; i++) {
            BoxStack bs = new BoxStack(i+1, i*2, 2, Constants.Cs);
            boxStacks.appendBoxStacks(bs);
        }

        Allocator allocator = new Allocator();
        BufferPoint bufferPoint = new BufferPoint(0, 0);

        Network network = new Network(allocator, boxStacks, vehicles, bufferPoint);


        network.run(Constants.CSV_FILE,Constants.CSV_DELIMITER);

    }
}