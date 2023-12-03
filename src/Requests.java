import java.util.*;

public class Requests {
    private ArrayList<Request> requests;

    public Requests() {
        requests = new ArrayList<Request>();
    }

    public void add(Request r) {
        requests.add(r);
    }

    public boolean isEmpty(){
        return requests.isEmpty();
    }
    public Request getNextRequest(){
        Request request =  requests.get(0);
        requests.remove(0);
        return request;
    }

    public ArrayList<Request> getInProgressRequests () {
        ArrayList<Request> res = new ArrayList<>();
        for (Request r : requests) {
            if (r.getStatus() == Constants.statusRequest.INPROGRESS) {
                res.add(r);
            }
        }
        return res;
    }

    public Request getRequestByBox(String boxId) {  // functie werkt niet meer met arraylist van boxes
        Request res = null;
        for (Request r : requests) {
            if (r.getBoxIDsToString() == boxId) {
                res = r;
            }
        }
        return res;
    }

    public void printRequests() {
        for (Request r: requests) {
            System.out.println(r.getID() + r.getPickupLocation());
        }
    }

    // pool requests to same location together
    // start with simple pooling where all the requests with same pickuplocation and placelocation are pooled together in one request, if the boxes are directly on top of eachother
    public void preProcess(HashMap<String, Location> locations) {
        ArrayList<Request> res = new ArrayList<Request>();
        HashSet<Request> makedForRemoval = new HashSet<>();
        // pool requests per pickup location
        for (String pickupLocId: locations.keySet()) {
            System.out.println("Requests with pickup on: " + pickupLocId + ":");
            ArrayList<Request> requestsOnPickupLocation = new ArrayList<>();
            for (Request r: requests) {
                if (r.getPickupLocation().equals(pickupLocId)) requestsOnPickupLocation.add(r);
            }
            // run over all requests and pool them by place location
            for (String placeLocId: locations.keySet()) {
                System.out.print(" and dropoff on: " + placeLocId + " -> ");
                ArrayList<Request> requestsOnPlaceLocation= new ArrayList<>();
                for (Request r: requestsOnPickupLocation) {
                    if (r.getPlaceLocation().equals(placeLocId)) requestsOnPlaceLocation.add(r);
                }
                // now try to pool the requests together
                Collections.sort(requestsOnPlaceLocation, Comparator.comparingInt(r -> r.getPickup().getBoxes().indexOf(r.getBoxID())));    // sorts so that element at top of stack is last
                Collections.reverse(requestsOnPlaceLocation);
                for (Request r: requestsOnPlaceLocation) {
                    System.out.print(r.getBoxID() + "; ");
                }
                System.out.println(" ");

                // only try to pool together if size is bigger then one request
                if (requestsOnPlaceLocation.size() > 1) {
                    int indexFirstRequest = requests.indexOf(requestsOnPlaceLocation.get(0));
                    Location pickup = requestsOnPlaceLocation.get(0).getPickup();
                    Location place = requestsOnPlaceLocation.get(0).getDropOff();
                    ArrayList<ArrayList<String>> allBoxes = new ArrayList<>();          // list of subsequences
                    ArrayList<String> boxes = new ArrayList<>();
                    // forge new request from neighbouring requests
                    int i = 0;
                    while (i < requestsOnPlaceLocation.size()-1) {
                        String boxIdCurrentI = requestsOnPlaceLocation.get(i).getBoxID();
                        String boxIdNextI = requestsOnPlaceLocation.get(i+1).getBoxID();
                        // two requests follow each other, so we can pool them together
                        if (pickup.getBoxes().indexOf(boxIdCurrentI) - 1 == pickup.getBoxes().indexOf(boxIdNextI)) {
                            if (boxes.isEmpty()) {
                                boxes.add(requestsOnPlaceLocation.get(i).getBoxID());
                                makedForRemoval.add(requestsOnPlaceLocation.get(i));
                            }
                            boxes.add(requestsOnPlaceLocation.get(i+1).getBoxID());
                            makedForRemoval.add(requestsOnPlaceLocation.get(i+1));
                        } else {
                            if (!boxes.isEmpty()) {         // if the subsequence we found is not empty, add it to the list of subsequences
                                allBoxes.add(new ArrayList<>(boxes));
                                boxes.clear();
                            }
                        }
                        i++;
                    }
                    if (!boxes.isEmpty()) {     // for the last subsequence
                        allBoxes.add(boxes);
                    }

                    for (int index = 0; index < allBoxes.size(); index++) {
                        ArrayList<String> subsequence = allBoxes.get(index);
                        int newId = requestsOnPlaceLocation.get(0).getID() * requestsOnPlaceLocation.get(i-1).getID() + index;
                        Request pooledRequest = new Request(newId, pickup, place, subsequence);
                        System.out.println("new pooled request with ID: " + newId + " and boxes: " + pooledRequest.getBoxIDsToString());
                        requests.add(indexFirstRequest, pooledRequest);
                    }

                }
            }
            System.out.println();
        }
        // remove the requests marked for removal
        for (Request markedSucker: makedForRemoval) {
            requests.remove(markedSucker);
        }

        System.out.println("Resulting requestlist after preprocessing");
        for (Request r: requests) {
            System.out.println(r.toString());
        }
        System.out.println(" ");
    }
    // nadeel alle requests samen te poolen -> veel trager want zelf als request na processing nog één box heeft, zit dit toch nog in een arraylist
    //          -> maakt alle operaties die gebuik maken van box trager (vb opvragen door vehicle, print, ...)

    public void addInfront(List<Request> requestList){
        requests.addAll(0, requestList);
    }

    public void addAtIndex(int index, Request request) {
        requests.add(index, request);
    }

    public void addInfront(Request request){
        requests.add(0, request);
    }

    public void updateFutureRequests(List<Request> newRequests) {        // fout gevonden: elke reallocation heeft maar 1 box per keer -> pooledRequests worden nooit geupdate
        HashMap<String, String> newLocation = new HashMap<>();          // opgelost door eerst de boxIds te splitsen en allemaal te overlopen

        for(Request request: newRequests)
            newLocation.put(request.getBoxIDsToString(), request.getPlaceLocation());

        for(Request request: requests) {
            if (request.getBoxIDs().size() > 2) {
                System.out.println("test " + request.getID() + " -> " + request.getBoxIDsToString());
                String firstBox = request.getBoxIDs().get(request.getBoxIDs().size()-1);
                System.out.println(firstBox);
                System.out.println("old location: " + request.getPickupLocation());
                if (newLocation.containsKey(firstBox)) {
                    request.setPickupLocation(newLocation.get(firstBox));
                    System.out.println("new location: " + newLocation.get(firstBox));
                }
            }
            else {
                if (newLocation.containsKey(request.getBoxIDsToString())) {
                    request.setPickupLocation(newLocation.get(request.getBoxIDsToString()));
                }
            }
        }
        newLocation.clear();
    }
}
