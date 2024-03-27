import java.util.ArrayList;

public class Obj2 {

    public String ip;
    public int totalIPRequests;

    public ArrayList<HttpRequest> ipRequests;

    public Obj2(HttpRequest req) {
        this.ip = req.ip;
        this.totalIPRequests = 1;
        this.ipRequests = new ArrayList<HttpRequest>();
        this.ipRequests.add(req);
    }

    public void addRequest(HttpRequest req) {
        this.ipRequests.add(req);
        this.totalIPRequests += 1;
    }
}
