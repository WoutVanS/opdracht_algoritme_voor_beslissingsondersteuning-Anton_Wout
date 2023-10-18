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
}
