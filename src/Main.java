import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.io.FileReader;

public class Main {
    public static int loadingDuration;
    public static int timeCount;


    public static void main(String[] args) {

        BoxStacks boxStacks = new BoxStacks();
        BufferPoints bufferPoints = new BufferPoints();
        Vehicles vehicles = new Vehicles();
        Requests requests = new Requests();

        JSONObject jo = new JSONObject();
        //load in json file
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("I3_3_1_5.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            loadingDuration = Integer.parseInt(jsonObject.get("loadingduration").toString());
            System.out.println(loadingDuration);

            int vehiclespeed = Integer.parseInt(jsonObject.get("vehiclespeed").toString());
            System.out.println(vehiclespeed);

            int stackcapacity = Integer.parseInt(jsonObject.get("stackcapacity").toString());
            System.out.println(stackcapacity);

            // loop array of stacks
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
            }

            //loop array of bufferpoints
            JSONArray bufferpoints = (JSONArray) jsonObject.get("bufferpoints");
            for (Object o : bufferpoints) {
                JSONObject bufferpoint = (JSONObject) o;
                int id = Integer.parseInt(bufferpoint.get("ID").toString());
                String name = bufferpoint.get("name").toString();
                int x = Integer.parseInt(bufferpoint.get("x").toString());
                int y = Integer.parseInt(bufferpoint.get("y").toString());

                bufferPoints.add(new BufferPoint(name, id, x, y));
            }

            //loop array of vehicles
            JSONArray vehicleList = (JSONArray) jsonObject.get("vehicles");
            for (Object o : vehicleList) {
                JSONObject vehicle = (JSONObject) o;
                int id = Integer.parseInt(vehicle.get("ID").toString());
                String name = vehicle.get("name").toString();
                int capacity = Integer.parseInt(vehicle.get("capacity").toString());
                int x = Integer.parseInt(vehicle.get("xCoordinate").toString());
                int y = Integer.parseInt(vehicle.get("yCoordinate").toString());

                vehicles.addVehicle(new Vehicle(id, name, capacity, vehiclespeed, x, y));
            }

            //loop array of requests
            JSONArray requestList = (JSONArray) jsonObject.get("requests");
            for (Object o : requestList) {
                JSONObject request = (JSONObject) o;
                int id = Integer.parseInt(request.get("ID").toString());

                String pickupLocation = request.get("pickupLocation").toString();
                pickupLocation = pickupLocation.substring(2, pickupLocation.length()-2);
                String placeLocation = request.get("placeLocation").toString();
                placeLocation = placeLocation.substring(2, placeLocation.length()-2);
                String boxID = request.get("boxID").toString();

                requests.add(new Request(id, pickupLocation, placeLocation, boxID));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        Allocator allocator = new Allocator();
        Network network = new Network(allocator, boxStacks, vehicles, bufferPoints, requests);

        // run in loop and increment timeCount each itteration
        timeCount = 0;
        while(timeCount < 100000) {

            //let all vehicles move closer to destination
            vehicles.updateVehicles();

            //run the network and assing requests to new vehicles
            network.run();

            timeCount++;
        }


    }
}