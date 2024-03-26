public class HttpRequest {
    public String ip;

    public String time;

    public String httpMethod;

    public String url;

    public String protocol;

    public int statusCode;

    public int responseSize;

    public String userAgent;

    public HttpRequest(
            String ip,
            String time,
            String httpMethod,
            String url,
            String protocol,
            int statusCode,
            int responseSize,
            String userAgent
    ) {
        this.ip = ip;
        this.time = time;
        this.httpMethod = httpMethod;
        this.url = url;
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
        this.userAgent = userAgent;
    }
}
