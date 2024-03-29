import java.util.*;

public class Requests {
    public ArrayList<Request> requests;

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

    public void sortToCreateSpace() {       // sort the requests so that the incoming boxes are last in array
//        requests.sort(Comparator.comparing(request -> request.getDropOff() instanceof BufferPoint));
        requests.sort(Comparator.comparing(request -> request.getPickup() instanceof BufferPoint));
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

    // updates request when relocations where made. it only updates the first relevant request because it gets executed after every dropoff.
    public void updateRequests(Request currentRequest, Location currentLocation){
        
        int currentRequestSize = currentRequest.getBoxIDs().size();
        int currentLocationSize = currentLocation.boxes.size();
        String currentPickUp = currentRequest.getPickupLocation();
        String currentDropoff = currentRequest.getPlaceLocation();
        String topBoxBeforeDropOff = (currentLocationSize - currentRequestSize -1 < 0)? "" : currentLocation.boxes.get(currentLocationSize - currentRequestSize -1);


        ////////debug//////////////////////////
        if(currentPickUp.equals("stack0_18") && currentDropoff.equals("stack0_17") && currentRequest.getBoxIDs().contains("B1008"))
            System.err.println("funcy buesnness here");


        /////////////// debug ///////////////

        ArrayList<String> boxes = (ArrayList<String>) currentRequest.getBoxIDs().clone();
        Collections.reverse(boxes);


        for(String box: boxes){                        // look for each box if there are request that need to be updated
            for(Request request: requests){
                if(request.getBoxIDs().get(0).equals(box)){                 // if request contains request

                    // checks if there is still a mirror action in the requests list and deletes this action.
                    if(request.getPickupLocation().equals(currentPickUp) && request.getPlaceLocation().equals(currentDropoff)){
                        requests.remove(request);
                        break;
                    }
                    // check if pickup is different from place location of current request
                    if(!request.getPickupLocation().equals(currentDropoff)){
                        String requestPLaceLocation = request.getPlaceLocation();
                        request.getBoxIDs().remove(0);                 // if its different the box needs to be removed from the old request

                        boolean prepocessedRequestUpdated = false;
                        for(Request nextRequest: requests.subList(requests.indexOf(request), requests.size())){
                            // checks if there is already a preprocessed request from the same location to the same location and if the pool containts the top box before the dropoff.
                            // if true then add the box to this already existing request
                            if(nextRequest.getPickupLocation().equals(currentDropoff) && nextRequest.getPlaceLocation().equals(requestPLaceLocation) && nextRequest.getBoxIDs().contains(topBoxBeforeDropOff)){
                                prepocessedRequestUpdated = true;
                                int index = nextRequest.getBoxIDs().indexOf(topBoxBeforeDropOff);
                                nextRequest.getBoxIDs().add(index, box);
                                break;
                            }
                            // does the same as the if statement above but then checks if this request already contains a box from the currentRequest that the vehicle dropped off
//                            if(nextRequest.getPickupLocation().equals(currentDropoff) && nextRequest.getPlaceLocation().equals(requestPLaceLocation) && nextRequest.getBoxIDs().contains(boxes.get(0))){
//                                prepocessedRequestUpdated = true;
//                                nextRequest.getBoxIDs().add(box);
//                                break;
//                            }
                        }

                        // makes a new request if there is none existing from the location to the pickup.
                        if(!prepocessedRequestUpdated){
                            int requestID = currentRequest.getID() * 1000 + 1;
                            Request newRequest = new Request(requestID, currentLocation, request.getDropOff(), box);
                            requests.add(newRequest);
                            Main.amountOfPooledRequest ++;
                        }

                        if(request.getBoxIDs().isEmpty()) requests.remove(request);

                    }
                    break;
                }
            }
            topBoxBeforeDropOff = box;
        }

//        System.out.println("\nrequests after updating them");
//        for (Request r: requests) {
//            System.out.println(r.toString());
//        }
//        System.out.println(" ");

    }

    public void sortForFixings() {
        for (int i = 0; i < requests.size(); i++) {
            Request first = requests.get(i);
            int indexFirst = requests.indexOf(first);
            for (int j = i; j < requests.size(); j++) {
                Request second = requests.get(j);
                int indexSecond = requests.indexOf(second);
                // fist is pickup -> make sure it is handeld first to avoid reallocations
                if (first.getPlaceLocation().equals(second.getPickupLocation())) {
                    requests.remove(second);
                    requests.remove(first);
                    requests.add(indexSecond, first);
                    requests.add(indexFirst, first);
                }
            }
        }

        System.out.println("new order correcting requests");
        for (Request r: requests) {
            System.out.println(r.toString());
        }
    }

//    public void updateFutureRequests(List<Request> newRequests) {        // fout gevonden: elke reallocation heeft maar 1 box per keer -> pooledRequests worden nooit geupdate
//        HashMap<String, String> newLocation = new HashMap<>();          // opgelost door eerst de boxIds te splitsen en allemaal te overlopen
//
//        for(Request request: newRequests)
//            newLocation.put(request.getBoxIDsToString(), request.getPlaceLocation());
//
//        for(Request request: requests) {
//            if (request.getBoxIDs().size() > 2) {
//                System.out.println("test " + request.getID() + " -> " + request.getBoxIDsToString());
//                String firstBox = request.getBoxIDs().get(request.getBoxIDs().size()-1);
//                System.out.println(firstBox);
//                System.out.println("old location: " + request.getPickupLocation());
//                if (newLocation.containsKey(firstBox)) {
//                    request.setPickupLocation(newLocation.get(firstBox));
//                    System.out.println("new location: " + newLocation.get(firstBox));
//                }
//            }
//            else {
//                if (newLocation.containsKey(request.getBoxIDsToString())) {
//                    request.setPickupLocation(newLocation.get(request.getBoxIDsToString()));
//                }
//            }
//        }
//        newLocation.clear();
//    }
}
