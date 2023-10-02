
public class Main {
    private static final int V = 2; // V is number of vehicles
    private static final int S = 8; // V is number of stacks
    private static final int Cs = 2; // Cs is max capacity of a single stack
    private static final int Cb = 1; // Cs is max capacity of the bufferpoint

    private static final String CSV_FILE = "instructions.csv"; // Replace with your CSV file path
    private static final String CSV_DELIMITER = ";"; // Set the delimiter used in your CSV file

    public static void main(String[] args) {
        Vehicles vehicles = new Vehicles(V);

        for(int i = 0; i < V; i++) {
            Vehicle v = new Vehicle(i+1, 1, 0, 0);
            vehicles.addVehicle(v);
        }

        BoxStacks boxStacks = new BoxStacks();
        for(int i = 0; i < S; i++) {
            BoxStack bs = new BoxStack(i+1, i*2, 2, Cs);
            boxStacks.appendBoxStacks(bs);
        }

        Allocator allocator = new Allocator();
        BufferPoint bufferPoint = new BufferPoint(0, 0, Cb);

        Network network = new Network(allocator, boxStacks, vehicles, bufferPoint);


        network.run(CSV_FILE,CSV_DELIMITER);

    }
}