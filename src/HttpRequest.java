public class HttpRequest {
    public transient final String ip;

    public transient final String time;

    public String httpMethod;

    public String url;

    public String protocol;

    public int statusCode;

    public String userAgent;

    public HttpRequest(
            String ip,
            String time,
            String httpMethod,
            String url,
            String protocol,
            int statusCode,
            String userAgent
    ) {
        this.ip = ip;
        this.time = time;
        this.httpMethod = httpMethod;
        this.url = url;
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.userAgent = userAgent;
    }
}
