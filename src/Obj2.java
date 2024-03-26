import java.util.ArrayList;

public class Obj2 {

    public String ip;
    public int totalReqs;

    public ArrayList<HttpRequest> ipRequests;

    public Obj2(HttpRequest req) {
        this.ip = req.ip;
        this.totalReqs = 1;
        this.ipRequests = new ArrayList<HttpRequest>();
        this.ipRequests.add(req);
    }

    public void addRequest(HttpRequest req) {
        this.ipRequests.add(req);
        this.totalReqs += 1;
    }
}
