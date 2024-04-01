import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class Main {
    static Pattern pattern;
    static ArrayList<TimeObj> dataset = new ArrayList<>();
    static DateTimeFormatter nginxFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
    static ZonedDateTime fromTime = null;
    static ZonedDateTime toTime = null;
    static Logger logger;

    public static void main(String[] args) {
        logger = new Logger();

        try {
            String inputFilepath = args.length >= 1 ? args[0] : "C:\\Users\\Bsuir\\Downloads\\access\\access.log";
            String outputFilepath = args.length >= 2 ? args[1] : "C:\\Users\\Bsuir\\Downloads\\access\\access3.json";
            String fromTimeArg = args.length >= 3 ? args[2] : null;
            String toTimeArg = args.length >= 4 ? args[3] : null;

            DateTimeFormatter argsTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH.mm.ss").withZone(ZoneOffset.ofHours(+3));
            if (fromTimeArg != null) {
                try {
                    fromTime = ZonedDateTime.parse(fromTimeArg, argsTimeFormatter);
                } catch (DateTimeParseException e) {
                    logger.logException(e, "Incorrect start date");
                }
            }
            if (toTimeArg != null) {
                try {
                    toTime = ZonedDateTime.parse(toTimeArg, argsTimeFormatter);
                } catch (DateTimeParseException e) {
                    logger.logException(e, "Incorrect end date");
                }
            }

            String regex = "^(\\S+) (\\S+) (\\S+) \\[(.+)] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"(\\S+)\" \"(.+)\"$";
            pattern = Pattern.compile(regex);

            File outputFile;
            try {
                outputFile = new File(outputFilepath);
            } catch (NullPointerException e) {
                logger.logException(e);
                return;
            }

            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                System.out.println("I/O error occurred");
                return;
            } catch (SecurityException e) {
                System.out.println("Access to file denied");
                return;
            }

            FileWriter myWriter;
            try {
                myWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.logException(e);
                return;
            }

            var lines = Files.readAllLines(Path.of(inputFilepath), StandardCharsets.UTF_8);
            logger.logMessage("File has been read");
            for (String line : lines) {
                parse(line);
            }
            logger.logMessage("Data has been transformed");

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String res = gson.toJson(dataset);
            try {
                myWriter.write(res);
                myWriter.close();
                logger.logMessage("Completed");
            } catch (IOException e) {
                logger.logException(e);
            }
        } catch (Exception e) {
            logger.logException(e);
        }
    }
//28s
    static void parse(String data) {
        Matcher matcher = pattern.matcher(data);

        if (matcher.matches()) {
            String ip = matcher.group(1);
            String time = matcher.group(4);
            String httpMethod = matcher.group(5);
            String requestedUrl = matcher.group(6);
            String protocol = matcher.group(7);
            int statusCode = Integer.parseInt(matcher.group(8));
            String userAgent = matcher.group(11);

            if (fromTime != null || toTime != null) {
                ZonedDateTime zonedDateTime;

                try {
                    zonedDateTime = ZonedDateTime.parse(time, nginxFormatter);
                } catch (DateTimeParseException e) {
                    logger.logException(e, "Incorrect data format in the file");
                    return;
                }

                if (fromTime != null) {
                    if (zonedDateTime.isBefore(fromTime)) return;
                }
                if (toTime != null) {
                    if (zonedDateTime.isAfter(toTime)) return;
                }
            }

            var hr = new HttpRequest(httpMethod, requestedUrl, protocol, statusCode, userAgent);
            var request = dataset.stream().parallel().filter(obj -> obj.time.equals(time)).findAny().orElse(null);
            if (request != null) {
                request.addRequest(hr, ip);
                counter++;
            } else {
                dataset.add(new TimeObj(hr, time, ip));
            }
        }
    }

    static int counter = 0;
}
