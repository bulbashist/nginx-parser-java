import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class Main {
    static Pattern pattern;
    static CopyOnWriteArrayList<TimeObj> dataset = new CopyOnWriteArrayList<>();

    static ZonedDateTime fromTime = null;
    static ZonedDateTime toTime = null;

    public static void main(String[] args) {
        long ts = System.currentTimeMillis();
        String inputFilepath = args.length >= 1 ? args[0] : "C:\\Users\\Bsuir\\Downloads\\access\\access2.log";
        String outputFilepath = args.length >= 2 ? args[1] : "C:\\Users\\Bsuir\\Downloads\\access\\access3.json";
        String fromTimeArg = args.length >= 3 ? args[2] : null;
        String toTimeArg = args.length >= 4 ? args[3] : null;

        DateTimeFormatter argsTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH.mm.ss").withZone(ZoneId.systemDefault());
        if (fromTimeArg != null) {
            fromTime = ZonedDateTime.parse(fromTimeArg, argsTimeFormatter);
        }
        if (toTimeArg != null) {
            toTime = ZonedDateTime.parse(toTimeArg, argsTimeFormatter);
        }

        try {
            String regex = "^(\\S+) (\\S+) (\\S+) \\[([^]]+)] \"(\\S+) (\\S+)\\S*\\s*\\S* (\\S+)\" (\\d+) (\\d+) \"([^\"]+)\" \"([^\"]+)\"$";
            pattern = Pattern.compile(regex);

            File inputFile = new File(inputFilepath);
            File outputFile = new File(outputFilepath);

            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                System.out.println("I/O error occurred");
            } catch (SecurityException e) {
                System.out.println("Access to file denied");
            }
            FileWriter myWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);

            Scanner myReader = new Scanner(inputFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                parse(data);
            }
            myReader.close();

            long rs = System.currentTimeMillis();
            System.out.println(rs - ts);

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String res = gson.toJson(dataset);
            myWriter.write(res);
            myWriter.close();

            System.out.println(System.currentTimeMillis() - rs);

        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }

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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, formatter);


            if (fromTime != null) {
                if (zonedDateTime.isBefore(fromTime)) return;

                if (toTime != null) {
                    if (zonedDateTime.isAfter(toTime)) return;
                }
            }


            var hr = new HttpRequest(ip, time, httpMethod, requestedUrl, protocol, statusCode, userAgent);
            var request = dataset.stream().filter(obj -> obj.time.equals(hr.time)).findAny().orElse(null);

            if (request != null) {
                request.addRequest(hr);
            } else {
                dataset.add(new TimeObj(hr));
            }


        }
    }
}
