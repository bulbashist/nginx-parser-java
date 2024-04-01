public class HttpRequest {
    public String httpMethod;

    public String url;

    public String protocol;

    public int statusCode;

    public String userAgent;

    public HttpRequest(

            String httpMethod,
            String url,
            String protocol,
            int statusCode,
            String userAgent
    ) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.userAgent = userAgent;
    }
}
