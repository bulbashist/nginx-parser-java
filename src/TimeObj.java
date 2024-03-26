import java.util.ArrayList;

public class TimeObj {
    public String time;

    public int totalRequests;

    public ArrayList<Obj2> requests;

    public TimeObj(HttpRequest req) {
        this.time = req.time;
        this.totalRequests = 1;
        this.requests = new ArrayList<Obj2>();
        this.requests.add(new Obj2(req));
    }

    public class Data {

    }
}


