import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public Logger() {

    }

    public void logException(Exception e) {
        System.out.println(this._getTime() + " " + e.getMessage());
    }

    public void logException(Exception e, String msg) {
        System.out.println(this._getTime() + " " + msg);
    }

    public void logMessage(String msg) {
        System.out.println(this._getTime() + " " + msg);
    }

    private String _getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
