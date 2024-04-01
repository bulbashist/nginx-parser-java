import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeObj {
    public String time;
    public int totalRequests;
    public ArrayList<Obj2> requests;
    public TimeObj(HttpRequest req, String time, String ip) {
        this.time = time;
        this.totalRequests = 1;
        this.requests = new ArrayList<>();
        this.requests.add(new Obj2(req, ip));
    }

    public void addRequest(HttpRequest req, String ip) {
        try {
            this.totalRequests += 1;

            var request = this.requests
                    .stream()
                    .parallel()
                    .filter(obj -> obj.ip.equals(ip))
                    .findAny()
                    .orElse(null);

            if (request != null) {
                request.addRequest(req);
            } else {
                this.requests.add(new Obj2(req, ip));
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}


