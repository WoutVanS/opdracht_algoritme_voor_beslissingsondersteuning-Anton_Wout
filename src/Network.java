
import java.io.*;
import java.util.Optional;

public class Network {
    private Allocator allocator;
    private BoxStacks boxStacks;
    private Vehicles vehicles;
    private BufferPoint bufferPoint;

    //constructor
    public Network(Allocator allocator, BoxStacks boxStacks, Vehicles vehicles, BufferPoint bufferPoint) {
        this.allocator = allocator;
        this.boxStacks = boxStacks;
        this.vehicles = vehicles;
        this.bufferPoint = bufferPoint;
    }

    // run methode the main of network. It is responsible for handling the instructions in the CSV file.
    // it instructs vehicles what to do.
    public void run(String csvFile, String csvDelimiter){
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) { // opening the CSV file
            while ((line = br.readLine()) != null) {                            // keep going while there are new instructions
                String[] data = line.split(csvDelimiter);                       //
                int pickupLocation = Integer.parseInt(data[0]);
                int placeLocation = Integer.parseInt(data[0]);
                int associatedBbox = Integer.parseInt(data[0]);


                // Process the data as needed
                for (String value : data) {
                    System.out.print(value + " ");
                }
                System.out.println(); // Print a new line for each row
            }
        } catch (IOException e) {
            System.err.println("error while loading the instrutions CSV file: " + e);
        }
    }

    //getters and setters
    public Allocator getAllocator() {
        return allocator;
    }
    public void setAllocator(Allocator allocator) {
        this.allocator = allocator;
    }
    public BoxStacks getBoxStacks() {
        return boxStacks;
    }
    public void setBoxStacks(BoxStacks boxStacks) {
        this.boxStacks = boxStacks;
    }
    public Vehicles getVehicles() {
        return vehicles;
    }
    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }

    //methodes
    public void updateVehicles(){
        vehicles.update();
    }



}
