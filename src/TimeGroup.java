import java.util.ArrayList;

public class TimeGroup {
    public String time;
    public int totalRequests;
    public ArrayList<IpGroup> requests;
    public TimeGroup(HttpRequest req) {
        this.time = req.time;
        this.totalRequests = 1;
        this.requests = new ArrayList<>();
        this.requests.add(new IpGroup(req));
    }

    public void addRequest(HttpRequest req) {
        try {
            this.totalRequests += 1;

            var request = this.requests
                    .stream()
                    .parallel()
                    .filter(obj -> obj.ip.equals(req.ip))
                    .findAny()
                    .orElse(null);

            if (request != null) {
                request.addRequest(req);
            } else {
                this.requests.add(new IpGroup(req));
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}


