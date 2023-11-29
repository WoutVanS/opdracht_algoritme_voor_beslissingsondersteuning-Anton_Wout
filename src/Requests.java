import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Request getRequestByBox(String boxId) {
        Request res = null;
        for (Request r : requests) {
            if (r.getBoxID() == boxId) {
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

//    // pool requests to same location together
//    public void preProcess() {
//        ArrayList<Request> res = new ArrayList<Request>();
//        ArrayList<Request> temp;
//        // pool requests per location
//        HashMap<String, ArrayList<Request>> requestsPerLocation = new HashMap<String, ArrayList<Request>>();
//        for (Request r: requests) {
//            if (!requestsPerLocation.containsKey(r.getPlaceLocation())) {
//                requestsPerLocation.put(r.getPickupLocation(), new ArrayList<>());
//            }
//            temp = requestsPerLocation.get(r.getPickupLocation());
//            temp.add(r);
//            System.out.println(temp.size());
//            requestsPerLocation.put(r.getPickupLocation(), temp);
//        }
//
//        for (HashMap.Entry<String, ArrayList<Request>> entry : requestsPerLocation.entrySet()) {
//            String location = entry.getKey();
//            ArrayList<Request> newRequests = entry.getValue();
//            System.out.println("Location: " + location);
//            for (Request request : newRequests) {
//                System.out.println(request);
//            }
//        }
//        System.out.println("-------------");
//    }

    public void addInfront(List<Request> requestList){
        requests.addAll(0, requestList);
    }

    public void updateFutureRequests(List<Request> newRequests){
        HashMap<String, String> newLocation = new HashMap<>();

        for(Request request: newRequests)
            newLocation.put(request.getBoxID(), request.getPlaceLocation());

        for(Request request: requests) {
            if (newLocation.containsKey(request.getBoxID())) {
                request.setPickupLocation(newLocation.get(request.getBoxID()));
            }
        }

    }
}
