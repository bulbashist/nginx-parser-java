import java.util.ArrayList;

public class IpGroup {
    public String ip;
    public int totalIPRequests;

    public ArrayList<HttpRequest> ipRequests;

    public IpGroup(HttpRequest req) {
        this.ip = req.ip;
        this.totalIPRequests = 1;
        this.ipRequests = new ArrayList<>();
        this.ipRequests.add(req);
    }

    public void addRequest(HttpRequest req) {
        this.ipRequests.add(req);
        this.totalIPRequests += 1;
    }
}
