import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

import java.io.*;
import java.io.FileReader;

public class Main {


    public static void main(String[] args) {

        BoxStacks boxStacks = new BoxStacks();
        BufferPoints bufferPoints = new BufferPoints();
        Vehicles vehicles = new Vehicles();
        Requests requests = new Requests();

        JSONObject jo = new JSONObject();
        //load in json file
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("l3_3_1.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            String loadingduration = (String) jsonObject.get("loadingduration");
            System.out.println(loadingduration);

            int vehiclespeed = (int) jsonObject.get("vehiclespeed");
            System.out.println(vehiclespeed);

            int stackcapacity = (int) jsonObject.get("stackcapacity");
            System.out.println(stackcapacity);

            // loop array of stacks
            JSONArray stacks = (JSONArray) jsonObject.get("stacks");
            for (Object o : stacks) {
                JSONObject stack = (JSONObject) o;
                int id = ((Integer) stack.get("ID")).intValue();
                String name = (String) stack.get("name").toString();
                int x = ((Integer) stack.get("x")).intValue();
                int y = ((Integer) stack.get("y")).intValue();

                BoxStack newStack = new BoxStack(name, id, x, y, stackcapacity);

                String[] boxes = (String[]) stack.get("boxes");
                for (int j = 0; j < boxes.length; j++) {
                    newStack.addBox(new Box(boxes[j]));
                }

                boxStacks.appendBoxStacks(newStack);
            }

            //loop array of bufferpoints
            JSONArray bufferpoints = (JSONArray) jsonObject.get("bufferpoints");
            for (Object o : bufferpoints) {
                JSONObject bufferpoint = (JSONObject) o;
                int id = ((Integer) bufferpoint.get("ID")).intValue();
                String name = (String) bufferpoint.get("name").toString();
                int x = ((Integer) bufferpoint.get("x")).intValue();
                int y = ((Integer) bufferpoint.get("y")).intValue();

                bufferPoints.add(new BufferPoint(id, name, x, y));
            }

            //loop array of vehicles
            JSONArray vehicleList = (JSONArray) jsonObject.get("vehicles");
            for (Object o : vehicleList) {
                JSONObject vehicle = (JSONObject) o;
                int id = ((Integer) vehicle.get("ID")).intValue();
                String name = (String) vehicle.get("name").toString();
                int capacity = ((Integer) vehicle.get("capacity")).intValue();
                int x = ((Integer) vehicle.get("xCoordinate")).intValue();
                int y = ((Integer) vehicle.get("yCoordinate")).intValue();

                vehicles.addVehicle(new Vehicle(id, name, vehiclespeed, x, y));
            }

            //loop array of requests
            JSONArray requestList = (JSONArray) jsonObject.get("requests");
            for (Object o : requestList) {
                JSONObject request = (JSONObject) o;
                int id = ((Integer) request.get("ID")).intValue();
                String[] pickupLocation = (String[]) request.get("pickupLocation");
                String[] placeLocation = (String[]) request.get("placeLocation");
                String boxID = (String) request.get("boxID").toString();

                requests.add(new Request(id, pickupLocation[0], placeLocation[0], boxID));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        Allocator allocator = new Allocator();
        Network network = new Network(allocator, boxStacks, vehicles, bufferPoints, requests);

        network.run();

    }
}