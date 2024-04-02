import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    private final CopyOnWriteArrayList<TimeGroup> _dataset = new CopyOnWriteArrayList<>();

    public void add(HttpRequest hr) {
        var request = this._dataset
                .stream()
                .parallel()
                .filter(obj -> obj.time.equals(hr.time))
                .findAny()
                .orElse(null);
        if (request != null) {
            request.addRequest(hr);
        } else {
            this._dataset.add(new TimeGroup(hr));
        }
    }

    public CopyOnWriteArrayList<TimeGroup> getAll() {
        return this._dataset;
    }
}
