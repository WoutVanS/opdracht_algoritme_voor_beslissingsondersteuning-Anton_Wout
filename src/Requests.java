import java.util.ArrayList;

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
}
