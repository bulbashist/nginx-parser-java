import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxParser {
    DateTimeFormatter nginxDateFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    Pattern pattern;
    ZonedDateTime fromTime;
    ZonedDateTime toTime;

    public NginxParser(ZonedDateTime fromTime, ZonedDateTime toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        String regex = "^(\\S+) (\\S+) (\\S+) \\[(.+)] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"(\\S+)\" \"(.+)\"$";
        this.pattern = Pattern.compile(regex);
    }
    public HttpRequest parse(String line) {
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            String ip = matcher.group(1);
            String time = matcher.group(4);
            String httpMethod = matcher.group(5);
            String requestedUrl = matcher.group(6);
            String protocol = matcher.group(7);
            int statusCode = Integer.parseInt(matcher.group(8));
            String userAgent = matcher.group(11);

            if (this.isInTimeInterval(time)) {
                return new HttpRequest(httpMethod, requestedUrl, protocol, statusCode, userAgent);
            } else {
                return null;
            }
        }
    }

    public boolean isInTimeInterval(String time) {
        if (fromTime != null || toTime != null) {
            ZonedDateTime zonedDateTime;

            try {
                zonedDateTime = ZonedDateTime.parse(time, nginxDateFormatter);
            } catch (DateTimeParseException e) {
//                logger.logException(e, "Incorrect data format in the file");
                return false;
            }

            if (fromTime != null) {
                return !zonedDateTime.isBefore(fromTime);
            }
            if (toTime != null) {
                return !zonedDateTime.isAfter(toTime);
            }
        }
        return true;
    }
}
