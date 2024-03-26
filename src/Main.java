import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;

public class Main {
    static Pattern pattern;
    static ArrayList<TimeObj> dataset = new ArrayList<TimeObj>();

    public static void main(String[] args) {
        try {
            String regex = "^(\\S+) (\\S+) (\\S+) \\[([^]]+)] \"(\\S+) (\\S+)\\S*\\s*\\S* (\\S+)\" (\\d+) (\\d+) \"([^\"]+)\" \"([^\"]+)\"$";
            pattern = Pattern.compile(regex);

            File myObj = new File("C:\\Users\\Bsuir\\Downloads\\access\\access2.log");
            File resFile = new File("C:\\Users\\Bsuir\\Downloads\\access\\access3.json");
            FileWriter myWriter = null;
            try {
                myWriter = new FileWriter("C:\\Users\\Bsuir\\Downloads\\access\\access3.json");
            } catch (Exception e) {}


            try {
                boolean res = resFile.createNewFile();
            } catch (Exception e) {
            }


            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                parse(data);
            }
            myReader.close();

            try {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String res = gson.toJson(dataset);
                myWriter.write(res);
                myWriter.close();
            } catch (Exception e) {}


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    static void parse(String data) {
        Matcher matcher = pattern.matcher(data);

        if (matcher.matches()) {


            String ipAddress = matcher.group(1);
            String timestamp = matcher.group(4);
            String httpMethod = matcher.group(5);
            String requestedUrl = matcher.group(6);
            String protocol = matcher.group(7);
            int statusCode = Integer.parseInt(matcher.group(8));
            int responseSize = Integer.parseInt(matcher.group(9));
            String userAgent = matcher.group(11);
            HttpRequest hr = new HttpRequest(ipAddress, timestamp, httpMethod, requestedUrl, protocol, statusCode, responseSize, userAgent);


            boolean res = dataset.stream().anyMatch((obj) -> obj.time.equals(timestamp));

            if (!res) {
                dataset.add(new TimeObj(hr));
            } else {
//                dataset.stream().
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestamp, formatter);
        }
    }
}

/*
 ip
 -
 -
 [время]
 "метод путь версия хттп"
 статус
 длина ответа
 "хз"
 "браузер?"


 "^(\\S+) (\\S+) (\\S+) \\[([^\\]]+)\\] \"(\\S+) (\\S+)\\S*\\s*\\S* (\\S+)\" (\\d+) (\\d+) \"([^\"]+)\" \"([^\"]+)\"$";
*/