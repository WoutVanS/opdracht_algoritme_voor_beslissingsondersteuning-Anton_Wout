import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/* TODO:
    GRONDIG NAKIJKEN ALS ALLES NOG CORRECT WERKT,
    REPORT 2 SCHRIJVEN
    NAKIJKEN ALS MEERDERE POOLS UIT 1 STACK MAKEN WERKT (anton)
 */

public class Main {
    public static int loadingDuration;
    public static int timeCount;
    public static HashMap<String, Location> locations = new HashMap<>();

    public static ArrayList<String> outputArray = new ArrayList<>();

    public static HashSet<String> incomingBoxes = new HashSet<>();

    public static int stackcapacity;
    public static int vehiclespeed;

    public static String filename = "I100_120_2_2_8b2";       // very congested datastack, risk of box beingn full
//    public static String filename = "I100_50_2_2_8b2";
//    public static String filename = "I3_3_1_5";
    public static void main(String[] args) {

        BoxStacks boxStacks = new BoxStacks();
        BufferPoints bufferPoints = new BufferPoints();
        Vehicles vehicles = new Vehicles();
        Requests requests = new Requests();

        JSONObject jo = new JSONObject();
        //load in json file
        JSONParser parser = new JSONParser();

        try {
//            Object obj = parser.parse(new FileReader("I30_100_1_1_10.json"));
//            Object obj = parser.parse(new FileReader("I20_20_2_2_8b2.json"));
            String inputFileName = filename + ".json";
            Object obj = parser.parse(new FileReader(inputFileName));

            JSONObject jsonObject =  (JSONObject) obj;

            System.out.println("============ Network parameters ============");
            loadingDuration = Integer.parseInt(jsonObject.get("loadingduration").toString());
            System.out.println("loading Duration: " + loadingDuration);

            vehiclespeed = Integer.parseInt(jsonObject.get("vehiclespeed").toString());
            System.out.println("vehicle speed: " + vehiclespeed);

            stackcapacity = Integer.parseInt(jsonObject.get("stackcapacity").toString());
            System.out.println("stack capacity:" + stackcapacity);

            // loop array of stacks
            System.out.println("\n============== filling stacks ==============");
            JSONArray stacks = (JSONArray) jsonObject.get("stacks");
            for (Object o : stacks) {
                JSONObject stack = (JSONObject) o;
                int id = Integer.parseInt(stack.get("ID").toString());
                String name = stack.get("name").toString();
                int x = Integer.parseInt(stack.get("x").toString());
                int y = Integer.parseInt(stack.get("y").toString());

                BoxStack newStack = new BoxStack(name, id, x, y, stackcapacity);

                JSONArray boxes = (JSONArray) stack.get("boxes");
                for (int j = 0; j < boxes.size(); j++) {
                    newStack.addBox(boxes.get(j).toString());
                }

                boxStacks.appendBoxStacks(newStack);
                locations.put(newStack.getName(), newStack);
            }

            //loop array of bufferpoints
            JSONArray bufferpoints = (JSONArray) jsonObject.get("bufferpoints");
            BufferPoint newBuff;
            for (Object o : bufferpoints) {
                JSONObject bufferpoint = (JSONObject) o;
                int id = Integer.parseInt(bufferpoint.get("ID").toString());
                String name = bufferpoint.get("name").toString();
                int x = Integer.parseInt(bufferpoint.get("x").toString());
                int y = Integer.parseInt(bufferpoint.get("y").toString());

                newBuff = new BufferPoint(name, id, x, y);

                bufferPoints.add(newBuff);
                locations.put(newBuff.getName(), newBuff);
            }

            //loop array of vehicles
            JSONArray vehicleList = (JSONArray) jsonObject.get("vehicles");
            for (Object o : vehicleList) {
                JSONObject vehicle = (JSONObject) o;
                int id = Integer.parseInt(vehicle.get("ID").toString());
                String name = vehicle.get("name").toString();
                int capacity = Integer.parseInt(vehicle.get("capacity").toString());
//                int x = Integer.parseInt(vehicle.get("x").toString());
//                int y = Integer.parseInt(vehicle.get("y").toString());
                int x = Integer.parseInt(vehicle.get("x").toString());
                int y = Integer.parseInt(vehicle.get("y").toString());

                vehicles.addVehicle(new Vehicle(id, name, capacity, vehiclespeed, x, y));
            }

            //loop array of requests
            JSONArray requestList = (JSONArray) jsonObject.get("requests");
            for (Object o : requestList) {
                JSONObject request = (JSONObject) o;
                int id = Integer.parseInt(request.get("ID").toString());

                String pickupLocation = request.get("pickupLocation").toString();
//                pickupLocation = pickupLocation.substring(2, pickupLocation.length()-2);
                String placeLocation = request.get("placeLocation").toString();
//                placeLocation = placeLocation.substring(2, placeLocation.length()-2);
                String boxID = request.get("boxID").toString();

                Location pickup = locations.get(pickupLocation);
                Location place = locations.get(placeLocation);

                if (pickup instanceof BufferPoint) {
                    incomingBoxes.add(boxID);
                }

                requests.add(new Request(id, pickup, place, boxID));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        Allocator allocator = new Allocator();
        Network network = new Network(allocator, boxStacks, vehicles, bufferPoints, requests);

        // preprocess the requests
        System.out.println("\n========== preprocessing requests ==========");
        requests.preProcess(locations);

        System.out.println("Preprocessing done, starting program");
        System.out.println(" ");

        // run in loop and increment timeCount each itteration

        System.out.println("\n============= running program ==============");
        timeCount = 0;
        boolean networkState = true;
        boolean vehiclesWorking = true;
        while( networkState || vehiclesWorking) {

//            let all vehicles move closer to destination
            vehiclesWorking = vehicles.updateVehicles(requests);

            //run the network and assing requests to new vehicles
            networkState = network.run();

            timeCount++;
        }

        System.out.println("\n======== ALL REQUESTS ARE HANDLED ==========");

        System.out.println("");
        System.out.println("------ Printing boxStacks ------");
        for (String loc : Main.locations.keySet()) {
            System.out.print(loc + ": ");
            Main.locations.get(loc).printBoxes();
        }
        System.out.println("--------------------------------");
        System.out.println("");

        System.out.println("total time needed: " + timeCount);

        // write output to file
        String outputFileName = "output_" + filename + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String str : outputArray) {
                bw.write(str);
                bw.newLine();
            }
            System.out.println("Successfully wrote ArrayList to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}