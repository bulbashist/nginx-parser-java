import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxParser {
    private final DateTimeFormatter _nginxDateFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
    private final Pattern _pattern;
    private final Pattern _errorPattern;
    private final ZonedDateTime _fromTime;
    private final ZonedDateTime _toTime;
    private final Logger _logger;

    public NginxParser(ZonedDateTime fromTime, ZonedDateTime toTime, Logger logger) {
        this._fromTime = fromTime;
        this._toTime = toTime;
        this._logger = logger;

        String regex = "^(\\S+) (\\S+) (\\S+) \\[(.+)] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"(.*)\" \"(.*)\"$";
        String errorRegex = "^(\\S+) (\\S+) (\\S+) \\[(.+)] \"(.*)\" (\\d+) (\\d+) \"(.*)\" \"(.*)\"$";
        this._pattern = Pattern.compile(regex);
        this._errorPattern = Pattern.compile(errorRegex);
    }
    public HttpRequest parse(String line) {
        Matcher matcher = this._pattern.matcher(line);

        if (matcher.matches()) {
            String ip = matcher.group(1);
            String time = matcher.group(4);
            String httpMethod = matcher.group(5);
            String requestedUrl = matcher.group(6);
            String protocol = matcher.group(7);
            int statusCode = Integer.parseInt(matcher.group(8));
            String userAgent = matcher.group(11);

            if (this.isInTimeInterval(time)) {
                return new HttpRequest(ip, time, httpMethod, requestedUrl, protocol, statusCode, userAgent);
            } else {
                return null;
            }
        } else {
            Matcher errMatcher = this._errorPattern.matcher(line);
            if (errMatcher.matches()) {
                String ip = errMatcher.group(1);
                String time = errMatcher.group(4);
                int statusCode = Integer.parseInt(errMatcher.group(6));
                String userAgent = errMatcher.group(9);

                if (this.isInTimeInterval(time)) {
                    return new HttpRequest(ip, time, "", "", "", statusCode, userAgent);
                } else {
                    return null;
                }

            } else {
                this._logger.logMessage("Not a Nginx format" + " " + line);
                return null;
            }
        }
    }

    public boolean isInTimeInterval(String time) {
        if (this._fromTime == null && this._toTime == null) return true;

        ZonedDateTime zonedDateTime;
        try {
            zonedDateTime = ZonedDateTime.parse(time, this._nginxDateFormatter);
        } catch (DateTimeParseException e) {
          this._logger.logException(e, "Incorrect data format in the file");
            return false;
        }

        boolean r1 = true;
        boolean r2 = true;
        if (this._fromTime != null) {
            r1 = !zonedDateTime.isBefore(this._fromTime);
        }
        if (this._toTime != null) {
            r2 = !zonedDateTime.isAfter(this._toTime);
        }
        return r1 && r2;
    }
}
