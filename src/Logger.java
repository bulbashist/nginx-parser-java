import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger implements AutoCloseable {
    private FileWriter _writer;

    public Logger() {
        File file = new File("log.txt");
        try {
            this._writer = new FileWriter(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            this.logException(e);
        }
    }
    public void logException(Exception e) {
        String message = this._getTime() + " " + e.getMessage();
        System.out.println(message);
        this.logToFile(message);
    }

    public void logException(Exception e, String msg) {
        String message = this._getTime() + " " + msg;
        System.out.println(message);
        this.logToFile(message);
    }

    public void logMessage(String msg) {
        String message = this._getTime() + " " + msg;
        System.out.println(message);
        this.logToFile(message);
    }

    private void logToFile(String msg) {
        try {
            this._writer.append(msg);
            this._writer.write(System.lineSeparator());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String _getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public void close() {
        try {
            this._writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
