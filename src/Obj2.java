import java.util.ArrayList;

public class Obj2 {
    public String ip;

    public int totalReqs;

    public ArrayList<HttpRequest> requests;

    public Obj2(HttpRequest req) {
        this.ip = req.ip;
        this.totalReqs = 1;
        this.requests = new ArrayList<HttpRequest>();
        this.requests.add(req);
    }
}
