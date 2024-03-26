import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeObj {

    public String time;
    public int totalRequests;

    public ArrayList<Obj2> requests;


    public TimeObj(HttpRequest req) {
        this.time = req.time;
        this.totalRequests = 1;
        this.requests = new ArrayList<>();
        this.requests.add(new Obj2(req));
    }

    public void addRequest(HttpRequest req) {
        try {
            this.totalRequests += 1;

            var request = this.requests.stream().filter(obj -> obj.ip.equals(req.ip)).findAny().orElse(null);
            if (request != null) {
                request.addRequest(req);
            } else {
                this.requests.add(new Obj2(req));
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}


